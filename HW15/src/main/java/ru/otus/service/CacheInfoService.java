package ru.otus.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheInfoService {
    private static final String DEFAULT_PATTERN = "HH.mm.ss";

    private final String pattern;

    private final DBService dbService;

    public CacheInfoService(DBService dbService) {
        this.dbService = dbService;
        this.pattern = DEFAULT_PATTERN;
    }

    public CacheInfoService(DBService dbService, String pattern) {
        this.dbService = dbService;
        this.pattern = pattern;
    }

    public Map<String, Object> createCacheVariablesMap() {
        Map<String, Object> pageVariables = new HashMap<>();

        int[] cacheStats = dbService.getCacheStats();

        pageVariables.put("hit_count", cacheStats[0]);
        pageVariables.put("miss_count", cacheStats[1]);
        pageVariables.put("element_count", cacheStats[2]);

        return pageVariables;
    }

    public String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
}
