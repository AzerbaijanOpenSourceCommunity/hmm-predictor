package com.owary.textparser;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String filename = "string.txt";
        List<WordPair> parsed = TextParser.parse(filename);
        CsvWriter.writeToCSV(parsed);
    }

}
