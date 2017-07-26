package ru.otus.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.dataSets.AddressDataSet;
import ru.otus.dataSets.PhoneDataSet;
import ru.otus.dataSets.UserDataSet;
import ru.otus.service.cache.CacheInfoServiceImpl;
import ru.otus.app.DBService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class CacheInfoServlet extends HttpServlet {
    private static final String ACCESS_DENIED_PAGE_TEMPLATE = "access_denied.html";
    private static final String CACHE_INFO_PAGE_TEMPLATE = "cache_info.html";

    private static final String HARDCODED_LOGIN = "admin";
    private static final String HARDCODED_PASSWORD = "12345";

    private DBService dbService;
    private CacheInfoServiceImpl cacheInfoService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        initBeansFromServletContext(config);

        // for test purposes only
        for (int i = 1; i <= 6; i++) {
            UserDataSet userDataSet = new UserDataSet("User " + i,
                new AddressDataSet("Street " + i, i),
                Collections.singletonList(new PhoneDataSet(i, String.valueOf(i))));
            dbService.save(userDataSet);
        }
    }

    private void initBeansFromServletContext(ServletConfig config) {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());

        this.dbService = (DBService) context.getBean("dbService");
        this.cacheInfoService = (CacheInfoServiceImpl) context.getBean("cacheInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = cacheInfoService.createCacheVariablesMap();
        pageVariables.put("time", cacheInfoService.getTime());

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
            Map<String, Object> pageVariables = cacheInfoService.createCacheVariablesMap();
            pageVariables.put("time", cacheInfoService.getTime());

            response.getWriter().println(TemplateProcessor.instance().getPage(CACHE_INFO_PAGE_TEMPLATE, pageVariables));

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
