package ClientServer.commands;

import java.io.Serializable;

public class ConnectionCloseFromClientCommandData implements Serializable {

    private String client;

    public ConnectionCloseFromClientCommandData(String client) {
        this.client = client;

    }

    public String getClient() {
        return client;
    }


}
