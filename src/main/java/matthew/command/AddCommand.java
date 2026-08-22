package matthew.command;

import matthew.exception.MatthewException;
import matthew.storage.Storage;
import matthew.task.Task;
import matthew.task.TaskList;
import matthew.ui.Ui;

public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MatthewException {
        tasks.add(task);
        storage.save(tasks);
        ui.showAddedTask(task, tasks.size());
    }
}
