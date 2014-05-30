package messageSistem.msg;

import frontend.AuthStatus;
import frontend.Frontend;
import messageSistem.Address;

/**
 * Created by artur on 30.05.14.
 */
public class Msg_toFE_authError extends Msg_toFE {
    private String sessionId;
    private AuthStatus status;

    public Msg_toFE_authError(Address from, Address to, String sessionId, AuthStatus status) {
        super(from, to);
        this.sessionId = sessionId;
        this.status = status;
    }

    void exec(Frontend frontend) {
        frontend.setAuthError(sessionId, status);
    }
}
