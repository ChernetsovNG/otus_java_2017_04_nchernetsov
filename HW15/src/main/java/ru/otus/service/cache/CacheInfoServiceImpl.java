package ru.otus.service.cache;

import ru.otus.app.CacheInfoService;
import ru.otus.app.DBService;
import ru.otus.app.MessageSystemContext;
import ru.otus.messageSystem.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CacheInfoServiceImpl implements CacheInfoService, Addressee {
    private final Address address;
    private final MessageSystemContext context;

    private static final String DEFAULT_PATTERN = "HH.mm.ss";

    private final String pattern;

    private final DBService dbService;

    private final int answerWaitTimeMs = 10;
    private int[] cacheStats = new int[3];
    private volatile boolean isCacheStatsWasWritten = false;

    public CacheInfoServiceImpl(MessageSystemContext context, DBService dbService, Address address) {
        this.dbService = dbService;
        this.context = context;
        this.address = address;
        this.pattern = DEFAULT_PATTERN;
    }

    public void init() {
        this.context.getMessageSystem().addAddressee(this);
        // запускаем систему обработки сообщений
        context.getMessageSystem().start();
    }

    public CacheInfoServiceImpl(MessageSystemContext context, Address address, DBService dbService, String pattern) {
        this.context = context;
        this.address = address;
        this.dbService = dbService;
        this.pattern = pattern;

        this.context.getMessageSystem().addAddressee(this);
    }

    public Map<String, Object> createCacheVariablesMap() {
        Map<String, Object> pageVariables = new HashMap<>();

        Message message = new MsgGetCacheInfo(context.getMessageSystem(), getAddress(), context.getDbAddress());
        context.getMessageSystem().sendMessage(message);

        // Приостанавливаем поток до тех пор, пока не придёт ответ от системы сообщений
        while (!isCacheStatsWasWritten) {
            try {
                TimeUnit.MILLISECONDS.sleep(answerWaitTimeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pageVariables.put("hit_count", cacheStats[0]);
        pageVariables.put("miss_count", cacheStats[1]);
        pageVariables.put("element_count", cacheStats[2]);

        isCacheStatsWasWritten = false;

        return pageVariables;
    }

    public String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    @Override
    public void putCacheStats(int[] cacheStats) {
        this.cacheStats = cacheStats;
        isCacheStatsWasWritten = true;
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
