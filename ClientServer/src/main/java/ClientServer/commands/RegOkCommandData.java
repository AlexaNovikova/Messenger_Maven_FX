package ClientServer.commands;

import java.io.Serializable;

public class RegOkCommandData implements Serializable {

    private String messageOKReg;

    public RegOkCommandData(String messageOKReg) {
        this.messageOKReg= messageOKReg;
    }

    public String getMessageOKReg() {
        return messageOKReg;
    }
}
