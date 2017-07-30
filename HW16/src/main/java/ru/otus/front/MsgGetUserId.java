package ru.otus.front;

import ru.otus.app.DBService;
import ru.otus.db.MsgToDB;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;

public class MsgGetUserId extends MsgToDB {
    private final MessageSystem messageSystem;
    private final String login;

    public MsgGetUserId(MessageSystem messageSystem, Address from, Address to, String login) {
        super(from, to);
        this.login = login;
        this.messageSystem = messageSystem;
    }

    @Override
    public void exec(DBService dbService) {
        int id = dbService.getUserId(login);
        messageSystem.sendMessage(new MsgGetUserIdAnswer(getTo(), getFrom(), login, id));
    }
}
