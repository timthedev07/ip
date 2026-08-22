import java.util.ArrayList;

public class Ui {
    private static final String LINE =
            "____________________________________________________________\n";

    public void showWelcome() {
        String banner = LINE
                + " __  __       _   _   _                   \n"
                + "|  \\/  | __ _| |_| |_| |__   _____      __\n"
                + "| |\\/| |/ _` | __| __| '_ \\ / _ \\ \\ /\\ / /\n"
                + "| |  | | (_| | |_| |_| | | |  __/\\ V  V / \n"
                + "|_|  |_|\\__,_|\\__|\\__|_| |_|\\___| \\_/\\_/  \n"
                + "Hello! I'm Matthew.\n"
                + "What can I do for you?\n"
                + LINE;

        System.out.println(banner);
    }

    public String readCommand() {
        return System.console().readLine();
    }

    public void showGoodbye() {
        System.out.println(wrap("Goodbye! Have a nice day!"));
    }

    public void showError(String message) {
        System.out.println(wrap("OOPS!!! " + message));
    }

    public void showLoadingError() {
        System.out.println(
                wrap("OOPS!!! I couldn't load the saved tasks."));
    }

    public void showTaskList(ArrayList<Task> tasks) {
        System.out.print(LINE);
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }

        System.out.print(LINE);
    }

    public void showAddedTask(Task task, int taskCount) {
        System.out.print(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list.");
        System.out.print(LINE);
    }

    public void showMarkedTask(Task task) {
        System.out.print(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.print(LINE);
    }

    public void showUnmarkedTask(Task task) {
        System.out.print(LINE);
        System.out.println(
                "OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.print(LINE);
    }

    public void showDeletedTask(Task task, int taskCount) {
        System.out.print(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list.");
        System.out.print(LINE);
    }

    public void showTasksOn(String date, ArrayList<Task> tasks) {
        System.out.print(LINE);
        System.out.println("Tasks occurring on " + date + ":");

        if (tasks.isEmpty()) {
            System.out.println("No deadlines or events found.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }

        System.out.print(LINE);
    }

    private String wrap(String text) {
        return LINE + text + "\n" + LINE;
    }
}
