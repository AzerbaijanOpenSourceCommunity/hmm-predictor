package com.owary.textparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {

    public static void writeToCSV(List<WordPair> pairs){
        try (PrintWriter writer = new PrintWriter(new File("src/main/resources/output_"+System.currentTimeMillis()+".csv"))) {

            StringBuilder sb = new StringBuilder();
            sb.append("word,");
            sb.append("next,");
            sb.append("pair_count,");
            sb.append("word_count");
            sb.append('\n');

            for (WordPair pair : pairs) {
                sb.append(pair.getWord().getText());
                sb.append(",");
                sb.append(pair.getNext().getText());
                sb.append(",");
                sb.append(pair.getPairOccurred());
                sb.append(",");
                sb.append(pair.getOccurred());
                sb.append("\n");
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeToFile(List<String> sentences){
        try (PrintWriter writer = new PrintWriter(new File("src/main/resources/sent_"+System.currentTimeMillis()+".txt"))) {

            StringBuilder sb = new StringBuilder();

            for (String sentence : sentences) {
                sb.append(sentence.trim());
                sb.append("\n");
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
