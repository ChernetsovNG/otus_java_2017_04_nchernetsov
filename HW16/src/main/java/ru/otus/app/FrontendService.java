package ru.otus.app;

public interface FrontendService {
    void init();

    void handleRequest(String login);

    void addUser(int id, String name);
}

