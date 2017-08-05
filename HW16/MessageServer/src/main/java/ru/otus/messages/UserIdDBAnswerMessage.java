package ru.otus.messages;

import ru.otus.app.Message;
import ru.otus.messageSystem.Address;

// Ответ от базы данных, содержащий id пользователя
public class UserIdDBAnswerMessage extends Message {
    public UserIdDBAnswerMessage(Address from, Address to, String userId) {
        super(from, to, userId, UserIdDBAnswerMessage.class);
    }
}
