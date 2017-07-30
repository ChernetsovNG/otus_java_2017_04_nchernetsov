package ru.otus.frontend;

public interface FrontendService {
    void init();

    void handleRequest(String login);

    void addUser(long id, String name);
}

