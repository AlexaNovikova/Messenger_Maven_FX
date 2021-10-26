package client.controllers;

import ClientServer.Command;
import ClientServer.commands.RegOkCommandData;
import client.NetworkClient;
import client.models.Network;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterDialogController {

    private Network network;
    private NetworkClient networkClient;


    @FXML public TextField loginReg;
    @FXML public TextField passwordReg;
    @FXML public TextField nickReg;


    @FXML
    private void register(){
        String login = loginReg.getText();
        String password = passwordReg.getText();
        String nick = nickReg.getText();

        if (login.isEmpty()||password.isEmpty()||nick.isEmpty()){
            NetworkClient.showErrorMessage("Поля не должны быть пустыми", "Ошибка ввода");
            return;
        }
          String regInfo = network.sendRegisterCommandAndGetAnswerFromServer(nick,login,password);
          if (network.isServerConfirmRegistration()) NetworkClient.showOKMessage(regInfo);
          if (!(network.isServerConfirmRegistration())) NetworkClient.showErrorRegMessage(regInfo);
          networkClient.closeReg();
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
}
