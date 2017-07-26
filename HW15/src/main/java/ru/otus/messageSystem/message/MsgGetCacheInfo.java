package ru.otus.messageSystem.message;

import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;
import ru.otus.service.DBService;

public class MsgGetCacheInfo extends MsgToDB {
    private final MessageSystem messageSystem;

    public MsgGetCacheInfo(MessageSystem messageSystem, Address from, Address to) {
        super(from, to);
        this.messageSystem = messageSystem;
    }

    @Override
    public void exec(DBService dbService) {
        int[] cacheStats = dbService.getCacheStats();
        messageSystem.sendMessage(new MsgGetCacheInfoAnswer(getTo(), getFrom(), cacheStats));
    }
}
