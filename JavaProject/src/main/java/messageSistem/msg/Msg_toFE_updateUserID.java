package messageSistem.msg;

import frontend.Frontend;
import messageSistem.Address;

/**
 * Created by artur on 24.05.14.
 */
public class Msg_toFE_updateUserID extends Msg_toFE {
    private String sessionId;
    private Integer id;

    public Msg_toFE_updateUserID(Address from, Address to, String sessionId, Integer id) {
        super(from, to);
        this.sessionId = sessionId;
        this.id = id;
    }

    public void exec(Frontend frontend) {
        frontend.setId(sessionId, id);
    }
}
