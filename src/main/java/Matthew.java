import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Matthew {
    private static final String line =
            "____________________________________________________________\n";

    private static final Path DATA_FILE =
            Path.of("data", "matthew.txt");

    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private enum Command {
        BYE,
        LIST,
        MARK,
        UNMARK,
        DELETE,
        TODO,
        DEADLINE,
        EVENT,
        ON,
        UNKNOWN
    }

    public static void main(String[] args) {

        String banner = line
                + " __  __       _   _   _                   \n"
                + "|  \\/  | __ _| |_| |_| |__   _____      __\n"
                + "| |\\/| |/ _` | __| __| '_ \\ / _ \\ \\ /\\ / /\n"
                + "| |  | | (_| | |_| |_| | | |  __/\\ V  V / \n"
                + "|_|  |_|\\__,_|\\__|\\__|_| |_|\\___| \\_/\\_/  \n"
                + "Hello! I'm Matthew.\n"
                + "What can I do for you?\n"
                + line;

        System.out.println(banner);

        ArrayList<Task> tasks = loadTasks();
        boolean isRunning = true;

        while (isRunning) {
            String response = System.console().readLine();

            try {
                Command command = getCommand(response);

                switch (command) {
                case BYE:
                    isRunning = false;
                    break;

                case LIST:
                    printTaskList(tasks);
                    break;

                case MARK:
                    if (!response.startsWith("mark ")) {
                        throw new MatthewException(
                                "Please tell me which task to mark, e.g. mark 2.");
                    }

                    int markNumber = getTaskNumber(
                            response.substring(5), tasks.size());

                    Task markedTask = tasks.get(markNumber - 1);
                    markedTask.markAsDone();
                    saveTasks(tasks);

                    System.out.print(line);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + markedTask);
                    System.out.print(line);
                    break;

                case UNMARK:
                    if (!response.startsWith("unmark ")) {
                        throw new MatthewException(
                                "Please tell me which task to unmark, e.g. unmark 2.");
                    }

                    int unmarkNumber = getTaskNumber(
                            response.substring(7), tasks.size());

                    Task unmarkedTask = tasks.get(unmarkNumber - 1);
                    unmarkedTask.markAsNotDone();
                    saveTasks(tasks);

                    System.out.print(line);
                    System.out.println(
                            "OK, I've marked this task as not done yet:");
                    System.out.println("  " + unmarkedTask);
                    System.out.print(line);
                    break;

                case DELETE:
                    if (!response.startsWith("delete ")) {
                        throw new MatthewException(
                                "Please tell me which task to delete, e.g. delete 3.");
                    }

                    int deleteNumber = getTaskNumber(
                            response.substring(7), tasks.size());

                    Task removedTask = tasks.remove(deleteNumber - 1);
                    saveTasks(tasks);

                    System.out.print(line);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println(
                            "Now you have " + tasks.size()
                                    + " tasks in the list.");
                    System.out.print(line);
                    break;

                case TODO:
                    if (!response.startsWith("todo ")) {
                        throw new MatthewException(
                                "A todo needs a description.");
                    }

                    String todoDescription = response.substring(5).trim();

                    if (todoDescription.isEmpty()) {
                        throw new MatthewException(
                                "A todo needs a description.");
                    }

                    Task todo = new Todo(todoDescription);
                    tasks.add(todo);
                    saveTasks(tasks);

                    printAddedTask(todo, tasks.size());
                    break;

                case DEADLINE:
                    handleDeadline(response, tasks);
                    break;

                case EVENT:
                    handleEvent(response, tasks);
                    break;

                case ON:
                    handleOn(response, tasks);
                    break;

                case UNKNOWN:
                    throw new MatthewException(
                            "I don't recognise that command.");
                }

            } catch (MatthewException e) {
                System.out.println(
                        wrap("OOPS!!! " + e.getMessage()));
            }
        }

        System.out.println(
                wrap("Goodbye! Have a nice day!"));
    }

    public static Command getCommand(String response) {
        String trimmed = response.trim();

        if (trimmed.isEmpty()) {
            return Command.UNKNOWN;
        }

        String commandWord = trimmed.split("\\s+")[0];

        switch (commandWord) {
        case "bye":
            return Command.BYE;
        case "list":
            return Command.LIST;
        case "mark":
            return Command.MARK;
        case "unmark":
            return Command.UNMARK;
        case "delete":
            return Command.DELETE;
        case "todo":
            return Command.TODO;
        case "deadline":
            return Command.DEADLINE;
        case "event":
            return Command.EVENT;
        case "on":
            return Command.ON;
        default:
            return Command.UNKNOWN;
        }
    }

    public static void handleDeadline(
            String response, ArrayList<Task> tasks)
            throws MatthewException {

        if (!response.startsWith("deadline ")) {
            throw new MatthewException(
                    "A deadline needs a description and /by date/time.");
        }

        String input = response.substring(9).trim();
        int byIndex = input.indexOf(" /by ");

        if (byIndex == -1) {
            throw new MatthewException(
                    "A deadline must include '/by'. "
                            + "Example: deadline return book /by 2019-12-02 1800");
        }

        String description = input.substring(0, byIndex).trim();
        String dateTimeText = input.substring(byIndex + 5).trim();

        if (description.isEmpty()) {
            throw new MatthewException(
                    "A deadline needs a description.");
        }

        LocalDateTime by = parseDateTime(dateTimeText);

        Task deadline = new Deadline(description, by);
        tasks.add(deadline);
        saveTasks(tasks);

        printAddedTask(deadline, tasks.size());
    }

    public static void handleEvent(
            String response, ArrayList<Task> tasks)
            throws MatthewException {

        if (!response.startsWith("event ")) {
            throw new MatthewException(
                    "An event needs a description, /from date/time and /to date/time.");
        }

        String input = response.substring(6).trim();

        int fromIndex = input.indexOf(" /from ");
        int toIndex = input.indexOf(" /to ");

        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new MatthewException(
                    "An event must use '/from' and '/to'. "
                            + "Example: event meeting /from 2019-12-02 1400 "
                            + "/to 2019-12-02 1600");
        }

        String description = input.substring(0, fromIndex).trim();
        String fromText = input.substring(fromIndex + 7, toIndex).trim();
        String toText = input.substring(toIndex + 5).trim();

        if (description.isEmpty()) {
            throw new MatthewException(
                    "An event needs a description.");
        }

        LocalDateTime from = parseDateTime(fromText);
        LocalDateTime to = parseDateTime(toText);

        if (to.isBefore(from)) {
            throw new MatthewException(
                    "The event end time cannot be before its start time.");
        }

        Task event = new Event(description, from, to);
        tasks.add(event);
        saveTasks(tasks);

        printAddedTask(event, tasks.size());
    }

    public static void handleOn(
            String response, ArrayList<Task> tasks)
            throws MatthewException {

        if (!response.startsWith("on ")) {
            throw new MatthewException(
                    "Please provide a date, e.g. on 2019-12-02.");
        }

        String dateText = response.substring(3).trim();
        LocalDate date = parseDate(dateText);

        System.out.print(line);
        System.out.println("Tasks occurring on " + date + ":");

        int counter = 1;

        for (Task task : tasks) {
            boolean matches = false;

            if (task instanceof Deadline) {
                matches = ((Deadline) task).occursOn(date);
            } else if (task instanceof Event) {
                matches = ((Event) task).occursOn(date);
            }

            if (matches) {
                System.out.println(counter + "." + task);
                counter++;
            }
        }

        if (counter == 1) {
            System.out.println("No deadlines or events found.");
        }

        System.out.print(line);
    }

    public static LocalDateTime parseDateTime(String input)
            throws MatthewException {
        try {
            return LocalDateTime.parse(input, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "Date/time must use yyyy-MM-dd HHmm, "
                            + "e.g. 2019-12-02 1800.");
        }
    }

    public static LocalDate parseDate(String input)
            throws MatthewException {
        try {
            return LocalDate.parse(input, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "Date must use yyyy-MM-dd, e.g. 2019-12-02.");
        }
    }

    public static int getTaskNumber(String input, int taskCount)
            throws MatthewException {

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new MatthewException(
                    "The task number must be a number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new MatthewException(
                    "There is no task numbered " + taskNumber + ".");
        }

        return taskNumber;
    }

    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Path parent = DATA_FILE.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(DATA_FILE)) {
                Files.createFile(DATA_FILE);
                return tasks;
            }

            List<String> lines = Files.readAllLines(DATA_FILE);

            for (String dataLine : lines) {
                if (dataLine.isBlank()) {
                    continue;
                }

                try {
                    Task task = parseTask(dataLine);
                    tasks.add(task);
                } catch (MatthewException e) {
                    System.out.println(
                            wrap("Warning: skipped corrupted saved task: "
                                    + dataLine));
                }
            }

        } catch (IOException e) {
            System.out.println(
                    wrap("OOPS!!! I couldn't load the saved tasks."));
        }

        return tasks;
    }

    public static Task parseTask(String dataLine)
            throws MatthewException {

        String[] parts = dataLine.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new MatthewException("Invalid saved task.");
        }

        String type = parts[0];
        boolean isDone;

        if (parts[1].equals("1")) {
            isDone = true;
        } else if (parts[1].equals("0")) {
            isDone = false;
        } else {
            throw new MatthewException("Invalid task status.");
        }

        Task task;

        try {
            switch (type) {
            case "T":
                if (parts.length != 3) {
                    throw new MatthewException("Invalid todo format.");
                }
                task = new Todo(parts[2]);
                break;

            case "D":
                if (parts.length != 4) {
                    throw new MatthewException("Invalid deadline format.");
                }
                task = new Deadline(
                        parts[2], LocalDateTime.parse(parts[3]));
                break;

            case "E":
                if (parts.length != 5) {
                    throw new MatthewException("Invalid event format.");
                }
                task = new Event(
                        parts[2],
                        LocalDateTime.parse(parts[3]),
                        LocalDateTime.parse(parts[4]));
                break;

            default:
                throw new MatthewException("Unknown saved task type.");
            }
        } catch (DateTimeParseException e) {
            throw new MatthewException("Invalid saved date/time.");
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }

    public static void saveTasks(ArrayList<Task> tasks)
            throws MatthewException {

        try {
            Path parent = DATA_FILE.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            ArrayList<String> lines = new ArrayList<>();

            for (Task task : tasks) {
                String status = task.isDone() ? "1" : "0";

                if (task instanceof Todo) {
                    lines.add(
                            "T | " + status + " | "
                                    + task.getDescription());

                } else if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;

                    lines.add(
                            "D | " + status + " | "
                                    + deadline.getDescription() + " | "
                                    + deadline.getBy());

                } else if (task instanceof Event) {
                    Event event = (Event) task;

                    lines.add(
                            "E | " + status + " | "
                                    + event.getDescription() + " | "
                                    + event.getFrom() + " | "
                                    + event.getTo());
                }
            }

            Files.write(DATA_FILE, lines);

        } catch (IOException e) {
            throw new MatthewException(
                    "I couldn't save the task list.");
        }
    }

    public static void printTaskList(ArrayList<Task> tasks) {
        System.out.print(line);
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }

        System.out.print(line);
    }

    public static void printAddedTask(Task task, int taskCount) {
        System.out.print(line);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list.");
        System.out.print(line);
    }

    public static String wrap(String text) {
        return line + text + "\n" + line;
    }
}
