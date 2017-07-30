package ru.otus.frontend;

import ru.otus.db.MsgGetUserId;
import ru.otus.messageSystem.MessageSystemContext;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messageSystem.Message;

import java.util.HashMap;
import java.util.Map;

public class FrontendServiceImpl implements FrontendService, Addressee {
    private final Address address;
    private final MessageSystemContext context;

    private final Map<Long, String> users = new HashMap<>();

    public FrontendServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }

    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void handleRequest(String login) {
        Message message = new MsgGetUserId(context.getMessageSystem(), getAddress(), context.getDbAddress(), login);
        context.getMessageSystem().sendMessage(message);
    }

    public void addUser(long id, String name) {
        users.put(id, name);
        System.out.println("User: " + name + " has id: " + id);
    }
}
