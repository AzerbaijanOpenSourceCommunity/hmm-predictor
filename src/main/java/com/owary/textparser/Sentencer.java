package com.owary.textparser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.owary.textparser.TextParser.*;

public class Sentencer {

    public static void main(String[] args) throws IOException {
        String filename = "samples.txt";
        String string = getString(filename);
        String clean = clean(string);
        List<String> sentences = getSentences(clean);
        List<String> words = tokenize(sentences);
        CsvWriter.writeToFile(words);
    }

    public static List<String> tokenize(List<String> sentences){
        return sentences
                .stream()
                .map(TextParser::getWords)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
