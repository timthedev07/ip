public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MatthewException {
        Task task = tasks.unmark(taskNumber);
        storage.save(tasks);
        ui.showUnmarkedTask(task);
    }
}
