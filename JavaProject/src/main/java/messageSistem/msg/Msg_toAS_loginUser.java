package messageSistem.msg;

import dbService.AccountService;
import messageSistem.Address;

/**
 * Created by artur on 23.05.14.
 */
public class Msg_toAS_loginUser extends Msg_toAS {
    private String login;
    private String password;
    private String sessionId;

    public Msg_toAS_loginUser(Address from, Address to, String login, String password, String sessionId) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    public void exec(AccountService accountService) {
        /*try {
            Long id = accountService.tryLogin(login, password);
            accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
        }
        catch (DBException e) {
            accountService.getMessageSystem().sendMessage(new MsgDBError(getTo(), getFrom(), sessionId));
        }*/
    }
}
