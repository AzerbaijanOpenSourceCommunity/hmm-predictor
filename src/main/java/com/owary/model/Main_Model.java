package com.owary.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;


public class Main_Model {

    private MarkovModel hmm = new MarkovModel();
    private ObservableList list = FXCollections.observableArrayList();

    public void loadData(TextArea textArea, ListView listView) {
        list.removeAll();

        String text = textArea.getText();

        String[] lastTwo = getLastTwo(text);

        List<String> arrayList = hmm.nextWord(lastTwo);

        list.addAll(arrayList);

        listView.getItems().addAll(list);
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
