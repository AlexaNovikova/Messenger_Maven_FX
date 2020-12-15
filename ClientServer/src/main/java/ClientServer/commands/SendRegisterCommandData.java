package ClientServer.commands;

import java.io.Serializable;

public class SendRegisterCommandData implements Serializable {

    private final String login;
    private final String password;
    private final String nick;

    public SendRegisterCommandData(String login, String password, String nick) {
        this.login = login;
        this.password = password;
        this.nick=nick;
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
