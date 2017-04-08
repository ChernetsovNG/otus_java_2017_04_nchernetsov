package ru.otus.measure;

import java.lang.instrument.Instrumentation;

public class InstrumentationAgent {

    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain...");
        instrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("agentmain...");
        instrumentation = inst;
    }

    public static void printSize(final Object object) {
        System.out.println("Object of type: " + object.getClass() +
                " has size of: " + getObjectSize(object) + " bytes");
    }

    public static long getObjectSize(final Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return instrumentation.getObjectSize(object);
    }
}
