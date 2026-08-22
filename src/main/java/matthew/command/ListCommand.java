package matthew.command;

import matthew.storage.Storage;
import matthew.task.TaskList;
import matthew.ui.Ui;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getAll());
    }
}
