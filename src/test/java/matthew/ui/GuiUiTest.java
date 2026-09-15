package matthew.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/** Tests buffered GUI responses without requiring a JavaFX display. */
class GuiUiTest {
    @Test
    void guiBuffersPersonalityAndEmptySortedResults() {
        GuiUi ui = new GuiUi();

        ui.showWelcome();
        ui.showSortedTasks(new ArrayList<>());

        String output = ui.consumeOutput();
        assertTrue(output.contains("Hello! I'm Matthew."));
        assertTrue(output.contains("neatly numbered boxes"));
        assertTrue(output.contains("Matthew's tiny filing cabinet"));
        assertTrue(output.contains("No tasks found."));
    }
}
