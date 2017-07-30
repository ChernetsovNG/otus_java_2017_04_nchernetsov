package ru.otus;

import ru.otus.app.DBService;
import ru.otus.app.FrontendService;
import ru.otus.messageSystem.MessageSystemContext;
import ru.otus.db.DBServiceImpl;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MessageSystem messageSystem = new MessageSystem();

        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        context.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);

        FrontendService frontendService = new FrontendServiceImpl(context, frontAddress);
        frontendService.init();

        DBService dbService = new DBServiceImpl(context, dbAddress);
        dbService.init();

        messageSystem.start();

        frontendService.handleRequest("tully");
        frontendService.handleRequest("sully");
    }
}
