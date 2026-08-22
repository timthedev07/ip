public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MatthewException {
        Task removedTask = tasks.delete(taskNumber);
        storage.save(tasks);
        ui.showDeletedTask(removedTask, tasks.size());
    }
}
