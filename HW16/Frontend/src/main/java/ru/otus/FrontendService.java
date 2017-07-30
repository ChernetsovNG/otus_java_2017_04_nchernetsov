package ru.otus;

public interface FrontendService {
    void init();

    void handleRequest(String login);

    void addUser(long id, String name);
}

