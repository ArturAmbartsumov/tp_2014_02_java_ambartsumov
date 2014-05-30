package messageSistem.msg;

import frontend.Frontend;
import frontend.RegStatus;
import messageSistem.Address;

/**
 * Created by artur on 30.05.14.
 */
public class Msg_toFE_regError extends Msg_toFE {
    private String sessionId;
    private RegStatus status;

    public Msg_toFE_regError(Address from, Address to, String sessionId, RegStatus status) {
        super(from, to);
        this.sessionId = sessionId;
        this.status = status;
    }

    void exec(Frontend frontend) {
        frontend.setRegError(sessionId, status);
    }
}
