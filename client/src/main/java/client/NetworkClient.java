package client;
import client.controllers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import client.models.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NetworkClient extends Application {

    public Stage primaryStage;
    private Stage authStage;
    private Stage registerStage;
    private Stage nickChangeStage;
    private Network network;
    private ChatController chatController;
    private MessageService messageService;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        network = new Network();
        if (!network.connect()) {
            showErrorMessage("", "Ошибка подключения к серверу");
            return;
        }
        openAuthDialog(primaryStage);
        createChatDialog(primaryStage);
    }

    private void createChatDialog(Stage primaryStage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(NetworkClient.class.getResource("/views/chat-view.fxml"));

        Parent root = mainLoader.load();

        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 600, 400));

        chatController = mainLoader.getController();
        chatController.setNetwork(network);

        primaryStage.setOnCloseRequest(event -> {History.closeWriter(); network.close();});
    }

    public void openRegistrationDialog() throws IOException {

        FXMLLoader regLoader = new FXMLLoader();
        regLoader.setLocation(NetworkClient.class.getResource("/views/register-dialog.fxml"));
        Parent page = regLoader.load();
        registerStage = new Stage();
        registerStage.setTitle("Регистрация");
        registerStage.initModality(Modality.WINDOW_MODAL);
        registerStage.initOwner(authStage);
        Scene scene = new Scene(page);
        registerStage.setScene(scene);
        registerStage.show();
        RegisterDialogController registerDialogController = regLoader.getController();
        registerDialogController.setNetwork(network);
        registerDialogController.setNetworkClient(this);

    }

    private void openAuthDialog(Stage primaryStage) throws IOException {

        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(NetworkClient.class.getResource("/views/auth-dialog.fxml"));
        Parent page = authLoader.load();
        authStage = new Stage();
        authStage.setTitle("Авторизация");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        authStage.setScene(scene);
        authStage.show();
        AuthDialogController authDialogController = authLoader.getController();
        authDialogController.setNetwork(network);
        authDialogController.setNetworkClient(this);
    }

    public static void showErrorMessage(String message, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Проблемы с соединением");
        alert.setHeaderText(errorMessage);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showOKMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Успех!");
        alert.setHeaderText("Регистрация/изменения прошли успешно!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showErrorRegMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!");
        alert.setHeaderText("Регистрацию/изменения не удалось выполнить!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void openChat() {
        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());
        System.out.println(network.getUsername());
        History.showChatHistory(network.login,chatController.getChatHistory());
        History.startWriter(network.login);
        network.waitMessage(chatController);

    }

    public void closeReg() {
        registerStage.close();
    }


    public void openNickChangeDialog() throws IOException {
        FXMLLoader nickLoader = new FXMLLoader();
        nickLoader.setLocation(NetworkClient.class.getResource("/views/changeNick.fxml"));
        Parent page = nickLoader.load();
         nickChangeStage = new Stage();
        nickChangeStage.setTitle("Изменения ника.");
       nickChangeStage.initModality(Modality.WINDOW_MODAL);
      nickChangeStage.initOwner(authStage);
        Scene scene = new Scene(page);
        nickChangeStage.setScene(scene);
       nickChangeStage.show();
       NickChangeController nickChangeController = nickLoader.getController();
        nickChangeController.setNetwork(network);
       nickChangeController.setNetworkClient(this);
    }

    public void closeNickChange() {
        nickChangeStage.close();
    }
}
