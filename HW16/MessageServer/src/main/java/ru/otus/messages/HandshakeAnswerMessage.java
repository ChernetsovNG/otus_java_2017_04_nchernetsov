package ru.otus.messages;

import ru.otus.app.Message;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;

// Ответ от сервра сообщений об успешном установлении связи для общения
public class HandshakeAnswerMessage extends Message {
    public HandshakeAnswerMessage(Address from, Address to) {
        super(from, to, HandshakeDemandMessage.class);
    }

    @Override
    public void exec(Addressee addressee) {
    }
}
