package ru.otus.messages;

import ru.otus.app.Message;
import ru.otus.messageSystem.Address;

// Запрос к базе данных на получение Id пользователя по его имени
public class GetUserIdByNameMessage extends Message {
    public GetUserIdByNameMessage(Address from, Address to, String name) {
        super(from, to, name, GetUserIdByNameMessage.class);
    }
}
