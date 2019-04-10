package com.owary.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Main_Model {

    private ObservableList list = FXCollections.observableArrayList();
    private ArrayList<String> data = new ArrayList<String>(Arrays.asList("Riyaziyyat", "Elektrik", "Mühəndislik", "Kompyuter", "Xəzər"));

    public void loadData(ListView listView) {
        list.removeAll();
        list.addAll(data);

        listView.getItems().addAll(list);
    }

}
