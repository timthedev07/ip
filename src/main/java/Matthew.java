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
        int i = 0;

        String response = "";

        while (!response.equals("bye")) {
            response = System.console().readLine();
            if (response.equals("list")) {
                int counter = 1;
                System.out.print(line);
                for (String s : tasks) {
                    if (s == null) {
                        break;
                    }
                    System.out.println(String.format("%d. %s", counter++, s));
                }
                System.out.print(line);
            } else {
                tasks[i++] = response;
                System.out.println(wrap(String.format("added: %s", response)));
            }
        }

        System.out.println(wrap("Goodbye! Have a nice day!"));
    }

    public static String wrap(String t) {
        return line + t + "\n" + line;
    }
}