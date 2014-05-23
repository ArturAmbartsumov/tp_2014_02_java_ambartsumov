package messageSistem.msg;

import dbService.AccountService;
import dbService.models.UserDataSet;
import exceptions.AccountServiceException;
import exceptions.WrongDataException;
import exceptions.EmptyDataException;
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
        try {
            UserDataSet user = accountService.authorization(login, password);
            int id = user.getId().intValue();
            accountService.getMessageSystem().sendMessage(new Msg_toFE_updateUserID(getTo(), getFrom(), sessionId, id));
        }
        catch (AccountServiceException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_asError(getTo(), getFrom(), sessionId));
        }
        catch (WrongDataException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_wrongData(getTo(), getFrom(), sessionId));
        }
        catch (EmptyDataException e) {
            accountService.getMessageSystem().sendMessage(new Msg_toFE_emptyForm(getTo(), getFrom(), sessionId));
        }
    }
}
