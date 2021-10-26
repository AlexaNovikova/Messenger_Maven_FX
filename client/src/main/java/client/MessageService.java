package client;

import ClientServer.Command;
import ClientServer.CommandType;
import ClientServer.commands.AuthErrorCommandData;
import ClientServer.commands.AuthOkCommandData;
import ClientServer.commands.ErrorCommandData;
import client.controllers.ChatController;

public class MessageService {
    private ChatController chatController;

    public MessageService() {

    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void handle(Command command) {

    }

    public boolean handleServerAuthenticationAnswer(Command command) {
        if(command == null){
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
}
