package frontend;


import dbService.AccountService;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import dbService.models.UserDataSet;
import exceptions.WrongDataException;
import messageSistem.Address;
import messageSistem.MessageSystem;
import messageSistem.Sleeper;
import messageSistem.Subscriber;
import messageSistem.msg.Msg_toAS_loginUser;

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

    private AccountService accauntService;
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
    private Address address;
    private int handleCount;
    private MessageSystem messageSystem;
    private DateFormat formatter = new SimpleDateFormat("HH.mm.ss");

    public Frontend(MessageSystem ms) {
        this.handleCount = 0;
        this.messageSystem = ms;
        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setFrontend(address);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //System.out.println("gdhf");
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

    public void setId(String sessionId, Integer userId) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
    }

    public void setError(String sessionId, String errorType) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        if (errorType.equals("AccountServiceError")) userSession.setError(true);
        if (errorType.equals("EmptyFormError")) userSession.setEmpty(true);
        if (errorType.equals("WrongDataError")) userSession.setWrong(true);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        this.handleCount++;

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
            exit(request, response, pageVariables);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        handleCount++;

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
        UserSession userSession = new UserSession(sessionId, login, messageSystem.getAddressService());
        sessionIdToUserSession.put(sessionId, userSession);

        Address frontendAddress = getAddress();
        Address accountServiceAddress = messageSystem.getAddressService().getAccountService();

        messageSystem.sendMessage(new Msg_toAS_loginUser(frontendAddress, accountServiceAddress,
                login, password, sessionId));
        response.sendRedirect("/waiting");
    }

    private void waitingScreen(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (userSession == null) {
            response.sendRedirect("/authform");
        }
        else {
            if (userSession.isAuthorized()) {
                response.sendRedirect("/game");
                return;
            }
            if (userSession.isWrong()) {
                session.setAttribute("message", "Неверный логин или пароль!");
                response.sendRedirect("/authform");
                return;
            }
            if (userSession.isError()) {
                session.setAttribute("message", "Ошибка базы данных!");
                response.sendRedirect("/authform");
                return;
            }
            pageVariables.put("refreshPeriod", "1000");
            response.getWriter().println(PageGenerator.getPage("waiting.tml", pageVariables));
        }
    }

    private void registration(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            this.accauntService.registration(login, email, password);
            session.setAttribute("message", "регистрация прошла успешно!\nТеперь авторизируйтесь.");
            response.sendRedirect("/authform");

        }
        catch (AccountServiceException | EmptyDataException | WrongDataException e) {
            session.setAttribute("message", e.getMessage());
            response.sendRedirect("/regform");
        }
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

    private void exit(HttpServletRequest request,
                      HttpServletResponse response,
                      Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        sessionIdToUserSession.remove(session.getId());
        response.sendRedirect("/");
    }

    private void userId(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (!userSession.isAuthorized() || userSession == null) {
            pageVariables.put("message", "Вы не авторизированы!");

            pageVariables.put("user", new UserSession("no session", "Anonim", null));
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