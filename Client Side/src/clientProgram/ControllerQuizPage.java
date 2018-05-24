//นายจุตินภัส คลังเจริญกุล 5810400973

package clientProgram;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ControllerQuizPage {

    @FXML
    private Button trueButton;
    @FXML
    private Button falseButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button restartButton;
    @FXML
    private Label quizLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label nameLabel;

    static ClientProgram clientProgram;
    private Model model = new Model();

    @FXML
    public void initialize() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nameLabel.setText(clientProgram.getName());
            }
        });
        nextQuiz();
    }

    public void setClientProgram(ClientProgram clientProgram) {
        this.clientProgram = clientProgram;
    }

    @FXML
    public void handleTrueButton() throws IOException {
        if (model.isCurrentAnswer()) {
            model.increaseScore();
        } nextQuiz();
    }

    @FXML
    public void handleFalseButton() throws IOException {
        if (!model.isCurrentAnswer()) {
            model.increaseScore();
        } nextQuiz();
    }

    @FXML
    public void handleRestartButton() throws IOException {
        clientProgram.sendToServer("RESTART " + clientProgram.getRoomName());
    }

    @FXML
    public void handleQuitButton() throws IOException {
        clientProgram.sendToServer("QUIT " + clientProgram.getRoomName() + " " + clientProgram.getName());
        clientProgram.stop();
        Platform.exit();
    }

    @FXML
    public void restartGame() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                model.reset();
                restartButton.setDisable(true);
                quitButton.setDisable(true);
                trueButton.setDisable(false);
                falseButton.setDisable(false);
                statusLabel.setText("");
                try {
                    nextQuiz();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void setStatus(String name) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText(name + " has left.");
            }
        });
    }

    @FXML
    public void setWinnerLabel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                quizLabel.setText("FINISH!!");
                restartButton.setDisable(false);
                quitButton.setDisable(false);
            }
        });
    }

    @FXML
    public void setWinnerLabel(String name, String score) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (clientProgram.getName().equals(name)) {
                    quizLabel.setText("YOU'RE WINNER!!");
                } else {
                    quizLabel.setText("The winner is.. " + name + "!!\n" + "Score: " + score);
                }
                restartButton.setDisable(false);
                quitButton.setDisable(false);
            }
        });
    }

    private void nextQuiz() throws IOException {
        if (hasMoreQuiz()) {
            model.nextQuiz();
            quizLabel.setText(model.getCurrentQuestion());
        } else {
            quizLabel.setText("Finish! please wait for the results.");
            trueButton.setDisable(true);
            falseButton.setDisable(true);
            clientProgram.setController(this);
            clientProgram.sendToServer("FINISH " + clientProgram.getRoomName() + " " + clientProgram.getName() + " " + model.getScore());
            System.out.println("FINISH " + clientProgram.getRoomName() + " " + clientProgram.getName() + " " + model.getScore() + " >> SERVER");
        } scoreLabel.setText("Score: " + model.getScore() );
    }


    private boolean hasMoreQuiz() {
        return model.hasMoreQuiz();
    }

}
