package messageSistem.msg;

import frontend.Frontend;
import messageSistem.Address;

/**
 * Created by artur on 24.05.14.
 */
public class Msg_toFE_successAuth extends Msg_toFE {
    private String sessionId;
    private Integer id;
    private String name;

    public Msg_toFE_successAuth(Address from, Address to, String sessionId, Integer id, String name) {
        super(from, to);
        this.sessionId = sessionId;
        this.id = id;
        this.name = name;
    }

    public void exec(Frontend frontend) {
        frontend.successAuth(sessionId, id, name);
    }
}
