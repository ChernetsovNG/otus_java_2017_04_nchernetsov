package ru.otus.util;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVutil {

    public static List<String> readFromCSV(String pathToFile, char csvSplitBy) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(pathToFile), csvSplitBy);
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
