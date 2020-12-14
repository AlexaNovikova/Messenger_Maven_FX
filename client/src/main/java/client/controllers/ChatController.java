package client.controllers;


import client.NetworkClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import client.models.*;
import javafx.scene.input.MouseEvent;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatController {

    @FXML
    public ListView<String> usersList;

    @FXML
    private Button sendButton;
    @FXML
    private TextArea chatHistory;
    @FXML
    private TextField textField;


    private Network network;
    private String selectedRecipient;



    @FXML
    public void initialize() {
        usersList.setItems(FXCollections.observableArrayList());
        sendButton.setOnAction(event -> ChatController.this.sendMessage());
        textField.setOnAction(event -> ChatController.this.sendMessage());

        usersList.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                usersList.requestFocus();
                if (! cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell ;
        });

    }

    private void sendMessage() {
        String message = textField.getText();
        textField.clear();

        if(message.isEmpty()) {
            return;
        }
//        appendMessage(network.getUsername() + ": " + message);
        appendMessage("Я: " + message);
        textField.clear();

        try {
            if (selectedRecipient != null) {
                network.sendPrivateMessage(message, selectedRecipient);
            }
            else {
                network.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Ошибка при отправке сообщения";
            NetworkClient.showErrorMessage(e.getMessage(), errorMessage);
        }

    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void appendMessage(String message) {
        String timestamp = DateFormat.getInstance().format(new Date());
        chatHistory.appendText(timestamp);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(message);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());
    }

    public void showError(String title, String message) {
        NetworkClient.showErrorMessage(message, title);
    }



    public void updateUsers(List<String> users) {
        usersList.setItems(FXCollections.observableArrayList(users));
    }

    public void showChatHistory(){
        try{  String fileName = "client/src/main/resources/history/history_"+network.getLogin()+".txt";
        //   Path historyFile = Paths.get("client/src/main/resources/1.txt");
        if (!Files.exists(Paths.get(fileName))) {
              Path path = Files.createFile(Paths.get(fileName));

        }
        List<String> history = Files.readAllLines(Paths.get(fileName));
        int min;
         min=(history.size()>100)?(history.size()-100):0;
         for (int i=min; i<history.size(); i++){
             chatHistory.appendText(history.get(i));
             chatHistory.appendText(System.lineSeparator());
         }

//            List<String> history = new ArrayList<>();
//            history.addAll(Files.readAllLines(Paths.get(fileName)));
//            history.
//            history.subList(history.size()-100, history.size());
//
//            for (String s :
//                    history) {
//             chatHistory.setText(s);
//            }
//         chatHistory.setText(Files.readAllLines(Paths.get(fileName)).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHistory()  {
        String fileName = "client/src/main/resources/history/history_"+network.getLogin()+".txt";
        int min;
//        if (chatHistory.getLength()>100) {min = chatHistory.getLength()-100;}
//        else min=1;
        String history = chatHistory.getText();
        try {
            Files.write(Paths.get(fileName), Collections.singleton(history));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        byte[] bytes = history.getBytes();
//        try {
//            Files.write(Paths.get(fileName),bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}