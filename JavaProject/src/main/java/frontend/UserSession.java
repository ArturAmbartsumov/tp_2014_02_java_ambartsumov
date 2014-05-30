package frontend;

import messageSistem.Address;

/**
 * Created by artur on 23.05.14.
 */
public class UserSession {
    private Address accountService;

    private String userName;
    private String sessionId;
    private Integer userId;
    private RegStatus regStatus;
    private AuthStatus authStatus;

    public UserSession(String sessionId, Action action) {
        if (action == Action.ANONYM) {
            this.sessionId = "Сессия не открыта";
            this.userName = "Аноним";
            this.userId = 0;
        }
        if (action == Action.AUTH) {
            this.sessionId = sessionId;
            setAuthStatus(AuthStatus.WAITING);
        }
        if (action == Action.REG) {
            this.sessionId = sessionId;
            setRegStatus(RegStatus.WAITING);
        }
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public RegStatus getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(RegStatus status) {
        regStatus = status;
        authStatus = null;
    }

    public AuthStatus getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(AuthStatus status) {
        authStatus = status;
        regStatus = null;
    }

    public void setUserId(Integer userId) {
        if (userId == null)
            this.authStatus = AuthStatus.WRONG_DATA;
        this.userId = userId;
    }
}
