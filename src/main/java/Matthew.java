public class Matthew {
    private static final String line = "____________________________________________________________\n";

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

        String[] tasks = new String[100];
        boolean[] done = new boolean[100];
        int i = 0;

        String response = "";

        while (!response.equals("bye")) {
            response = System.console().readLine();

            if (response.equals("bye")) {
                break;
            }

            if (response.equals("list")) {
                System.out.print(line);
                System.out.println("Here are the tasks in your list:");

                for (int j = 0; j < i; j++) {
                    String status = done[j] ? "X" : " ";
                    System.out.printf("%d.[%s] %s%n", j + 1, status, tasks[j]);
                }

                System.out.print(line);

            } else if (response.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(response.substring(5));
                int index = taskNumber - 1;

                done[index] = true;

                System.out.print(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.printf("  [X] %s%n", tasks[index]);
                System.out.print(line);

            } else if (response.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(response.substring(7));
                int index = taskNumber - 1;

                done[index] = false;

                System.out.print(line);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.printf("  [ ] %s%n", tasks[index]);
                System.out.print(line);

            } else {
                tasks[i] = response;
                done[i] = false;
                i++;

                System.out.println(wrap(String.format("added: %s", response)));
            }
        }

        System.out.println(wrap("Goodbye! Have a nice day!"));
    }

    public static String wrap(String t) {
        return line + t + "\n" + line;
    }
}