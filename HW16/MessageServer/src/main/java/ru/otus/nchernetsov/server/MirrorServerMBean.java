package ru.otus.nchernetsov.server;

public interface MirrorServerMBean {
    boolean getRunning();

    void setRunning(boolean running);
}
