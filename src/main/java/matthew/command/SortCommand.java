package matthew.command;

import matthew.storage.Storage;
import matthew.task.TaskList;
import matthew.ui.Ui;

/** Displays the task list in alphabetical order without changing it. */
public class SortCommand extends Command {

    /** Creates a sort command. */
    public SortCommand() {
    }

    /**
     * Displays a sorted copy of the task list.
     *
     * @param tasks Task list to sort for display.
     * @param ui User interface used to display the sorted tasks.
     * @param storage Unused storage supplied by the command loop.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showSortedTasks(tasks.getTasksSortedByDescription());
    }
}
