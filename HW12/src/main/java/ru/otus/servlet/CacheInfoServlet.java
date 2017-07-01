package ru.otus.servlet;

import ru.otus.dbService.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheInfoServlet extends HttpServlet {
    private static final String ACCESS_DENIED_PAGE_TEMPLATE = "access_denied.html";
    private static final String CACHE_INFO_PAGE_TEMPLATE = "cache_info.html";

    private static final String HARDCODED_LOGIN = "admin";
    private static final String HARDCODED_PASSWORD = "12345";

    private DBService dbService;

    public CacheInfoServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createCacheVariablesMap();
        pageVariables.put("time", getTime());

        response.getWriter().println(TemplateProcessor.instance().getPage("server-data.json", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(AuthServlet.LOGIN_PARAMETER_NAME);
        String password = request.getParameter(AuthServlet.PASSWORD_PARAMETER_NAME);

        if (!login.equals(HARDCODED_LOGIN) || !password.equals(HARDCODED_PASSWORD)) {
            response.getWriter().println(TemplateProcessor.instance().getPage(ACCESS_DENIED_PAGE_TEMPLATE, null));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
        } else {
            Map<String, Object> pageVariables = createCacheVariablesMap();
            pageVariables.put("time", getTime());

            response.getWriter().println(TemplateProcessor.instance().getPage(CACHE_INFO_PAGE_TEMPLATE, pageVariables));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private Map<String, Object> createCacheVariablesMap() {
        Map<String, Object> pageVariables = new HashMap<>();

        int[] cacheStats = dbService.getCacheStats();

        pageVariables.put("hit_count", cacheStats[0]);
        pageVariables.put("miss_count", cacheStats[1]);
        pageVariables.put("element_count", cacheStats[2]);

        return pageVariables;
    }

    private static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

}
