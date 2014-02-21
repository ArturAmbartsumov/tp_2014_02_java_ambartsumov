package frontend;

import templater.PageGenerator;

import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by artur on 15.02.14.
 */
public class Frontend extends HttpServlet {

    public Frontend() {
        User user1 = new User("Artur", "12345");
        User user2 = new User("Ivan", "123");
        userList.add(user1);
        userList.add(user2);
    }

    class User {
        public String name;
        public String password;
        public User(String n, String p) {
            name = n;
            password = p;
        }
        public boolean equals(Object obj) {
            if(obj == this)
                return true;

            if(obj == null)
                return false;

            if(!(getClass() == obj.getClass()))
                return false;
            else
            {
                User tmp = (User)obj;
                if(tmp.name.equals(name) && tmp.password.equals(password))
                    return true;
                else
                    return false;
            }
        }
    }


    private User currentUser = new User("", "");
    private ArrayList<User> userList = new ArrayList<User>();
    private AtomicLong userIdGenerator = new AtomicLong();
    private boolean userChange = false;
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
        }

        if (request.getPathInfo().equals("/timer")) {
            timer(request, response, pageVariables);
        }

        if (request.getPathInfo().equals("/userid")) {
            userId(request, response, pageVariables);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
    }

    private void authForm(HttpServletRequest request,
                          HttpServletResponse response,
                          Map<String, Object> pageVariables) throws ServletException, IOException {
        User logEndPassword = new User(request.getParameter("login"), request.getParameter("password"));
        if (logEndPassword.name != null && logEndPassword.password != null) {
            if (userList.contains(logEndPassword)) {
                response.sendRedirect("/userid");
            }
            else {
                pageVariables.put("message", "Ошибка: не правильный логин или пароль!!");
                response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            }
        }
        else {
            pageVariables.put("message", "Введите логин и пароль!");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
        }
    }

    private void timer(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, Object> pageVariables) throws ServletException, IOException {
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        response.getWriter().println(PageGenerator.getPage("timer.tml", pageVariables));
    }

    private void userId(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> pageVariables) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            userId = userIdGenerator.getAndIncrement();
            session.setAttribute("userId", userId);
        }
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("userId", userId);
        response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
    }
}