package ru.otus.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheInfoServlet extends HttpServlet {
    private static final String ACCESS_DENIED_PAGE_TEMPLATE = "access_denied.html";
    private static final String CACHE_INFO_PAGE_TEMPLATE = "cache_info.html";

    private static final String HARDCODED_LOGIN = "admin";
    private static final String HARDCODED_PASSWORD = "12345";

    private int[] cacheStats;

    public CacheInfoServlet(int[] cacheStats) {
        this.cacheStats = cacheStats;
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
            Map<String, Object> pageVariables = createPageVariablesMap(request);

            response.getWriter().println(TemplateProcessor.instance().getPage(CACHE_INFO_PAGE_TEMPLATE, pageVariables));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("hit_count", cacheStats[0]);
        pageVariables.put("miss_count", cacheStats[1]);
        pageVariables.put("element_count", cacheStats[2]);

        return pageVariables;
    }

}
