package server.chat.auto;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password) throws ClassNotFoundException;
    Integer registration (String nick, String login, String password);

    Integer changeNick(String login, String password, String oldNick, String newNick);
}