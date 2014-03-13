package frontend;

import dbService.UserDAOImpl;
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

/**
 * Created by artur on 15.02.14.
 */
public class Frontend extends HttpServlet {

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
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getPathInfo().equals("/authform")) {
            authForm(request, response, pageVariables);
            return;
        }

        if (request.getPathInfo().equals("/regform")) {
            regForm(request, response, pageVariables);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void authForm(HttpServletRequest request,
                          HttpServletResponse response,
                          Map<String, Object> pageVariables) throws ServletException, IOException {
        UserDataSet user = new UserDataSet(request.getParameter("login"),
                                              request.getParameter("password"));
        if (user.getUsername() != null && user.getPassword() != null) {
            try {
                UserDAOImpl userDAO = new UserDAOImpl();
                UserDataSet checkUser = userDAO.getUserByNameAndPass(user.getUsername(),
                                                              user.getPassword());
                if (checkUser != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("sessionId", userIdGenerator.getAndIncrement());
                    session.setAttribute("user", checkUser);
                    response.sendRedirect("/userid");
                }
                else {
                    pageVariables.put("message", "Ошибка: не правильный логин или пароль!!");
                    response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Ошибка 'SQLException' При авторизации",
                        JOptionPane.WARNING_MESSAGE);
                response.sendRedirect("/regform");
            }
        }
        else {
            pageVariables.put("message", "Введите логин и пароль!");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
        }
    }

    private void regForm(HttpServletRequest request,
                         HttpServletResponse response,
                         Map<String,Object> pageVariables) throws ServletException, IOException {

        UserDataSet newUser = new UserDataSet(request.getParameter("login"),
                                              request.getParameter("email"),
                                              request.getParameter("password"));
        if (newUser.getUsername() != null &&
               newUser.getEmail() != null &&
               newUser.getPassword() != null) {
            UserDAOImpl userDAO = new UserDAOImpl();
            try {
                UserDataSet checkUser = userDAO.getUserByName(newUser.getUsername());
                if (checkUser == null) {
                    userDAO.addUser(newUser);
                }
                else {
                    pageVariables.put("message", "Это имя уже занято!");
                    response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                                              e.getMessage(),
                                              "Ошибка 'SQLException' При добавлении пользователя",
                                              JOptionPane.WARNING_MESSAGE);
                response.sendRedirect("/regform");
                return;
            }
            response.sendRedirect("/authform");
        }
        else if(newUser.getUsername() == null ||
                newUser.getEmail() == null ||
                newUser.getPassword() == null) {
            pageVariables.put("message", "Вы заполнили не все поля!");
            response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
        }
        else {
            pageVariables.put("message", "Заполните форму регистрации");
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
}