package ru.otus.service.cache;

import ru.otus.app.DBService;
import ru.otus.app.MsgToDB;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;

public class MsgGetCacheInfo extends MsgToDB {
    private final MessageSystem messageSystem;

    MsgGetCacheInfo(MessageSystem messageSystem, Address from, Address to) {
        super(from, to);
        this.messageSystem = messageSystem;
    }

    @Override
    public void exec(DBService dbService) {
        int[] cacheStats = dbService.getCacheStats();
        messageSystem.sendMessage(new MsgGetCacheInfoAnswer(getTo(), getFrom(), cacheStats));
    }
}
