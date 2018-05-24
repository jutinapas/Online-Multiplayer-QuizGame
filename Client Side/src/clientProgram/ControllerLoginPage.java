//นายจุตินภัส คลังเจริญกุล 5810400973

package clientProgram;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerLoginPage {

    @FXML
    private Button joinButton;
    @FXML
    private Button createButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField roomNameField;
    @FXML
    private TextField numField;
    @FXML
    private Label statusLabel;

    static ClientProgram clientProgram;

    @FXML
    public void handleJoinButton() throws IOException {
        statusLabel.setText("");
        numField.setText("");
        if (clientProgram == null && !nameField.getText().isEmpty() && !roomNameField.getText().isEmpty()) {
            startProgram();
            sendConnection();
        } else if (clientProgram != null && !nameField.getText().isEmpty() && !roomNameField.getText().isEmpty()) {
            clientProgram.setName(nameField.getText());
            clientProgram.setRoomName(roomNameField.getText());
            sendConnection();
        } else{
            setRequired();
        }
    }

    @FXML
    public void handleCreateButton() throws IOException {
        statusLabel.setText("");
        if (clientProgram == null && !nameField.getText().isEmpty() && !roomNameField.getText().isEmpty() && !numField.getText().isEmpty()) {
            startProgram();
            sendConnection();
        } else if (clientProgram != null && !nameField.getText().isEmpty() && !roomNameField.getText().isEmpty() && !numField.getText().isEmpty()) {
            clientProgram.setName(nameField.getText());
            clientProgram.setRoomName(roomNameField.getText());
            sendConnection();
        }  else {
            if (!numField.getText().isEmpty()) {
                setRequired();
            } else {
                setRequired();
                if (statusLabel.getText().isEmpty()){
                    statusLabel.setText("Required number of players");
                } else {
                    statusLabel.setText(statusLabel.getText() + "\nand number of players");
                }
            }
        }
    }

    @FXML
    private void setRequired() {
        if (nameField.getText().isEmpty()) {
            if (roomNameField.getText().isEmpty()) {
                statusLabel.setText("*Required nickname, room name");
            } else {
                statusLabel.setText("*Required nickname");
            }
        } else if (roomNameField.getText().isEmpty()) {
            statusLabel.setText("*Requlired room name");
        }
    }


    private void startProgram() throws IOException {
        String name = nameField.getText();
        String roomName = roomNameField.getText();
        clientProgram = new ClientProgram(name, roomName);
        clientProgram.start();
        clientProgram.setController(this);
    }

    private void sendConnection() throws IOException {
        String roomName = roomNameField.getText();
        if (numField.getText().equals("")) {
            clientProgram.sendToServer("CONNECT " + roomName);
            System.out.println("CONNECT " + roomName);
        } else {
            String num = numField.getText();
            clientProgram.sendToServer("CONNECT " + roomName + " " + num);
            System.out.println("CONNECT " + roomName + " " + num);
        }
    }

    @FXML
    private void setDisable() {
        nameField.setDisable(true);
        roomNameField.setDisable(true);
        numField.setDisable(true);
        joinButton.setDisable(true);
        createButton.setDisable(true);
    }

    @FXML
    public void setStatus(String status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("");
                if (status.equals("100CREATE") || status.equals("101JOINED")) {
                    setDisable();
                    statusLabel.setText("Waiting for players.. ");
                } else if (status.equals("401NOJOIN")) {
                    statusLabel.setText("This room has not created");
                } else if (status.equals("403CREATED")) {
                    numField.setText("");
                    statusLabel.setText("This room has created");
                } else if (status.equals("402FULL")) {
                    statusLabel.setText("This room has full");
                }
            }
        });
    }

    @FXML
    public void loadQuizPage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage) joinButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("QuizPage.fxml"));
                try {
                    stage.setScene(new Scene(loader.load(), 800, 600));
                    ControllerQuizPage controllerQuizPage = loader.getController();
                    controllerQuizPage.setClientProgram(clientProgram);
                    stage.setResizable(false);
                    stage.setOnCloseRequest(event -> {
                        try {
                            controllerQuizPage.handleQuitButton();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
