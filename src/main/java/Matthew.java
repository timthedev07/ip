import java.util.ArrayList;

public class Matthew {
    private static final String line =
            "____________________________________________________________\n";

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

        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String response = System.console().readLine();

            try {
                if (response.equals("bye")) {
                    break;

                } else if (response.equals("list")) {
                    System.out.print(line);
                    System.out.println("Here are the tasks in your list:");

                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }

                    System.out.print(line);

                } else if (response.startsWith("mark")) {
                    if (!response.startsWith("mark ")) {
                        throw new MatthewException(
                                "Please tell me which task to mark, e.g. mark 2.");
                    }

                    int taskNumber = getTaskNumber(
                            response.substring(5), tasks.size());

                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();

                    System.out.print(line);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + task);
                    System.out.print(line);

                } else if (response.startsWith("unmark")) {
                    if (!response.startsWith("unmark ")) {
                        throw new MatthewException(
                                "Please tell me which task to unmark, e.g. unmark 2.");
                    }

                    int taskNumber = getTaskNumber(
                            response.substring(7), tasks.size());

                    Task task = tasks.get(taskNumber - 1);
                    task.markAsNotDone();

                    System.out.print(line);
                    System.out.println(
                            "OK, I've marked this task as not done yet:");
                    System.out.println("  " + task);
                    System.out.print(line);

                } else if (response.startsWith("delete")) {
                    if (!response.startsWith("delete ")) {
                        throw new MatthewException(
                                "Please tell me which task to delete, e.g. delete 3.");
                    }

                    int taskNumber = getTaskNumber(
                            response.substring(7), tasks.size());

                    Task removedTask = tasks.remove(taskNumber - 1);

                    System.out.print(line);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println(
                            "Now you have " + tasks.size()
                                    + " tasks in the list.");
                    System.out.print(line);

                } else if (response.startsWith("todo")) {
                    if (!response.startsWith("todo ")) {
                        throw new MatthewException(
                                "A todo needs a description.");
                    }

                    String description = response.substring(5).trim();

                    if (description.isEmpty()) {
                        throw new MatthewException(
                                "A todo needs a description.");
                    }

                    Task task = new Todo(description);
                    tasks.add(task);

                    printAddedTask(task, tasks.size());

                } else if (response.startsWith("deadline")) {
                    if (!response.startsWith("deadline ")) {
                        throw new MatthewException(
                                "A deadline needs a description and /by time.");
                    }

                    String input = response.substring(9).trim();
                    int byIndex = input.indexOf(" /by ");

                    if (byIndex == -1) {
                        throw new MatthewException(
                                "A deadline must include '/by'. "
                                        + "For example: "
                                        + "deadline return book /by Sunday");
                    }

                    String description =
                            input.substring(0, byIndex).trim();
                    String by =
                            input.substring(byIndex + 5).trim();

                    if (description.isEmpty()) {
                        throw new MatthewException(
                                "A deadline needs a description.");
                    }

                    if (by.isEmpty()) {
                        throw new MatthewException(
                                "A deadline needs a time after '/by'.");
                    }

                    Task task = new Deadline(description, by);
                    tasks.add(task);

                    printAddedTask(task, tasks.size());

                } else if (response.startsWith("event")) {
                    if (!response.startsWith("event ")) {
                        throw new MatthewException(
                                "An event needs a description, "
                                        + "/from time and /to time.");
                    }

                    String input = response.substring(6).trim();

                    int fromIndex = input.indexOf(" /from ");
                    int toIndex = input.indexOf(" /to ");

                    if (fromIndex == -1 || toIndex == -1
                            || toIndex < fromIndex) {
                        throw new MatthewException(
                                "An event must use '/from' and '/to'. "
                                        + "For example: "
                                        + "event meeting /from Mon 2pm /to 4pm");
                    }

                    String description =
                            input.substring(0, fromIndex).trim();
                    String from =
                            input.substring(fromIndex + 7, toIndex).trim();
                    String to =
                            input.substring(toIndex + 5).trim();

                    if (description.isEmpty()) {
                        throw new MatthewException(
                                "An event needs a description.");
                    }

                    if (from.isEmpty()) {
                        throw new MatthewException(
                                "An event needs a start time after '/from'.");
                    }

                    if (to.isEmpty()) {
                        throw new MatthewException(
                                "An event needs an end time after '/to'.");
                    }

                    Task task = new Event(description, from, to);
                    tasks.add(task);

                    printAddedTask(task, tasks.size());

                } else {
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