package com.owary.model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.owary.textparser.TextParser.*;

public class MarkovModel implements Serializable {

    private static final String STRING_LOCATION = "src/main/resources/serialized.model";

    private Map<String, Long> FirstPossibleWords = new HashMap<>();
    private Map<String, List<String>> SecondPossibleWords = new HashMap<>();
    private Map<List<String>, List<String>> Transitions = new HashMap<>();

    private Map<String, Double> FirstPossibleWordsProbabilities = new HashMap<>();
    private Map<String, Map<String, Double>> SecondPossibleWordsProbabilities = new HashMap<>();
    private Map<List<String>, Map<String, Double>> TransitionsProbabilities = new HashMap<>();

    /**
     * Model Constructor
     */
    public MarkovModel() {

    }

    /**
     * Returns a trained model
     * If model already exists as a serialized file, it will load it, otherwise it will re-train the model.
     * @return trained model instance
     */
    public static MarkovModel getTrainedModel(){
        MarkovModel model = null;
        System.out.println("Checking for serialized file...");
        if (fileExists(STRING_LOCATION)) {
            System.out.println("File is found...");
            model = loadFromFile();
        }else{
            System.out.println("File is not found...");
        }
        if (model == null){
            System.out.println("File is corrupted, training will be repeated");
            model = getTrainedInstance();
            System.out.println("Model has been trained...");
        }
        return model;
    }

    /**
     * Based on the provided words array (1 or 2 words), the method returns next words list, based on their probabilities sorted
     * @param provided input array
     * @return list of words
     */
    public List<String> nextWord(String...provided){
        // result list
        List<String> result = null;

        // words 1. and 2.
        String word1 = null, word2 = null;
        // length of the array
        int length = provided.length;

        // if there are more than 2 words provided, we choose the last 2
        if (length > 2){
            word1 = provided[length - 2].trim();
            word2 = provided[length - 1].trim();
        // if exactly 2 words are given, then select them
        }else if(length == 2) {
            word1 = provided[0].trim();
            word2 = provided[1].trim();
        // if there's only one word (so the first word in the sentence)
        }else if (length == 1){
            word1 = provided[0].trim();
        }

        // Word : Probability pair map
        Map<String, Double> stringDoubleMap;

        // if only the first word is set
        if (word1 != null && word2 == null){
            // so, this means sentence has just started and no words before our current word1
            // we get the result from SecondPossibleWordsProbabilities map, because it is where we store it
            stringDoubleMap = SecondPossibleWordsProbabilities.get(word1);
            // if it is available
            if (stringDoubleMap != null) {
                // get the list
                result = getResult(stringDoubleMap);
            }
        }
        // all set
        if (word1 != null && word2 != null ){
            // find next words based on the (word1, word2) pair
            stringDoubleMap = TransitionsProbabilities.get(getList(word1, word2));
            if (stringDoubleMap != null){
                // get result list
                result = getResult(stringDoubleMap);
            }
            // if result is null or empty
            if (result == null || result.isEmpty()) {
                // then search for the last word in the SecondPossibleWordsProbabilities map
                stringDoubleMap = SecondPossibleWordsProbabilities.get(word2);
                if (stringDoubleMap != null) {
                    // get the result
                    result = getResult(stringDoubleMap);
                }
            }
        }
        // if not null return the result, otherwise an empty array
        return result != null ? result : new ArrayList<>();
    }

    /**
     * getResult sorts the keys by their values, then returns keys as a list
     * @param stringDoubleMap
     * @return
     */
    private List<String> getResult(Map<String, Double> stringDoubleMap){
        return stringDoubleMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
    }

    /**
     * Training the Markov Model
     */
    public void train() {
        try {
            System.out.println("Training started...");
            // the name of the file
            String filename = "test_train.txt";
            // get the whole string of the file
            String string = getString(filename);
            // clean the string
            String clean = clean(string);
            // get the individual sentences as a list
            List<String> sentences = getSentences(clean);

            // process each sentence's words
            for (int i = 0; i < sentences.size(); i++) {
                process(getWords(sentences.get(i)));
            }

            System.out.println("Training ended...");
        }catch (Exception ex){
            System.out.println("Error occurred while training");
            ex.printStackTrace();
        }
    }

    /**
     * main processing method
     * @param strings list of words
     */
    private void process(List<String> strings) {
        // list size
        int len = strings.size();
        // for each word in the list
        for (int i = 0; i < len; i++) {
            // get the corresponding word
            String token = strings.get(i);

            // if its position is the first
            if (i==0) {
                // add it to the map and set the counter to 1, or if it is already there increment by 1
                FirstPossibleWords.computeIfPresent(token, (k, v) -> v + 1);
                FirstPossibleWords.putIfAbsent(token, 1L);
            // if it is not the first word
            } else {
                // get the previous token/word
                String previousToken = strings.get(i - 1);

                // if we are at the last word
                if (i == len - 1) {
                    // add to the transition map, and mark the ending as a period (.) meaning the sentence has ended
                    // pair is (previousToken, token) and the following is period (.)
                    addToTransition(previousToken, token, ". ");
                }
                // if we are at the second position
                if (i == 1) {
                    // add (previousToken, token) pair to the SecondPossibilities map
                    addToSecond(previousToken, token);
                // if not
                } else {
                    // get the previous previous token, then
                    // (prevPrevToken, prevToken) -> token to the transition map
                    String previousPreviousToken = strings.get(i - 2);
                    addToTransition(previousPreviousToken, previousToken, token);
                }
            }
        }

        // after persisting to the maps finish
        // finding the probabilities for the First possible words
        Double firstWordsTotal = FirstPossibleWords
                .values() // get values
                .stream() // stream
                .mapToDouble(Long::doubleValue) // convert them to double
                .sum(); // sum

        // now, divide each entry count to total count and find the probability
        for (Map.Entry<String, Long> entry : FirstPossibleWords.entrySet()) {
            Double value = entry.getValue() / firstWordsTotal;
            FirstPossibleWordsProbabilities.put(entry.getKey(), value);
        }
        // finding probability for the Second possible words
        for (Map.Entry<String, List<String>> entry : SecondPossibleWords.entrySet()) {
            SecondPossibleWordsProbabilities.put(entry.getKey(), getNextProbability(entry.getValue()));
        }

        // finding probability for the transitions
        for (Map.Entry<List<String>, List<String>> entry : Transitions.entrySet()) {
            TransitionsProbabilities.put(entry.getKey(), getNextProbability(entry.getValue()));
        }

    }

    /**
     * Method calculates the probability of words in a list <br/>
     * Basically, it counts the words
     * Then divide those counts to the list size
     * @param words
     * @return
     */
    private Map<String, Double> getNextProbability(List<String> words){
        Map<String, Long> wordCounts = new HashMap<>();
        for (String word : words) {
            wordCounts.computeIfPresent(word, (k, v) -> v + 1);
            wordCounts.putIfAbsent(word, 1L);
        }

        Map<String, Double> probs = new HashMap<>();
        double len = words.size();

        for (Map.Entry<String, Long> entry : wordCounts.entrySet()) {
            probs.put(entry.getKey(), entry.getValue() / len);
        }

        return probs;
    }

    /**
     * Adds to the SecondPossibleWords map
     * @param previousToken
     * @param token
     */
    private void addToSecond(String previousToken, String token) {
        List<String> valAbsent = getList(token);

        SecondPossibleWords.putIfAbsent(previousToken, valAbsent);
        SecondPossibleWords.computeIfPresent(previousToken, (k, v) -> {
            ArrayList<String> temp = new ArrayList<>(v);
            temp.add(token);
            return temp;
        });
    }

    /**
     * Adds to the Transitions array
     * @param key1
     * @param key2
     * @param value
     */
    private void addToTransition (String key1, String key2, String value) {
        List<String> key = getList(key1, key2);
        List<String> val = getList(value);

        List<String> list = Transitions.get(key);
        if (list != null ){
            list.add(value);
        }else{
            Transitions.putIfAbsent(key, val);
        }
    }

    // returns an array list from an array
    private List<String> getList(String...val){
        return new ArrayList<>(Arrays.asList(val));
    }

    /**
     * Serializes the object
     */
    private void serialize(){
        try {
            System.out.println("Saving model...");
            FileOutputStream fileOut = new FileOutputStream(STRING_LOCATION);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Saved in src/main/resources/serialized.model");
        } catch (IOException i) {
            System.out.println("Problem occurred while saving the model, cause "+i.getMessage());
        }
    }

    /**
     * Returns a trained instance by loading it from a file
     * @return trained model
     */
    private static MarkovModel loadFromFile(){
        MarkovModel model = null;
        try {
            FileInputStream fileIn = new FileInputStream(STRING_LOCATION);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            model = (MarkovModel) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return model;
    }

    /**
     * Returns a trained instance by training it
     * @return trained model
     */
    private static MarkovModel getTrainedInstance(){
        // train on model initiation
        MarkovModel model = new MarkovModel();
        model.train();
        model.serialize();
        return model;
    }

    /**
     * Checks if file exists in the filesystem
     * @param file
     * @return
     */
    private static boolean fileExists(String file){
        return new File(file).exists();
    }

    public Map<String, Long> getFirstPossibleWords() {
        return FirstPossibleWords;
    }
}
