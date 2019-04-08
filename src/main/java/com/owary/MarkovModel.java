package com.owary;

import java.util.*;
import java.util.stream.Collectors;

import static com.owary.textparser.TextParser.*;

public class MarkovModel {

    private static Map<String, Long> FirstPossibleWords = new HashMap<>();
    private static Map<String, List<String>> SecondPossibleWords = new HashMap<>();
    private static Map<List<String>, List<String>> Transitions = new HashMap<>();

    private static Map<String, Double> FirstPossibleWordsProbabilities = new HashMap<>();
    private static Map<String, Map<String, Double>> SecondPossibleWordsProbabilities = new HashMap<>();
    private static Map<List<String>, Map<String, Double>> TransitionsProbabilities = new HashMap<>();

    public MarkovModel() {
        train();
    }

    public List<String> nextWord(String...provided){
        List<String> result = null;

        String word1 = null, word2 = null;
        int length = provided.length;

        if (length > 2){
            word1 = provided[length - 2].trim();
            word2 = provided[length - 1].trim();
        }else if(length == 2) {
            word1 = provided[0].trim();
            word2 = provided[1].trim();
        }else if (length == 1){
            word1 = provided[0].trim();
        }

        Map<String, Double> stringDoubleMap = null;

        // the first word
        if (word1 != null && word2 == null){
            stringDoubleMap = SecondPossibleWordsProbabilities.get(word1);
            if (stringDoubleMap != null) {
                result = getResult(stringDoubleMap);
            }
        }
        // all set
        if (word1 != null && word2 != null ){
            stringDoubleMap = TransitionsProbabilities.get(getList(word1, word2));
            if (stringDoubleMap != null){
                result = getResult(stringDoubleMap);
            }
            if (result == null || result.isEmpty()) {
                stringDoubleMap = SecondPossibleWordsProbabilities.get(word2);
                if (stringDoubleMap != null) {
                    result = getResult(stringDoubleMap);
                    System.out.println("herrr" + result);
                }
            }
        }

        return result != null ? result : new ArrayList<>();
    }

    public List<String> getResult(Map<String, Double> stringDoubleMap){
        return stringDoubleMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
    }

    public void train() {
        try {

            String filename = "test_train.txt";
            String string = getString(filename);
            String clean = clean(string);
            List<String> sentences = getSentences(clean);

            for (int i = 0; i < sentences.size(); i++) {
                process(getWords(sentences.get(i)));
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void process(List<String> strings) {
        int len = strings.size();
        for (int i = 0; i < len; i++) {
            String token = strings.get(i);

            if (i==0) {
                FirstPossibleWords.computeIfPresent(token, (k, v) -> v + 1);
                FirstPossibleWords.putIfAbsent(token, 1L);
            } else {
                String previousToken = strings.get(i - 1);

                if (i == len - 1) {
                    addToTransition(previousToken, token, "<END>");
                }

                if (i == 1) {
                    addToSecond(previousToken, token);
                } else {
                    String previousPreviousToken = strings.get(i - 2);
                    addToTransition(previousPreviousToken, previousToken, token);
                }
            }
        }

        Double firstWordsTotal = FirstPossibleWords.values()
                .stream()
                .mapToDouble(Long::doubleValue)
                .sum();

        for (Map.Entry<String, Long> entry : FirstPossibleWords.entrySet()) {
            Double value = entry.getValue() / firstWordsTotal;
            FirstPossibleWordsProbabilities.put(entry.getKey(), value);
        }

        FirstPossibleWords.clear();

        for (Map.Entry<String, List<String>> entry : SecondPossibleWords.entrySet()) {
            SecondPossibleWordsProbabilities.put(entry.getKey(), getNextProbability(entry.getValue()));
        }

        SecondPossibleWords.clear();

        for (Map.Entry<List<String>, List<String>> entry : Transitions.entrySet()) {
            TransitionsProbabilities.put(entry.getKey(), getNextProbability(entry.getValue()));
        }

        Transitions.clear();

    }

    public Map<String, Double> getNextProbability(List<String> words){
        Map<String, Long> wordCounts = new HashMap<>();
        for (String word : words) {
            wordCounts.putIfAbsent(word, 0L);
            wordCounts.computeIfPresent(word, (k, v) -> v + 1);
        }

        Map<String, Double> probs = new HashMap<>();
        double len = words.size();

        for (Map.Entry<String, Long> entry : wordCounts.entrySet()) {
            probs.put(entry.getKey(), entry.getValue() / len);
        }

        return probs;
    }

    private void addToSecond(String previousToken, String token) {
        List<String> valAbsent = getList(token);

        SecondPossibleWords.putIfAbsent(previousToken, valAbsent);
        SecondPossibleWords.computeIfPresent(previousToken, (k, v) -> {
            ArrayList<String> temp = new ArrayList<>(v);
            temp.add(token);
            return temp;
        });
    }

    public void addToTransition (String key1, String key2, String value) {
        List<String> key = getList(key1, key2);
        List<String> val = getList(value);

        List<String> list = Transitions.get(key);
        if (list != null ){
            list.add(value);
        }else{
            Transitions.putIfAbsent(key, val);
        }
    }

    public List<String> getList(String...val){
        return new ArrayList<>(Arrays.asList(val));
    }








}
