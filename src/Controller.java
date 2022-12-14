import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private ComboBox<String> guessComboBox;

    @FXML
    private Label guessLabel;

    private String answer = "polymorphism";
    private String shown;
    private int mistakes;
    private GraphicsContext gc;
    private static final int PIECES_COUNT = 6;

    @FXML
    private void initialize() {
        // generate a new word
        answer = WordGenerator.getRandomWord();

        // add all the characters of the alphabet to the Combo Box
        guessComboBox.getItems().clear();
        guessComboBox.getItems().add("Select a letter...");
        for (int i = 'a'; i <= 'z'; i++)
            guessComboBox.getItems().add("" + (char)i);
        guessComboBox.getSelectionModel().select("Select a letter...");
        guessComboBox.setDisable(false);

        // initialize the shown string
        shown = "_".repeat(answer.length());
        guessLabel.setText(shown);

        // initialize mistakes count to 0
        mistakes = 0;

        gc = canvas.getGraphicsContext2D();

        //clear the canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        //draw the post that the hangman will be hung on
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.strokeLine(20,canvas.getHeight()-20, canvas.getWidth()-20, canvas.getHeight()-20);
        gc.strokeLine(40, canvas.getHeight()-20, 40,20);
        gc.strokeLine(40, 20, canvas.getWidth()/2.0,20);
        gc.strokeLine(canvas.getWidth()/2.0,20,canvas.getWidth()/2.0,40);
    }

    @FXML
    void charGuessed() {
        if (guessComboBox.isDisable())
            return;

        //remove the selected character
        String selected = guessComboBox.getSelectionModel().getSelectedItem();
        guessComboBox.setDisable(true);
        guessComboBox.getItems().remove(selected);
        guessComboBox.getSelectionModel().select("Select a letter...");
        guessComboBox.setDisable(false);

        // update the shown label
        updateLabel(selected.charAt(0));
    }

    private void updateLabel(char c) {
        boolean isCorrect = false;
        for (int i = 0; i < answer.length(); i++)
            if (answer.charAt(i) == c) {
                shown = shown.substring(0, i) + c + shown.substring(i + 1);
                isCorrect = true;
            }

        if (!isCorrect){
            drawNextPiece();
            mistakes++;
        }
        else guessLabel.setText(shown);

        // end the game if there are no letters left to guess (win)
        if (!shown.contains("_"))
            promptWin();

        if (mistakes == PIECES_COUNT)
            promptLoss();
    }

    private void drawNextPiece() {
        switch (mistakes) {
            case 0 -> gc.strokeOval(canvas.getWidth()/2.0 - 20,40,40,40); // draw circle face
            case 1 -> gc.strokeLine(canvas.getWidth()/2.0, 80, canvas.getWidth()/2.0, 150);
            case 2 -> gc.strokeLine(canvas.getWidth()/2.0, 80, canvas.getWidth()/2.0 + 40, 100);
            case 3 -> gc.strokeLine(canvas.getWidth()/2.0, 80, canvas.getWidth()/2.0 - 40, 100);
            case 4 -> gc.strokeLine(canvas.getWidth()/2.0, 150, canvas.getWidth()/2.0 + 40, 170);
            case 5 -> gc.strokeLine(canvas.getWidth()/2.0, 150, canvas.getWidth()/2.0 - 40, 170);
        }
    }

    @FXML
    void restartPressed(){
        guessComboBox.setDisable(true);
        initialize();
    }

    private void promptWin() {
        promptEnd("You win ! As you guessed, the word was \"" + answer + "\".\n" +
                "You succeeded with " + mistakes + " wrong guess" + (mistakes == 1 ? "." : "es."));
    }

    private void promptLoss() {
        promptEnd("You lose ! The word was \"" + answer + "\".");
    }

    private void promptEnd(String prompt) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Game Over");
        a.setHeaderText(prompt);
        a.show();
        guessComboBox.setDisable(true);
    }
}
