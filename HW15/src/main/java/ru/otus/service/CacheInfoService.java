package ru.otus.service;

import ru.otus.messageSystem.*;
import ru.otus.messageSystem.message.MsgGetCacheInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheInfoService implements Addressee {
    private final Address address;
    private final MessageSystemContext context;

    private static final String DEFAULT_PATTERN = "HH.mm.ss";

    private final String pattern;

    private final DBService dbService;

    private int[] cacheStats = new int[3];
    private volatile boolean isCacheStatsWasWritten = false;

    public CacheInfoService(MessageSystemContext context, DBService dbService, Address address) {
        this.dbService = dbService;
        this.context = context;
        this.address = address;
        this.pattern = DEFAULT_PATTERN;

        this.context.getMessageSystem().addAddressee(this);
    }

    public CacheInfoService(MessageSystemContext context, Address address, DBService dbService, String pattern) {
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

        // Здесь надо приостановить поток, пока не придёт ответ от системы сообщений!
        while (!isCacheStatsWasWritten) {
            try {
                Thread.sleep(10);
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

    public void putCacheStats(int[] cacheStats) {
        this.cacheStats = cacheStats;
        isCacheStatsWasWritten = true;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void messageSystemStart() {
        context.getMessageSystem().start();
    }
}
