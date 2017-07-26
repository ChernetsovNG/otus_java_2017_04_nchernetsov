package ru.otus.service.cache;

import ru.otus.app.CacheInfoService;
import ru.otus.messageSystem.Address;
import ru.otus.app.MsgToService;

public class MsgGetCacheInfoAnswer extends MsgToService {
    private final int[] cacheStats;

    MsgGetCacheInfoAnswer(Address from, Address to, int[] cacheStats) {
        super(from, to);
        this.cacheStats = cacheStats;
    }

    @Override
    public void exec(CacheInfoService cacheInfoService) {
        cacheInfoService.putCacheStats(cacheStats);
    }
}
