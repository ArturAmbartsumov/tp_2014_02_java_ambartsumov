package messageSistem.msg;

import dbService.AccountService;
import messageSistem.Address;

/**
 * Created by artur on 24.05.14.
 */
public class Msg_toAS_registr extends Msg_toAS {
    private String login;
    private String password;
    private String email;
    private String sessionId;

    public Msg_toAS_registr(Address from, Address to, String login, String password, String email, String sessionId) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.email = email;
        this.sessionId = sessionId;
    }

    public void exec(AccountService accountService) {
        /*try {
            Long id = accountService.tryRegister(login, password, email);
            accountService.getMessageSystem()
                    .sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
        }
        catch (DBException e) {
            accountService.getMessageSystem()
                    .sendMessage(new MsgDBError(getTo(), getFrom(), sessionId));
        }*/
    }
}
