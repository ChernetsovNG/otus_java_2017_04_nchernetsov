package ru.otus.db;

import ru.otus.app.DBService;
import ru.otus.messageSystem.MessageSystemContext;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;

public class DBServiceImpl implements DBService, Addressee {
    private final Address address;
    private final MessageSystemContext context;

    public DBServiceImpl(MessageSystemContext context, Address address) {
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

    public int getUserId(String name) {
        //todo: load id from db
        return name.hashCode();
    }
}
