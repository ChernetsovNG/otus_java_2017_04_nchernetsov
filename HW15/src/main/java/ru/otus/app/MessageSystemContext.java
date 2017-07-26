package ru.otus.app;

import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;

public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address cacheInfoServiceAddress;
    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getCacheInfoServiceAddress() {
        return cacheInfoServiceAddress;
    }

    public void setCacheInfoServiceAddress(Address cacheInfoServiceAddress) {
        this.cacheInfoServiceAddress = cacheInfoServiceAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }

}
