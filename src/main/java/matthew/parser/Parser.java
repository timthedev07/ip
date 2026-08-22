package matthew.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import matthew.command.AddCommand;
import matthew.command.Command;
import matthew.command.DeleteCommand;
import matthew.command.ExitCommand;
import matthew.command.ListCommand;
import matthew.command.MarkCommand;
import matthew.command.OnCommand;
import matthew.command.UnmarkCommand;
import matthew.exception.MatthewException;
import matthew.task.Deadline;
import matthew.task.Event;
import matthew.task.Todo;

public class Parser {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Command parse(String input)
            throws MatthewException {

        String trimmed = input.trim();

        if (trimmed.isEmpty()) {
            throw new MatthewException(
                    "I don't recognise an empty command.");
        }

        String commandWord = trimmed.split("\\s+")[0];

        switch (commandWord) {
        case "bye":
            return parseExit(trimmed);

        case "list":
            return parseList(trimmed);

        case "mark":
            return new MarkCommand(
                    parseTaskNumber(trimmed, "mark"));

        case "unmark":
            return new UnmarkCommand(
                    parseTaskNumber(trimmed, "unmark"));

        case "delete":
            return new DeleteCommand(
                    parseTaskNumber(trimmed, "delete"));

        case "todo":
            return new AddCommand(
                    new Todo(parseTodoDescription(trimmed)));

        case "deadline":
            return new AddCommand(
                    parseDeadline(trimmed));

        case "event":
            return new AddCommand(
                    parseEvent(trimmed));

        case "on":
            return new OnCommand(
                    parseOnDate(trimmed));

        default:
            throw new MatthewException(
                    "I don't recognise that command.");
        }
    }

    private static Command parseExit(String input)
            throws MatthewException {
        if (!input.equals("bye")) {
            throw new MatthewException(
                    "The 'bye' command does not take extra arguments.");
        }
        return new ExitCommand();
    }

    private static Command parseList(String input)
            throws MatthewException {
        if (!input.equals("list")) {
            throw new MatthewException(
                    "The 'list' command does not take extra arguments.");
        }
        return new ListCommand();
    }

    private static int parseTaskNumber(
            String input, String command)
            throws MatthewException {

        String prefix = command + " ";

        if (!input.startsWith(prefix)) {
            throw new MatthewException(
                    "Please tell me which task to " + command
                            + ", e.g. " + command + " 2.");
        }

        String numberText = input.substring(prefix.length()).trim();

        if (numberText.isEmpty()) {
            throw new MatthewException(
                    "Please tell me which task to " + command
                            + ", e.g. " + command + " 2.");
        }

        try {
            return Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new MatthewException(
                    "The task number must be a number.");
        }
    }

    private static String parseTodoDescription(String input)
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

    private static Deadline parseDeadline(String input)
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

        return new Deadline(
                description, parseDateTime(dateTimeText));
    }

    private static Event parseEvent(String input)
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

    private static LocalDate parseOnDate(String input)
            throws MatthewException {

        if (!input.startsWith("on ")) {
            throw new MatthewException(
                    "Please provide a date, e.g. on 2019-12-02.");
        }

        String dateText = input.substring(3).trim();

        try {
            return LocalDate.parse(dateText, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "Date must use yyyy-MM-dd, e.g. 2019-12-02.");
        }
    }

    private static LocalDateTime parseDateTime(String input)
            throws MatthewException {
        try {
            return LocalDateTime.parse(
                    input, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MatthewException(
                    "Date/time must use yyyy-MM-dd HHmm, "
                            + "e.g. 2019-12-02 1800.");
        }
    }
}
