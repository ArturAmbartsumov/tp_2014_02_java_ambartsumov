package messageSistem.msg;

import frontend.Frontend;
import messageSistem.Address;

/**
 * Created by artur on 23.05.14.
 */
public class Msg_toFE_asError extends Msg_toFE {
    private String sessionId;

    public Msg_toFE_asError(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    void exec(Frontend frontend) {
        frontend.setError(sessionId, "AccountServiceError");
    }
}
