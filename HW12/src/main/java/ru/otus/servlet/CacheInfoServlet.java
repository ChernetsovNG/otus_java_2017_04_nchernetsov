package ru.otus.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheInfoServlet extends HttpServlet {
    private static final String CACHE_INFO_PAGE_TEMPLATE = "cache_info.html";

    private static final String DEFAULT_LOGIN = "unknown";
    private static final String DEFAULT_PASSWORD = "unknown password";

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        response.getWriter().println(TemplateProcessor.instance().getPage(CACHE_INFO_PAGE_TEMPLATE, pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        String login = request.getParameter(AuthServlet.LOGIN_PARAMETER_NAME);
        String password = request.getParameter(AuthServlet.PASSWORD_PARAMETER_NAME);

        pageVariables.put("login", login != null ? login : DEFAULT_LOGIN);
        pageVariables.put("password", password != null ? password : DEFAULT_PASSWORD);

        return pageVariables;
    }
}
