package ru.otus.example;

import ru.otus.util.CSVutil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String pathToFile = "/example.csv";
        char csvSplitBy = ';';

        CSVutil csVutil = new CSVutil();

        List<String> strings = csVutil.readFromCSV(pathToFile, csvSplitBy);

        for (String s : strings) {
            System.out.println(s);
        }
    }
}