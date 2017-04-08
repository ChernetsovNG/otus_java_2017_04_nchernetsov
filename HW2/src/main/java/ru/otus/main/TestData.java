package ru.otus.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ru.otus.measure.InstrumentationAgent.printObjectSizeByte;

class TestData {
    public TestData() {
    }

    public void printObjectsSize() {
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

        printObjectSizeByte(sb);
        printObjectSizeByte(falseBoolean);
        printObjectSizeByte(zeroInt);
        printObjectSizeByte(zeroDouble);
        printObjectSizeByte(zeroLong);
        printObjectSizeByte(zeroLongP);
        printObjectSizeByte(maxLong);
        printObjectSizeByte(maxLongP);
        printObjectSizeByte(minLongP);
        printObjectSizeByte(emptyString);
        printObjectSizeByte(string);
        printObjectSizeByte(strings);
        printObjectSizeByte(moreStrings);
        printObjectSizeByte(someStrings);
        printObjectSizeByte(bd);
        printObjectSizeByte(calendar);

        printObjectSizeByte(this);
    }

}
