package ru.otus.db;

import ru.otus.DBService;
import ru.otus.MsgGetUserIdAnswer;
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
        long id = dbService.getUserId(login);
        messageSystem.sendMessage(new MsgGetUserIdAnswer(getTo(), getFrom(), login, id));
    }
}
