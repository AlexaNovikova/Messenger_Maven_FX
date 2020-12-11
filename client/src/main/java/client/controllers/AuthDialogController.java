package client.controllers;
import client.NetworkClient;
import client.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Timer;

public class AuthDialogController {

    @FXML public TextField loginField;
    @FXML public PasswordField passField;

    private Network network;
    private NetworkClient networkClient;

    @FXML
    public void checkAuth() {

            String login = loginField.getText();
            String password = passField.getText();
            if (login.isEmpty()|| password.isEmpty()) {
                NetworkClient.showErrorMessage("Поля не должны быть пустыми", "Ошибка ввода");
                return;
            }

            String authErrorMessage = network.sendAuthCommand(login, password);
            if (authErrorMessage == null) {
                networkClient.openChat();
            } else {
                NetworkClient.showErrorMessage(authErrorMessage, "Ошибка авторизации");

            }


    }
    @FXML
    private void registerOpen(){
        try {
            networkClient.openRegistrationDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openChangeNickView(){
        try {
            networkClient.openNickChangeDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
}
