package com.owary.textparser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class TextParser {

    private static Map<Character, String> mappings;

    static {
        List<Character> abcCyr =  Arrays.asList(' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        mappings = new HashMap<>();

        for (int i = 0; i < abcCyr.size(); i++) {
            mappings.put(abcCyr.get(i), abcLat[i]);
        }
    }

    public static List<String> getSentences(String string){
        String[] split = string.split("\\.");
        return Arrays.asList(split);
    }

    public static String getString(String filename) throws IOException {
        InputStream inputStream = TextParser.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream==null) throw new NullPointerException("InputStream is null");
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(in)) {
            while ((line = bufferedReader.readLine()) != null) {
                out.append(transliterate(line));
            }
        }
        return out.toString();
    }

    public static String transliterate(String message){
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (mappings.containsKey(c)){
                builder.append(mappings.get(c));
            }else{
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String clean(String input){
        String string;
        string  = input.toLowerCase();
        string  = string.replaceAll("[0-9]+", "");
        string  = string.replaceAll("-[a-zA-Zəıöü]+", "");
        string  = string.replaceAll("\\s+[a-zA-Z\\.]+\\.(az|com|org|io|ru|info|biz)", "");
        string  = string.replaceAll("[@#^&)(,%;:\\-\\–_\\/]", " ");
        string  = string.replaceAll("\"([^\"]*)\"", "");
        string  = string.replaceAll("“([^”]*)”", "");
        string  = string.replaceAll("«([^»]*)»", "");
        string  = string.replaceAll("[\"”“]", "");
        string  = string.replaceAll("\\s+", " ");
        string  = string.replaceAll("[!?]", ".");
        return string;
    }

    public static List<String> getWords(String input){
        String sentence = input;//.replaceAll("^[a-zA-ZəğüöƏĞÖÜıIİ]", "");
        String[] split = sentence.split("\\s+");
        List<String> words = Arrays.asList(split);
        return words
                .stream()
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .filter(word -> word.length() > 1)
                .map(e -> new String(e.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))
                .collect(toList());
    }

    private static String getInName(String filename){
        String folder = "dumps";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        List<File> files = Arrays.asList(new File(path).listFiles());
        Long aLong = files.stream()
                .map(File::getName)
                .filter(e -> e.contains(filename))
                .map(TextParser::parseLong)
                .map(Number::longValue)
                .max(Long::compareTo)
                .get();
        return String.format("src/main/resources/dumps/%s_%d", filename, aLong);
    }

    private static Long parseLong(String string){
        String[] split = string.split("_");
        String s = split[1];
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            return -1L;
        }
    }
}
