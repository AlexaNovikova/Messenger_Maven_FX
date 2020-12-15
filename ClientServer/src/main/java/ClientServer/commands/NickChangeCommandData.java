package ClientServer.commands;

import java.io.Serializable;

public class NickChangeCommandData implements Serializable {

    private final String login;
    private final String password;
    private final String oldNick;
    private final String newNick;

    public NickChangeCommandData(String login, String password, String oldNick, String newNick) {
        this.login = login;
        this.password = password;
        this.oldNick=oldNick;
        this.newNick=newNick;
    }

    public String getNewNick() {
        return newNick;
    }

    public String getOldNick() {
        return oldNick;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
