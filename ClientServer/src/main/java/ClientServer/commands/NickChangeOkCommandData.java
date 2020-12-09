package ClientServer.commands;

import java.io.Serializable;

public class NickChangeOkCommandData implements Serializable {

    private String getMessageOKChange;

    public NickChangeOkCommandData(String getMessageOKChange) {
        this.getMessageOKChange= getMessageOKChange;
    }

    public String getMessageOKChange() {
        return getMessageOKChange;
    }
}
