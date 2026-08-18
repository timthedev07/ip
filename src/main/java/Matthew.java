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


        String response = "";

        while (!response.equals("bye")) {
            response = System.console().readLine();
            System.out.println(wrap(response));
        }

        System.out.println(wrap("Goodbye! Have a nice day!"));
    }
    public static String wrap(String t) {
        return line + t + "\n" + line;
    }

}
