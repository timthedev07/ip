package matthew.command;

import matthew.exception.MatthewException;
import matthew.storage.Storage;
import matthew.task.TaskList;
import matthew.ui.Ui;

public abstract class Command {

    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws MatthewException;

    public boolean isExit() {
        return false;
    }
}
