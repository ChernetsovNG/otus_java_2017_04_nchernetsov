package ru.otus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.base.dataSets.AddressDataSet;
import ru.otus.base.dataSets.PhoneDataSet;
import ru.otus.base.dataSets.UserDataSet;
import ru.otus.service.CacheInfoService;
import ru.otus.service.DBService;
import ru.otus.service.DBServiceHibernateImpl;
import ru.otus.servlet.AuthServlet;
import ru.otus.servlet.CacheInfoServlet;

import java.util.Collections;

public class Main {
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "HW13/public_html";

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");

        DBService dbService = (DBService) context.getBean("dbService");
        CacheInfoService cacheInfoService = (CacheInfoService) context.getBean("cacheInfoService");

        for (int i = 1; i <= 6; i++) {
            UserDataSet userDataSet = new UserDataSet("User " + i,
                new AddressDataSet("Street " + i, i),
                Collections.singletonList(new PhoneDataSet(i, String.valueOf(i))));

            dbService.save(userDataSet);
        }
        dbService.read(1);
        dbService.read(2);


    }
}
