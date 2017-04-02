package ru.otus.util;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVutil {

    public List<String> readFromCSV(String pathToFile, char csvSplitBy) throws IOException {

        InputStream resourceFile = getClass().getResourceAsStream(pathToFile);
        CSVReader reader = new CSVReader(new InputStreamReader(resourceFile), csvSplitBy);

        List<String> result = new ArrayList<>();
        String[] strings;
        while ((strings = reader.readNext()) != null) {
            for (String s : strings) {
                if (s.equals(""))  //пустые строки пропускаем
                    continue;
                result.add(s.trim());
            }
        }
        return result;
    }

}
