package client.models;

import ClientServer.Command;
import ClientServer.commands.*;
import client.MessageService;
import client.NetworkClient;
import client.controllers.ChatController;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Network {

    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8189;
    private final int port;
    private final String host;
    private final MessageService messageService;

    private ObjectOutputStream dataOutputStream;
    private ObjectInputStream dataInputStream;

    public boolean RegOK = false;
    public boolean ChangeOk = false;

    private Socket socket;

    private String username;
    public String login;

    public Network() {
        this(SERVER_ADRESS, SERVER_PORT);
    }

    private Network(String host, int port) {
        this.host = host;
        this.port = port;
        messageService = new MessageService();
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            dataOutputStream = new ObjectOutputStream(socket.getOutputStream());
            dataInputStream = new ObjectInputStream(socket.getInputStream());
            return true;

        } catch (IOException e) {
            System.out.println("Соединение не было установлено!");
            e.printStackTrace();
            return false;
        }

    }

    public void close() {
        try {
            sendEndConnectionCommand();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessage(ChatController chatController) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Command command = readCommand();
                    messageService.handle(command);
                }
            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Соединение потеряно!");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public String sendAuthCommandAndGetAnswerFromServer(String login, String password) {
        try {
            Command authCommand = Command.authCommand(login, password);
            dataOutputStream.writeObject(authCommand);
            Command serverCommand = readCommand();
            boolean serverConfirmAuthentication = messageService.handleServerAuthenticationAnswer(serverCommand);
            if(serverConfirmAuthentication){
                this.username = messageService.getUserNameFromServer(serverCommand);
                this.login = messageService.getLoginFromServer(serverCommand);
                return "OK";
            }
            else return messageService.getErrorAuthenticationMessageFromServer(serverCommand);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public String sendRegisterCommand(String nick, String login, String password) {
        try {
            Command registerCommand = Command.registrationCommand(nick, login, password);
            dataOutputStream.writeObject(registerCommand);
            Command command = readCommand();
            if (command == null) {
                RegOK = false;
                return "Неизвестная команда";

            }

            switch (command.getType()) {
                case REG_OK: {
                    RegOkCommandData data = (RegOkCommandData) command.getData();
                    RegOK = true;
                    return data.getMessageOKReg();
                }

                case REG_ERROR: {
                    RegErrorCommandData data = (RegErrorCommandData) command.getData();
                    RegOK = false;
                    return data.getErrorMessage();
                }

                default: {
                    RegOK = false;
                    return "Unknown type of command: " + command.getType();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void sendEndConnectionCommand() {
        try {
            Command endConnectionCommand = Command.endConnectionFromClient(this.username);
            dataOutputStream.writeObject(endConnectionCommand);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getLogin() {
        return login;
    }

    public void sendMessage(String message) throws IOException {
        sendMessage(Command.publicMessageCommand(username, message));
    }

    public void sendMessage(Command command) throws IOException {
        dataOutputStream.writeObject(command);
    }


    public void sendPrivateMessage(String message, String recipient) throws IOException {
//        String command = String.format("%s %s %s",PRIVATE_MSG_CMD_PREFIX, recipient, message);
        Command command = Command.privateMessageCommand(recipient, message);
        sendMessage(command);
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) dataInputStream.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    public String sendChangeNickCommand(String oldNick, String login, String password, String newNick) {
        try {
            Command nickChangeCommand = Command.nickChangeCommand(login, password, oldNick, newNick);
            dataOutputStream.writeObject(nickChangeCommand);
            Command command = readCommand();
            if (command == null) {
                ChangeOk = false;
                return "Неизвестная команда";
            }

            switch (command.getType()) {
                case CHANGE_OK: {
                    NickChangeOkCommandData data = (NickChangeOkCommandData) command.getData();
                    ChangeOk = true;
                    return data.getMessageOKChange();
                }

                case CHANGE_FAIL: {
                    ChangeErrorCommandData data = (ChangeErrorCommandData) command.getData();
                    ChangeOk = false;
                    return data.getErrorChangeMessage();
                }

                default: {
                    ChangeOk = false;
                    return "Unknown type of command: " + command.getType();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}



