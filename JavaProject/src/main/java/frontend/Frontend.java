package frontend;

import dbService.AccountService;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import models.UserDataSet;
import templater.PageGenerator;

import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Runnable;
import java.lang.Thread;

/**
 * Created by artur on 15.02.14.
 */
public class Frontend extends HttpServlet implements Runnable{

    int handleCount = 0;
    private AccountService accauntService = new  AccountService();

    private AtomicLong userIdGenerator = new AtomicLong();
    DateFormat formatter = new SimpleDateFormat("HH.mm.ss");

    public String getTime() {
        Date date = new Date();
        return formatter.format(date);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        handleCount++;

        if (request.getPathInfo().equals("/authform")) {
            authForm(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/regform")) {
            regForm(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/userid")) {
            userId(request, response, pageVariables);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

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

    private void authorization(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        UserDataSet user;
        try {
            user = accauntService.authorization(login, password);
            session.setAttribute("sessionId", userIdGenerator.getAndIncrement());
            session.setAttribute("user", user);
            response.sendRedirect("/userid");

        }
        catch (AccountServiceException | EmptyDataException e) {
            session.setAttribute("message", e.getMessage());
            response.sendRedirect("/authform");
        }
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

    private void registration(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            accauntService.registration(login, email, password);
            session.setAttribute("message", "регистрация прошла успешно!\nТеперь авторизируйтесь.");
            response.sendRedirect("/authform");

        }
        catch (AccountServiceException | EmptyDataException e) {
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

    private void userId(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDataSet user = (UserDataSet) session.getAttribute("user");
        if (user == null) {
            pageVariables.put("message", "Вы не авторизированы!");

            pageVariables.put("user", new UserDataSet(0, "0"));
        } else {
            pageVariables.put("message", "Вы авторизированы!");
            pageVariables.put("user", user);
        }
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("session", session);
        response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println(handleCount);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}