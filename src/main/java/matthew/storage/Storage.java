package matthew.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import matthew.exception.MatthewException;
import matthew.task.Deadline;
import matthew.task.Event;
import matthew.task.Task;
import matthew.task.TaskList;
import matthew.task.Todo;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public ArrayList<Task> load() throws MatthewException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                return tasks;
            }

            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }

                tasks.add(parseTask(line));
            }

            return tasks;

        } catch (IOException e) {
            throw new MatthewException(
                    "I couldn't load the saved tasks.");
        }
    }

    public void save(TaskList taskList)
            throws MatthewException {

        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            ArrayList<String> lines = new ArrayList<>();

            for (Task task : taskList.getAll()) {
                lines.add(formatTask(task));
            }

            Files.write(filePath, lines);

        } catch (IOException e) {
            throw new MatthewException(
                    "I couldn't save the task list.");
        }
    }

    private Task parseTask(String line)
            throws MatthewException {

        String[] parts = line.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new MatthewException(
                    "The saved task data is corrupted.");
        }

        boolean isDone;

        if (parts[1].equals("1")) {
            isDone = true;
        } else if (parts[1].equals("0")) {
            isDone = false;
        } else {
            throw new MatthewException(
                    "The saved task data is corrupted.");
        }

        Task task;

        try {
            switch (parts[0]) {
            case "T":
                if (parts.length != 3) {
                    throw new MatthewException(
                            "The saved todo data is corrupted.");
                }
                task = new Todo(parts[2]);
                break;

            case "D":
                if (parts.length != 4) {
                    throw new MatthewException(
                            "The saved deadline data is corrupted.");
                }
                task = new Deadline(
                        parts[2],
                        LocalDateTime.parse(parts[3]));
                break;

            case "E":
                if (parts.length != 5) {
                    throw new MatthewException(
                            "The saved event data is corrupted.");
                }
                task = new Event(
                        parts[2],
                        LocalDateTime.parse(parts[3]),
                        LocalDateTime.parse(parts[4]));
                break;

            default:
                throw new MatthewException(
                        "The saved task data is corrupted.");
            }
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "The saved date/time data is corrupted.");
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }

    private String formatTask(Task task)
            throws MatthewException {

        String status = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + status + " | "
                    + task.getDescription();

        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;

            return "D | " + status + " | "
                    + deadline.getDescription() + " | "
                    + deadline.getBy();

        } else if (task instanceof Event) {
            Event event = (Event) task;

            return "E | " + status + " | "
                    + event.getDescription() + " | "
                    + event.getFrom() + " | "
                    + event.getTo();
        }

        throw new MatthewException(
                "I couldn't save an unknown task type.");
    }
}
