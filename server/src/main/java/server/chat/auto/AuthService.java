package server.chat.auto;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password);
}