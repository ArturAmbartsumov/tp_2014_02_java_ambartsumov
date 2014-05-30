package frontend;


import dbService.AccountService;
import messageSistem.Address;
import messageSistem.MessageSystem;
import messageSistem.Sleeper;
import messageSistem.Subscriber;
import messageSistem.msg.Msg_toAS_auth;
import messageSistem.msg.Msg_toAS_registr;

import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.Runnable;
import java.lang.Thread;

/**
 * Created by artur on 15.02.14.
 */
public class Frontend extends HttpServlet implements Subscriber, Runnable{

    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
    private Address address;
    private MessageSystem messageSystem;
    private DateFormat formatter = new SimpleDateFormat("HH.mm.ss");

    public Frontend(MessageSystem ms) {
        this.messageSystem = ms;
        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setFrontend(address);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            messageSystem.execForSubscriber(this);
            Sleeper.sleep(Sleeper.TICK);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public void successAuth(String sessionId, Integer userId, String name) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
        userSession.setUserName(name);
        userSession.setAuthStatus(AuthStatus.OK);
    }

    public void setAuthError(String sessionId, AuthStatus status) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setAuthStatus(status);
    }

    public void successReg(String sessionId) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setRegStatus(RegStatus.OK);
    }

    public void setRegError(String sessionId, RegStatus status) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setRegStatus(status);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getPathInfo().equals("/authform")) {
            authForm(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/regform")) {
            regForm(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/waiting")) {
            waitingScreen(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/game")) {
            userId(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/exit")) {
            exit(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if (request.getPathInfo().equals("/authorization")) {
            authorization(request, response);
            return;
        }

        if (request.getPathInfo().equals("/registration")) {
            registration(request, response);
            return;
        }


        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void authForm(HttpServletRequest request,
                          HttpServletResponse response,
                          Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("message") != null) {
            String message = (String) session.getAttribute("message");
            pageVariables.put("message", message);
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            session.setAttribute("message", null);
        } else {
            pageVariables.put("message", "Введите логин и пароль!");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
        }
    }

    private void authorization(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (!checkAuthForm(login, password)) {
            session.setAttribute("message", "Заполните форму!");
            response.sendRedirect("/authform");
            return;
        }

        String sessionId = request.getSession().getId();
        UserSession userSession = new UserSession(sessionId, Action.AUTH);
        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService();

        messageSystem.sendMessage(new Msg_toAS_auth(frontendAddress, accountServiceAddress,
                login, password, sessionId));
        response.sendRedirect("/waiting");
    }

    private void regForm(HttpServletRequest request,
                         HttpServletResponse response,
                         Map<String,Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("message") != null) {
            String message = (String) session.getAttribute("message");
            pageVariables.put("message", message);
            response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
            session.setAttribute("message", null);
        } else {
            pageVariables.put("message", "Заполните форму регистрации!");
            response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
        }
    }

    private void registration(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!checkRegForm(login, password, email)) {
            session.setAttribute("message", "Заполните форму!");
            response.sendRedirect("/authform");
            return;
        }

        String sessionId = request.getSession().getId();
        UserSession userSession = new UserSession(sessionId, Action.REG);
        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService();

        messageSystem.sendMessage(new Msg_toAS_registr(frontendAddress, accountServiceAddress,
                login, password, email, sessionId));
        response.sendRedirect("/waiting");
    }

    private void waitingScreen(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (userSession == null) {
            response.sendRedirect("/");
            System.out.println("\nОшибка: не найдена сессия");
        }
        else {
            if (userSession.getAuthStatus() != null) {
                if (userSession.getAuthStatus() == AuthStatus.OK) {
                    response.sendRedirect("/game");
                    return;
                }
                if (userSession.getAuthStatus() == AuthStatus.WRONG_DATA) {
                    session.setAttribute("message", "Неверный логин или пароль!");
                    response.sendRedirect("/authform");
                    return;
                }
                if (userSession.getAuthStatus() == AuthStatus.SQL_ERROR) {
                    session.setAttribute("message", "Ошибка базы данных!");
                    response.sendRedirect("/authform");
                    return;
                }
                pageVariables.put("refreshPeriod", "1000");
                response.getWriter().println(PageGenerator.getPage("waiting.tml", pageVariables));
                return;
            }

            if (userSession.getRegStatus() != null) {
                if (userSession.getRegStatus() == RegStatus.OK) {
                    sessionIdToUserSession.remove(session.getId());
                    response.sendRedirect("/authform");
                    return;
                }
                if (userSession.getRegStatus() == RegStatus.DUPLICATE) {
                    session.setAttribute("message", "Такой пользователь уже существует!");
                    response.sendRedirect("/regform");
                    return;
                }
                if (userSession.getRegStatus() == RegStatus.SQL_ERROR) {
                    session.setAttribute("message", "Ошибка базы данных!");
                    response.sendRedirect("/regform");
                    return;
                }
                pageVariables.put("refreshPeriod", "1000");
                response.getWriter().println(PageGenerator.getPage("waiting.tml", pageVariables));
                return;
            }
            response.sendRedirect("/");
        }
    }

    private void exit(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        sessionIdToUserSession.remove(session.getId());
        response.sendRedirect("/");
    }

    private void userId(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (userSession == null || !(userSession.getAuthStatus() == AuthStatus.OK)) {
            pageVariables.put("message", "Вы не авторизированы!");
            UserSession anonim = new UserSession(session.getId(), Action.ANONYM);
            pageVariables.put("user", anonim);
        } else {
            pageVariables.put("message", "Вы авторизированы!");
            pageVariables.put("user", userSession);
        }
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));
    }

    public String getTime() {
        Date date = new Date();
        return formatter.format(date);
    }

    private boolean checkRegForm(String login, String password, String email) {
        return login != null && !login.isEmpty() &&
                password != null && !password.isEmpty() &&
                email != null && !email.isEmpty();
    }

    private boolean checkAuthForm(String login, String password) {
        return login != null && !login.isEmpty() &&
                password != null && !password.isEmpty();
    }
}