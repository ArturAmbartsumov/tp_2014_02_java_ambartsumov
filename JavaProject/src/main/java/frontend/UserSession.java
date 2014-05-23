package frontend;

import messageSistem.Address;
import messageSistem.AddressService;

/**
 * Created by artur on 23.05.14.
 */
public class UserSession {
    private Address accountService;

    private String login;
    private String sessionId;
    private Integer userId;
    private boolean isWrong = false;
    private boolean isError = false;
    private boolean isEmpty = false;

    public UserSession(String sessionId, String name, AddressService addressService) {
        this.sessionId = sessionId;
        this.login = name;
    }

    public String getLogin(){
        return login;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        if (userId == null)
            isWrong = true;

        this.userId = userId;
    }

    public boolean isWrong() {
        return isWrong;
    }

    public void setWrong(boolean isWrong) {
        this.isWrong = isWrong;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    public boolean isAuthorized() {
        return !isWrong() && !isError() && !isEmpty() && getUserId() != null;
    }
}
