/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author mhaqu
 */
public class FXMLController implements Initializable {

    private final int server = 1000;
    private InetAddress IPAddress;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    @FXML
    private TextField inputTextfield;
    @FXML
    private Button submitButton;
    @FXML
    private ListView<String> outputListview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            IPAddress = InetAddress.getByName("localhost");
            socket = new Socket(IPAddress, server);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            Thread messageRecieve = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String message = input.readUTF();
                            Platform.runLater(() -> {
                                //populate list view with incoming messages
                                ObservableList<String> ol = outputListview.getItems();
                                ol.add("Friend: " + message);
                                outputListview.setItems(ol);
                                //scroll to bottom as new messages are coming in
                                outputListview.scrollTo(ol.size());
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            messageRecieve.start();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onSubmit(ActionEvent event) throws IOException {
        sendMessage();
    }

    @FXML
    private void onKeyPressed(KeyEvent event) throws IOException {
        //send message if user presses enter
        if (event.getCode().equals(KeyCode.ENTER)) {
            sendMessage();
        }
    }

    private void sendMessage() throws IOException {
        String inputText = inputTextfield.getText();
        if (inputText != null && !inputText.isEmpty() && !inputText.isBlank()) {
            output.writeUTF(inputText);
            //populate listview with message user sent
            ObservableList<String> ol = outputListview.getItems();
            ol.add("You: " + inputText);
            outputListview.setItems(ol);
            //scroll to bottom as new messages are coming in
            outputListview.scrollTo(ol.size());
            inputTextfield.setText(null);
        }
    }
}
