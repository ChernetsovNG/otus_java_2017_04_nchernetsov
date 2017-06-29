package ru.otus.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.base.dataSets.AddressDataSet;
import ru.otus.base.dataSets.PhoneDataSet;
import ru.otus.base.dataSets.UserDataSet;
import ru.otus.dbService.DBService;
import ru.otus.dbService.DBServiceHibernateImpl;

import java.util.Collections;

public class Main {
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "HW12/public_html";

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        // Create database (and cache)
        DBService dbService = new DBServiceHibernateImpl();

        for (int i = 1; i <= 6; i++) {
            UserDataSet userDataSet = new UserDataSet("User " + i,
                new AddressDataSet("Street " + i, i),
                Collections.singletonList(new PhoneDataSet(i, String.valueOf(i))));

            dbService.save(userDataSet);
        }
        dbService.read(1);
        dbService.read(2);

        context.addServlet(new ServletHolder(new AuthServlet("unknown", "unknown")), "/auth");
        context.addServlet(new ServletHolder(new CacheInfoServlet(dbService.getCacheStats())), "/cache_info");

        Server server = new Server(PORT);

        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();

        dbService.shutdown();
    }
}
