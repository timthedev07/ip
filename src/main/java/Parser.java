import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Command getCommand(String input) {
        String trimmed = input.trim();

        if (trimmed.isEmpty()) {
            return Command.UNKNOWN;
        }

        String commandWord = trimmed.split("\\s+")[0];

        switch (commandWord) {
        case "bye":
            return Command.BYE;
        case "list":
            return Command.LIST;
        case "mark":
            return Command.MARK;
        case "unmark":
            return Command.UNMARK;
        case "delete":
            return Command.DELETE;
        case "todo":
            return Command.TODO;
        case "deadline":
            return Command.DEADLINE;
        case "event":
            return Command.EVENT;
        case "on":
            return Command.ON;
        default:
            return Command.UNKNOWN;
        }
    }

    public int parseTaskNumber(String input, int taskCount)
            throws MatthewException {

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new MatthewException(
                    "The task number must be a number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new MatthewException(
                    "There is no task numbered " + taskNumber + ".");
        }

        return taskNumber;
    }

    public String parseTodoDescription(String input)
            throws MatthewException {

        if (!input.startsWith("todo ")) {
            throw new MatthewException(
                    "A todo needs a description.");
        }

        String description = input.substring(5).trim();

        if (description.isEmpty()) {
            throw new MatthewException(
                    "A todo needs a description.");
        }

        return description;
    }

    public Deadline parseDeadline(String input)
            throws MatthewException {

        if (!input.startsWith("deadline ")) {
            throw new MatthewException(
                    "A deadline needs a description and /by date/time.");
        }

        String content = input.substring(9).trim();
        int byIndex = content.indexOf(" /by ");

        if (byIndex == -1) {
            throw new MatthewException(
                    "A deadline must include '/by'. "
                            + "Example: deadline return book /by 2019-12-02 1800");
        }

        String description = content.substring(0, byIndex).trim();
        String dateTimeText = content.substring(byIndex + 5).trim();

        if (description.isEmpty()) {
            throw new MatthewException(
                    "A deadline needs a description.");
        }

        return new Deadline(description, parseDateTime(dateTimeText));
    }

    public Event parseEvent(String input)
            throws MatthewException {

        if (!input.startsWith("event ")) {
            throw new MatthewException(
                    "An event needs a description, /from date/time and /to date/time.");
        }

        String content = input.substring(6).trim();

        int fromIndex = content.indexOf(" /from ");
        int toIndex = content.indexOf(" /to ");

        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new MatthewException(
                    "An event must use '/from' and '/to'. "
                            + "Example: event meeting /from 2019-12-02 1400 "
                            + "/to 2019-12-02 1600");
        }

        String description = content.substring(0, fromIndex).trim();
        String fromText = content.substring(fromIndex + 7, toIndex).trim();
        String toText = content.substring(toIndex + 5).trim();

        if (description.isEmpty()) {
            throw new MatthewException(
                    "An event needs a description.");
        }

        LocalDateTime from = parseDateTime(fromText);
        LocalDateTime to = parseDateTime(toText);

        if (to.isBefore(from)) {
            throw new MatthewException(
                    "The event end time cannot be before its start time.");
        }

        return new Event(description, from, to);
    }

    public LocalDate parseOnDate(String input)
            throws MatthewException {

        if (!input.startsWith("on ")) {
            throw new MatthewException(
                    "Please provide a date, e.g. on 2019-12-02.");
        }

        return parseDate(input.substring(3).trim());
    }

    private LocalDateTime parseDateTime(String input)
            throws MatthewException {
        try {
            return LocalDateTime.parse(input, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "Date/time must use yyyy-MM-dd HHmm, "
                            + "e.g. 2019-12-02 1800.");
        }
    }

    private LocalDate parseDate(String input)
            throws MatthewException {
        try {
            return LocalDate.parse(input, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "Date must use yyyy-MM-dd, e.g. 2019-12-02.");
        }
    }
}
