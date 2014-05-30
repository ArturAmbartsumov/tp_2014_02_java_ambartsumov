package messageSistem.msg;

import dbService.AccountService;
import dbService.models.UserDataSet;
import exceptions.AccountServiceException;
import exceptions.WrongDataException;
import exceptions.EmptyDataException;
import frontend.AuthStatus;
import messageSistem.Address;

/**
 * Created by artur on 23.05.14.
 */
public class Msg_toAS_auth extends Msg_toAS {
    private String login;
    private String password;
    private String sessionId;

    public Msg_toAS_auth(Address from, Address to, String login, String password, String sessionId) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    public void exec(AccountService accountService) {
        try {
            UserDataSet user = accountService.authorization(login, password);
            accountService.getMessageSystem().sendMessage(new Msg_toFE_successAuth(getTo(), getFrom(), sessionId, user.getId(), user.getUsername()));
        }
        catch (AccountServiceException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_authError(getTo(), getFrom(), sessionId, AuthStatus.SQL_ERROR));
        }
        catch (WrongDataException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_authError(getTo(), getFrom(), sessionId, AuthStatus.WRONG_DATA));
        }
        catch (EmptyDataException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_authError(getTo(), getFrom(), sessionId, AuthStatus.EMPTY_DATA));
        }
    }
}
