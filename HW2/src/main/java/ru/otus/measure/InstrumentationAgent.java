package ru.otus.measure;

import java.lang.instrument.Instrumentation;

public class InstrumentationAgent {
    static int bitInByte = 8;
    static int ByteInKb = 1024;
    static int byteInMb = ByteInKb * 1024;

    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void printObjectSizeByte(final Object object) {
        System.out.println("Object: " + object + " of type: " + object.getClass() +
                " has size of: " + getObjectSize(object) + " bytes\n");
    }

    public static void printObjectSizeMb(final Object object) {
        System.out.println("Object: " + object + " of type: " + object.getClass() +
                " has size of: " + ((double) getObjectSize(object))/byteInMb + " Mb\n");
    }

    public static long getObjectSize(final Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return instrumentation.getObjectSize(object);
    }
}
