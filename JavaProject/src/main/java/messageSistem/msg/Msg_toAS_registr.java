package messageSistem.msg;

import dbService.AccountService;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import exceptions.WrongDataException;
import frontend.RegStatus;
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
        try {
            accountService.registration(login, email, password);
            accountService.getMessageSystem().sendMessage(new Msg_toFE_successReg(getTo(), getFrom(), sessionId));
        }
        catch (AccountServiceException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_regError(getTo(), getFrom(), sessionId, RegStatus.SQL_ERROR));
        }
        catch (WrongDataException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_regError(getTo(), getFrom(), sessionId, RegStatus.DUPLICATE));
        }
        catch (EmptyDataException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_regError(getTo(), getFrom(), sessionId, RegStatus.EMPTY_DATA));
        }
    }
}
