package matthew.ui;

import java.util.ArrayList;
import java.util.Scanner;

import matthew.task.Task;

public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" __  __       _   _   _                   ");
        System.out.println("|  \\/  | __ _| |_| |_| |__   _____      __");
        System.out.println("| |\\/| |/ _` | __| __| '_ \\ / _ \\ \\ /\\ / /");
        System.out.println("| |  | | (_| | |_| |_| | | |  __/\\ V  V / ");
        System.out.println("|_|  |_|\\__,_|\\__|\\__|_| |_|\\___| \\_/\\_/  ");
        System.out.println("Hello! I'm Matthew.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showGoodbye() {
        System.out.println("Goodbye! Have a nice day!");
    }

    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    public void showLoadingError() {
        System.out.println(
                "OOPS!!! I couldn't load the saved tasks.");
    }

    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list.");
    }

    public void showMarkedTask(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showUnmarkedTask(Task task) {
        System.out.println(
                "OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public void showDeletedTask(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list.");
    }

    public void showTasksOn(String date, ArrayList<Task> tasks) {
        System.out.println("Tasks occurring on " + date + ":");

        if (tasks.isEmpty()) {
            System.out.println("No deadlines or events found.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
    }
}
