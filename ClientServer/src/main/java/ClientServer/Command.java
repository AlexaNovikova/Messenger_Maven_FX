package ClientServer;

import ClientServer.commands.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private CommandType type;
    private Object data;

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommandData(login, password);
        return command;
    }

    public static Command authOkCommand(String username, String login) {
        Command command = new Command();
        command.type = CommandType.AUTH_OK;
        command.data = new AuthOkCommandData(username, login);
        return command;
    }

    public static Command authErrorCommand(String authErrorMessage) {
        Command command = new Command();
        command.type = CommandType.AUTH_ERROR;
        command.data = new AuthErrorCommandData(authErrorMessage);
        return command;
    }
    public static Command errorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCommandData(errorMessage);
        return command;
    }

    public static Command messageInfoCommand(String message, String sender) {
        Command command = new Command();
        command.type = CommandType.INFO_MESSAGE;
        command.data = new MessageInfoCommandData(message, sender);
        return command;
    }

    public static Command publicMessageCommand(String username, String message) {
        Command command = new Command();
        command.type = CommandType.PUBLIC_MESSAGE;
        command.data = new PublicMessageCommandData(username, message);
        return command;
    }

    public static Command privateMessageCommand(String receiver, String message) {
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMessageCommandData(receiver, message);
        return command;
    }

    public static Command updateUsersListCommand(List<String> users) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USERS_LIST;
        command.data = new UpdateUsersListCommandData(users);
        return command;
    }

    public static Command registrationCommand(String nick, String login, String password) {
        Command command = new Command();
        command.type = CommandType.REGISTER;
        command.data = new SendRegisterCommandData(login,password,nick);
        return command;
    }
    public static Command regOKCommand() {
        Command command = new Command();
        command.type = CommandType.REG_OK;
        command.data= new RegOkCommandData("Регистрация прошла успешно!");
        return command;
    }
    public static Command regFailCommand(String errorMessage){
        Command command = new Command();
        command.type = CommandType.REG_ERROR;
        command.data=new RegErrorCommandData(errorMessage);
        return command;
    }
    public static Command nickChangeCommand(String login, String password, String oldNick, String newNick) {
        Command command = new Command();
        command.type = CommandType.NICK_CHANGE;
        command.data = new NickChangeCommandData(login,password,oldNick,newNick);
        return command;
    }
    public static Command nickChangeOKCommand() {
        Command command = new Command();
        command.type = CommandType.CHANGE_OK;
        command.data= new NickChangeOkCommandData("Ник успешно изменен!");
        return command;
    }
    public static Command nickChangeFailCommand(String errorMessage){
        Command command = new Command();
        command.type = CommandType.CHANGE_FAIL;
        command.data=new ChangeErrorCommandData(errorMessage);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

    public static Command endConnectionFromClient(String clientNick) {
        Command command = new Command();
        command.type = CommandType.END_CONNECTION_WANT;
        command.data = new ConnectionCloseFromClientCommandData(clientNick);
        return command;
    }

    public static Command endConnectionFromServer (String clientNick) {
        Command command = new Command();
        command.type = CommandType.END_CONNECTION_OK;
        command.data = new ConnectionCloseFromClientCommandData(clientNick);
        return command;
    }
}


