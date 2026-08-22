package matthew.command;

import matthew.exception.MatthewException;
import matthew.storage.Storage;
import matthew.task.Task;
import matthew.task.TaskList;
import matthew.ui.Ui;

public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MatthewException {
        Task task = tasks.mark(taskNumber);
        storage.save(tasks);
        ui.showMarkedTask(task);
    }
}
