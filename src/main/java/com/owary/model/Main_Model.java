package com.owary.model;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;


public class Main_Model {

    private MarkovModel hmm = new MarkovModel();

    public void loadData(TextArea textArea, ListView listView) {

        String text = textArea.getText();

        String[] lastTwo = getLastTwo(text);

        List<String> arrayList = hmm.nextWord(lastTwo);

        listView.getItems().clear();

        listView.getItems().addAll(getFive(arrayList));
    }

    public List<String> getFive(List<String> list){
        int size = 5;
        if (list.size() < 5){
            size = list.size();
        }
        return list.subList(0, size);
    }

    public String[] getLastTwo(String input){
        String[] sentences = input.split("\\.");
        String[] split = sentences[sentences.length-1].trim().split("\\s+");
        int length = split.length;
        if (length >= 2){
            return new String[]{split[length-2], split[length-1]};
        }
        return split;
    }

}
