package ru.otus.server;

public interface MirrorServerMBean {
    boolean getRunning();

    void setRunning(boolean running);
}
