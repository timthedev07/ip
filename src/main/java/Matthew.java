import java.time.LocalDate;
import java.util.ArrayList;

public class Matthew {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public Matthew(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);

        TaskList loadedTasks;

        try {
            loadedTasks = new TaskList(storage.load());
        } catch (MatthewException e) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }

        tasks = loadedTasks;
    }

    public void run() {
        ui.showWelcome();

        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readCommand();

            try {
                Command command = parser.getCommand(input);

                switch (command) {
                case BYE:
                    isRunning = false;
                    break;

                case LIST:
                    ui.showTaskList(tasks.getAll());
                    break;

                case MARK:
                    handleMark(input);
                    break;

                case UNMARK:
                    handleUnmark(input);
                    break;

                case DELETE:
                    handleDelete(input);
                    break;

                case TODO:
                    handleTodo(input);
                    break;

                case DEADLINE:
                    handleDeadline(input);
                    break;

                case EVENT:
                    handleEvent(input);
                    break;

                case ON:
                    handleOn(input);
                    break;

                case UNKNOWN:
                    throw new MatthewException(
                            "I don't recognise that command.");
                }

            } catch (MatthewException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    private void handleMark(String input)
            throws MatthewException {

        if (!input.startsWith("mark ")) {
            throw new MatthewException(
                    "Please tell me which task to mark, e.g. mark 2.");
        }

        int taskNumber = parser.parseTaskNumber(
                input.substring(5), tasks.size());

        Task task = tasks.mark(taskNumber);
        storage.save(tasks);
        ui.showMarkedTask(task);
    }

    private void handleUnmark(String input)
            throws MatthewException {

        if (!input.startsWith("unmark ")) {
            throw new MatthewException(
                    "Please tell me which task to unmark, e.g. unmark 2.");
        }

        int taskNumber = parser.parseTaskNumber(
                input.substring(7), tasks.size());

        Task task = tasks.unmark(taskNumber);
        storage.save(tasks);
        ui.showUnmarkedTask(task);
    }

    private void handleDelete(String input)
            throws MatthewException {

        if (!input.startsWith("delete ")) {
            throw new MatthewException(
                    "Please tell me which task to delete, e.g. delete 3.");
        }

        int taskNumber = parser.parseTaskNumber(
                input.substring(7), tasks.size());

        Task task = tasks.delete(taskNumber);
        storage.save(tasks);
        ui.showDeletedTask(task, tasks.size());
    }

    private void handleTodo(String input)
            throws MatthewException {

        String description = parser.parseTodoDescription(input);
        Task task = new Todo(description);

        tasks.add(task);
        storage.save(tasks);
        ui.showAddedTask(task, tasks.size());
    }

    private void handleDeadline(String input)
            throws MatthewException {

        Task task = parser.parseDeadline(input);

        tasks.add(task);
        storage.save(tasks);
        ui.showAddedTask(task, tasks.size());
    }

    private void handleEvent(String input)
            throws MatthewException {

        Task task = parser.parseEvent(input);

        tasks.add(task);
        storage.save(tasks);
        ui.showAddedTask(task, tasks.size());
    }

    private void handleOn(String input)
            throws MatthewException {

        LocalDate date = parser.parseOnDate(input);
        ArrayList<Task> matchingTasks = tasks.getTasksOn(date);

        ui.showTasksOn(date.toString(), matchingTasks);
    }

    public static void main(String[] args) {
        new Matthew("data/matthew.txt").run();
    }
}
