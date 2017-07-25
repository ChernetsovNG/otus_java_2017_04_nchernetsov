package ru.otus.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthServlet extends HttpServlet {
    static final String LOGIN_PARAMETER_NAME = "login";
    private static final String LOGIN_VARIABLE_NAME = "login";
    static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String PASSWORD_VARIABLE_NAME = "password";

    private static final String AUTHORIZATION_PAGE_TEMPLATE = "auth.html";

    private String login;
    private String password;

    public void init() {
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String requestLogin = request.getParameter(LOGIN_PARAMETER_NAME);
        String requestPassword = request.getParameter(PASSWORD_PARAMETER_NAME);

        if (requestLogin != null && requestPassword != null) {
            saveToVariable(requestLogin, requestPassword);
            saveToSession(request, requestLogin, requestPassword);
        }

        String page = getPage(login, password); //save to the page
        response.getWriter().println(page);

        setOK(response);
    }

    private void saveToSession(HttpServletRequest request, String requestLogin, String requestPassword) {
        request.getSession().setAttribute("login", requestLogin);
        request.getSession().setAttribute("password", requestPassword);
    }

    private void saveToVariable(String requestLogin, String requestPassword) {
        login = requestLogin != null ? requestLogin : login;
        password = requestPassword != null ? requestPassword : password;
    }

    private void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static String getPage(String login, String password) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(LOGIN_VARIABLE_NAME, login == null ? "" : login);
        pageVariables.put(PASSWORD_VARIABLE_NAME, password == null ? "" : password);

        return TemplateProcessor.instance().getPage(AUTHORIZATION_PAGE_TEMPLATE, pageVariables);
    }
}
