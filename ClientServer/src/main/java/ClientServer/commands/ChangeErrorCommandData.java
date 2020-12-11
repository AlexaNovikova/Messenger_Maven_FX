package ClientServer.commands;

import java.io.Serializable;

public class ChangeErrorCommandData implements Serializable {

    private final String errorChangeMessage;

    public ChangeErrorCommandData(String errorChangeMessage) {
        this.errorChangeMessage = errorChangeMessage;
    }

    public String getErrorChangeMessage() {
        return errorChangeMessage;
    }
}
