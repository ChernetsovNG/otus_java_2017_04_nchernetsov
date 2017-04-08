package ru.otus.main;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ru.otus.measure.InstrumentationAgent.printSize;

public class Main {
    int bitInByte = 8;
    int ByteInKb = 1024;
    int byteInMb = ByteInKb * 1024;

    public static void main(String[] args) throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        final StringBuilder sb = new StringBuilder(1000);
        final boolean falseBoolean = false;
        final int zeroInt = 0;
        final double zeroDouble = 0.0;
        final Long zeroLong = 0L;
        final long zeroLongP = 0L;
        final Long maxLong = Long.MAX_VALUE;
        final Long minLong = Long.MIN_VALUE;
        final long maxLongP = Long.MAX_VALUE;
        final long minLongP = Long.MIN_VALUE;
        final String emptyString = "";
        final String string = "ToBeOrNotToBeThatIsTheQuestion";
        final String[] strings = {emptyString, string, "Dustin"};
        final String[] moreStrings = new String[1000];
        final List<String> someStrings = new ArrayList<>();
        final BigDecimal bd = new BigDecimal("999999999999999999.99999999");
        final Calendar calendar = Calendar.getInstance();

        System.out.println("Memory: " + (runtime.totalMemory() - runtime.freeMemory()));

        printSize(sb);
        printSize(falseBoolean);
        printSize(zeroInt);
        printSize(zeroDouble);
        printSize(zeroLong);
        printSize(zeroLongP);
        printSize(maxLong);
        printSize(maxLongP);
        printSize(minLongP);
        printSize(emptyString);
        printSize(string);
        printSize(strings);
        printSize(moreStrings);
        printSize(someStrings);
        printSize(bd);
        printSize(calendar);
    }
}

