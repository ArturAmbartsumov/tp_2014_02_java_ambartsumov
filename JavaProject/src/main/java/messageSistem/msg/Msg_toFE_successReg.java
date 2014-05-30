package messageSistem.msg;

import frontend.Frontend;
import messageSistem.Address;

/**
 * Created by artur on 24.05.14.
 */
public class Msg_toFE_successReg extends Msg_toFE {
    private String sessionId;

    public Msg_toFE_successReg(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    public void exec(Frontend frontend) {
        frontend.successReg(sessionId);
    }
}
