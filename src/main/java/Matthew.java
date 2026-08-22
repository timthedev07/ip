public class Matthew {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Matthew(String filePath) {
        ui = new Ui();
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

        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();

            } catch (MatthewException e) {
                ui.showError(e.getMessage());

            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Matthew("data/matthew.txt").run();
    }
}
