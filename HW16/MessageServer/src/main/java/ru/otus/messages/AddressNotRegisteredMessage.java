package ru.otus.messages;

import ru.otus.app.Message;
import ru.otus.messageSystem.Address;

public class AddressNotRegisteredMessage extends Message {
    public AddressNotRegisteredMessage(Address from, Address to) {
        super(from, to, "", AddressNotRegisteredMessage.class);
    }
}
