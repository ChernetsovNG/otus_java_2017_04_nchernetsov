package ru.otus.app;

import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messageSystem.Message;

public abstract class MsgToService extends Message {
    public MsgToService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof CacheInfoService) {
            exec((CacheInfoService) addressee);
        }
    }

    public abstract void exec(CacheInfoService cacheInfoService);
}
