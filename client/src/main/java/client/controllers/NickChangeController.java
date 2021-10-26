package client.controllers;

import client.NetworkClient;
import client.models.Network;
import javafx.fxml.FXML;

import javafx.scene.control.*;



public class NickChangeController {

    private Network network;
    private NetworkClient networkClient;

    @FXML
   private TextField loginField;
 @FXML
 private TextField passField;
 @FXML
 private TextField oldNickField;
 @FXML
 private TextField newNickField;

 @FXML
 public void changeNick (){
     String login = loginField.getText();
     String password = passField.getText();
     String oldNick = oldNickField.getText();
     String newNick = newNickField.getText();

     if (login.isEmpty()||password.isEmpty()||oldNick.isEmpty()||newNick.isEmpty()){
         NetworkClient.showErrorMessage("Поля не должны быть пустыми", "Ошибка ввода");
         return;
     }
     String nickChangeInfo = network.sendChangeNickCommand(oldNick,login,password, newNick);
     if (network.isNickIsSuccessfullyChanged()) NetworkClient.showOKMessage(nickChangeInfo);
     if (!(network.isNickIsSuccessfullyChanged())) NetworkClient.showErrorRegMessage(nickChangeInfo);
     networkClient.closeNickChange();
 }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
}
