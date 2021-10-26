package client;

import ClientServer.Command;
import ClientServer.CommandType;
import ClientServer.commands.*;
import client.controllers.ChatController;
import javafx.application.Platform;

public class MessageService {
    private ChatController chatController;

    public MessageService() {

    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void handle(Command command) {
        switch (command.getType()) {
            case INFO_MESSAGE: {
                MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                String message = data.getMessage();
                String sender = data.getSender();
                String formattedMessage = sender != null ? String.format("%s: %s", sender, message) : message;
                Platform.runLater(() -> {
                    chatController.appendMessage(formattedMessage);
                });
                break;
            }
            case ERROR: {
                ErrorCommandData data = (ErrorCommandData) command.getData();
                String errorMessage = data.getErrorMessage();
                Platform.runLater(() -> {
                    chatController.showError("Server error", errorMessage);
                });
                break;
            }
            case UPDATE_USERS_LIST: {
                UpdateUsersListCommandData data = (UpdateUsersListCommandData) command.getData();
                Platform.runLater(() -> chatController.updateUsers(data.getUsers()));
                break;
            }
            default:
                Platform.runLater(() -> {
                    chatController.showError("Unknown command from server!", command.getType().toString());
                });
        }
    }

    public boolean handleServerAuthenticationAnswer(Command command) {
        if (command == null) {
            throw new NullPointerException("Command is null");
        }
        return command.getType().equals(CommandType.AUTH_OK);
    }

    public String getUserNameFromServer(Command serverCommand) {
        AuthOkCommandData data = (AuthOkCommandData) serverCommand.getData();
        return data.getUsername();
    }

    public String getLoginFromServer(Command serverCommand) {
        AuthOkCommandData data = (AuthOkCommandData) serverCommand.getData();
        return data.getLogin();
    }

    public String getErrorAuthenticationMessageFromServer(Command serverCommand) {
        switch (serverCommand.getType()) {
            case AUTH_ERROR: {
                AuthErrorCommandData data = (AuthErrorCommandData) serverCommand.getData();
                return data.getErrorMessage();
            }
            case ERROR: {
                ErrorCommandData data = (ErrorCommandData) serverCommand.getData();
                System.exit(1);
            }
            default: {
                return "Unknown type of command: " + serverCommand.getType();
            }
        }
    }

    public boolean handleServerRegistrationAnswer(Command command) {
        if (command == null) {
            throw new NullPointerException("Command is null");
        }
        return command.getType().equals(CommandType.REG_OK);
    }

    public String getServerRegistrationConfirmation(Command command) {
        RegOkCommandData data = (RegOkCommandData) command.getData();
        return data.getMessageOKReg();
    }

    public String getServerRegistrationError(Command command) {
        RegErrorCommandData data = (RegErrorCommandData) command.getData();
        return data.getErrorMessage();
    }

    public boolean handleServerChangeNickResultAnswer(Command command) {
        return command.getType().equals(CommandType.CHANGE_OK);
    }

    public String getErrorChangeFromServer(Command command) {
        ChangeErrorCommandData data = (ChangeErrorCommandData) command.getData();
        return data.getErrorChangeMessage();
    }

    public String getNickChangeResult(Command command) {
        NickChangeOkCommandData data = (NickChangeOkCommandData) command.getData();
        return data.getMessageOKChange();
    }
}



