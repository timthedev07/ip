import java.time.LocalDate;
import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int taskNumber) {
        return tasks.remove(taskNumber - 1);
    }

    public Task mark(int taskNumber) {
        Task task = get(taskNumber);
        task.markAsDone();
        return task;
    }

    public Task unmark(int taskNumber) {
        Task task = get(taskNumber);
        task.markAsNotDone();
        return task;
    }

    public Task get(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    public ArrayList<Task> getTasksOn(LocalDate date) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task instanceof Deadline
                    && ((Deadline) task).occursOn(date)) {
                matchingTasks.add(task);
            } else if (task instanceof Event
                    && ((Event) task).occursOn(date)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}
