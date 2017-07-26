package ru.otus.messageSystem.message;

import ru.otus.messageSystem.Address;
import ru.otus.service.CacheInfoService;

public class MsgGetCacheInfoAnswer extends MsgToService {
    private final int[] cacheStats;

    public MsgGetCacheInfoAnswer(Address from, Address to, int[] cacheStats) {
        super(from, to);
        this.cacheStats = cacheStats;
    }

    @Override
    public void exec(CacheInfoService cacheInfoService) {
        cacheInfoService.putCacheStats(cacheStats);
    }
}
