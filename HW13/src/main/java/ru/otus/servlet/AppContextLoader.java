package ru.otus.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class AppContextLoader {
    static ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext("SpringBeans.xml");
    }
}
