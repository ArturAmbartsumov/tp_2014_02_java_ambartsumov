import dbService.AccountService;
import frontend.Frontend;
import messageSistem.MessageSystem;
import org.eclipse.jetty.rewrite.handler.RedirectRegexRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by artur on 15.02.14.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        MessageSystem messageSystem = new MessageSystem();

        AccountService accountService = new AccountService(messageSystem);
        Thread accountServiceThread = new Thread(accountService);

        Frontend frontend = new Frontend(messageSystem);
        Thread frontendThread = new Thread(frontend);

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        accountServiceThread.start();
        frontendThread.start();
        server.start();
        server.join();
    }
}

//простые типы, их размер, методы класса обджект, автоупаковка, дженерик
//аннатации, public
