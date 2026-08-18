public class Matthew {
    private static final String line =
            "____________________________________________________________\n";

    public static void main(String[] args) {

        String banner = line
                + " __  __       _   _   _                   \n"
                + "|  \\/  | __ _| |_| |_| |__   _____      __\n"
                + "| |\\/| |/ _` | __| __| '_ \\ / _ \\ \\ /\\ / /\n"
                + "| |  | | (_| | |_| |_| | | |  __/\\ V  V / \n"
                + "|_|  |_|\\__,_|\\__|\\__|_| |_|\\___| \\_/\\_/  \n"
                + "Hello! I'm Matthew.\n"
                + "What can I do for you?\n"
                + line;

        System.out.println(banner);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String response = System.console().readLine();

            if (response.equals("bye")) {
                break;

            } else if (response.equals("list")) {
                System.out.print(line);
                System.out.println("Here are the tasks in your list:");

                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }

                System.out.print(line);

            } else if (response.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(response.substring(5));
                Task task = tasks[taskNumber - 1];

                task.markAsDone();

                System.out.print(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
                System.out.print(line);

            } else if (response.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(response.substring(7));
                Task task = tasks[taskNumber - 1];

                task.markAsNotDone();

                System.out.print(line);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
                System.out.print(line);

            } else if (response.startsWith("todo ")) {
                String description = response.substring(5);

                Task task = new Todo(description);
                tasks[taskCount++] = task;

                printAddedTask(task, taskCount);

            } else if (response.startsWith("deadline ")) {
                String input = response.substring(9);

                int byIndex = input.indexOf(" /by ");

                String description = input.substring(0, byIndex);
                String by = input.substring(byIndex + 5);

                Task task = new Deadline(description, by);
                tasks[taskCount++] = task;

                printAddedTask(task, taskCount);

            } else if (response.startsWith("event ")) {
                String input = response.substring(6);

                int fromIndex = input.indexOf(" /from ");
                int toIndex = input.indexOf(" /to ");

                String description = input.substring(0, fromIndex);
                String from = input.substring(fromIndex + 7, toIndex);
                String to = input.substring(toIndex + 5);

                Task task = new Event(description, from, to);
                tasks[taskCount++] = task;

                printAddedTask(task, taskCount);
            }
        }

        System.out.println(wrap("Goodbye! Have a nice day!"));
    }

    public static void printAddedTask(Task task, int taskCount) {
        System.out.print(line);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.print(line);
    }

    public static String wrap(String text) {
        return line + text + "\n" + line;
    }
}