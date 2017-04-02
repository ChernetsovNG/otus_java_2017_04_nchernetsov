package ru.otus.example;

import ru.otus.util.CSVutil;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String pathToFile = "HW1/src/main/resources/example.csv";
        char csvSplitBy = ';';

        List<String> strings = CSVutil.readFromCSV(pathToFile, csvSplitBy);

        for (String s : strings) {
            System.out.println(s);
        }
    }
}