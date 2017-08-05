package ru.otus.messages;

import ru.otus.app.Message;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;

// запрос к серверу сообщений на установление связи для общения
public class HandshakeDemandMessage extends Message {
    public HandshakeDemandMessage(Address from, Address to) {
        super(from, to, HandshakeDemandMessage.class);
    }

    @Override
    public void exec(Addressee addressee) {
    }
}
