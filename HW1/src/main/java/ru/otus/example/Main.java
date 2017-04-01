package ru.otus.example;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String pathToFile = "HW1/src/main/resources/example.csv";
        char cvsSplitBy = ';';

        CSVReader reader = new CSVReader(new FileReader(pathToFile), cvsSplitBy);

        String[] strings;

        while ((strings = reader.readNext()) != null) {
            for (String s : strings) {
                if (s.equals(""))  //пустые строки пропускаем
                    continue;
                System.out.println(s.trim());
            }
        }

    }
}