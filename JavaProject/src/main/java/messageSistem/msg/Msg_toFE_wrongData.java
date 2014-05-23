package messageSistem.msg;

import frontend.Frontend;
import messageSistem.Address;

/**
 * Created by artur on 24.05.14.
 */
public class Msg_toFE_wrongData  extends Msg_toFE {
    private String sessionId;

    public Msg_toFE_wrongData(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    void exec(Frontend frontend) {
        frontend.setError(sessionId, "WrongDataError");
    }
}
