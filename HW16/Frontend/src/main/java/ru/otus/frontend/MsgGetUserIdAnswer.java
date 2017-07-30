package ru.otus.frontend;

import ru.otus.messageSystem.Address;

public class MsgGetUserIdAnswer extends MsgToFrontend {
    private final String name;
    private final long id;

    public MsgGetUserIdAnswer(Address from, Address to, String name, long id) {
        super(from, to);
        this.name = name;
        this.id = id;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.addUser(id, name);
    }
}
