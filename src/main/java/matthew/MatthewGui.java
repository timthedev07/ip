package matthew;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import matthew.command.Command;
import matthew.exception.MatthewException;
import matthew.parser.Parser;
import matthew.storage.Storage;
import matthew.task.TaskList;
import matthew.ui.GuiUi;

/** Displays Matthew's task management chatbot in a JavaFX window. */
public class MatthewGui extends Application {
    private static final String STORAGE_PATH = "data/matthew.txt";
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_MIN_WIDTH = 560;
    private static final int WINDOW_MIN_HEIGHT = 400;

    private final Storage storage = new Storage(STORAGE_PATH);
    private final GuiUi ui = new GuiUi();
    private TaskList tasks;
    private TextArea transcript;
    private TextField commandInput;
    private Stage stage;
    private Label statusLabel;

    /** Creates the JavaFX application. */
    public MatthewGui() {
    }

    /** Builds and displays Matthew's user interface. */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        tasks = loadTasks();
        transcript = createTranscript();
        commandInput = new TextField();
        commandInput.setPromptText("Type a command, e.g. list or todo read book");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("primary-button");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> processCommand());
        commandInput.setOnAction(event -> processCommand());

        HBox commandBar = new HBox(8, commandInput, sendButton);
        commandBar.getStyleClass().add("command-bar");
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        Label heading = new Label("Matthew");
        heading.getStyleClass().add("app-title");
        Label subtitle = new Label("Your friendly task management chatbot");
        subtitle.getStyleClass().add("app-subtitle");
        Label status = new Label();
        status.getStyleClass().add("status-label");
        statusLabel = status;

        HBox quickActions = createQuickActions();
        VBox header = new VBox(4, heading, subtitle, status,
                new Separator(), quickActions);
        header.getStyleClass().add("app-header");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setTop(header);
        root.setCenter(transcript);
        root.setBottom(commandBar);
        BorderPane.setMargin(transcript, new Insets(16, 0, 16, 0));

        primaryStage.setTitle("Matthew");
        primaryStage.setMinWidth(WINDOW_MIN_WIDTH);
        primaryStage.setMinHeight(WINDOW_MIN_HEIGHT);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(
                getClass().getResource("/matthew.css").toExternalForm());
        primaryStage.setScene(scene);
        ui.showWelcome();
        primaryStage.show();
        appendOutput();
        updateStatus();
        commandInput.requestFocus();
    }

    /** Creates buttons for the most common read-only commands. */
    private HBox createQuickActions() {
        Button listButton = new Button("List tasks");
        Button sortButton = new Button("Sort tasks");
        Button todayButton = new Button("Today's tasks");
        Button clearButton = new Button("Clear chat");

        listButton.setOnAction(event -> sendCommand("list"));
        sortButton.setOnAction(event -> sendCommand("sort"));
        todayButton.setOnAction(event -> sendCommand(
                "on " + java.time.LocalDate.now()));
        clearButton.setOnAction(event -> clearTranscript());

        listButton.getStyleClass().add("quick-action");
        sortButton.getStyleClass().add("quick-action");
        todayButton.getStyleClass().add("quick-action");
        clearButton.getStyleClass().add("quick-action");

        return new HBox(8, listButton, sortButton, todayButton, clearButton);
    }

    /** Sends a command from a quick-action button. */
    private void sendCommand(String command) {
        commandInput.setText(command);
        processCommand();
    }

    /** Clears the transcript and restores the welcome message. */
    private void clearTranscript() {
        transcript.clear();
        ui.showWelcome();
        appendOutput();
        updateStatus();
    }

    /** Loads saved tasks, retaining the application with an empty list on failure. */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (MatthewException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Creates the read-only command transcript. */
    private TextArea createTranscript() {
        TextArea output = new TextArea();
        output.getStyleClass().add("transcript");
        output.setEditable(false);
        output.setWrapText(true);
        output.setFocusTraversable(false);
        return output;
    }

    /** Parses and executes the command currently entered by the user. */
    private void processCommand() {
        String input = commandInput.getText().trim();

        if (input.isEmpty()) {
            ui.showError("Please enter a command, or use one of the quick actions above.");
            appendOutput();
            return;
        }

        transcript.appendText("> " + input + "\n");
        commandInput.clear();

        try {
            Command command = Parser.parse(input);
            command.execute(tasks, ui, storage);
            appendOutput();
            updateStatus();

            if (command.isExit()) {
                stage.close();
            }
        } catch (MatthewException e) {
            ui.showError(e.getMessage());
            appendOutput();
            updateStatus();
        }
    }

    /** Updates the task-count indicator shown above the transcript. */
    private void updateStatus() {
        if (statusLabel != null && tasks != null) {
            statusLabel.setText(tasks.size() + " task"
                    + (tasks.size() == 1 ? "" : "s") + " saved");
        }
    }

    /** Appends buffered chatbot responses to the transcript. */
    private void appendOutput() {
        String output = ui.consumeOutput();

        if (!output.isEmpty()) {
            transcript.appendText(output);
            transcript.positionCaret(transcript.getLength());
        }
    }

}
