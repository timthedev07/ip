package matthew.command;

import java.time.LocalDate;

import matthew.storage.Storage;
import matthew.task.TaskList;
import matthew.ui.Ui;

public class OnCommand extends Command {
    private final LocalDate date;

    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOn(date.toString(), tasks.getTasksOn(date));
    }
}
