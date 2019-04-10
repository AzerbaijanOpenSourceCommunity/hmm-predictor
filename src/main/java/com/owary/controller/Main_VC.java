package com.owary.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Main_VC implements Initializable {

    private com.owary.model.Main_Model model;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //model.loadData(textArea, listView);

    }

    @FXML
    public void onButtonPressed(){
        model.loadData(textArea, listView);
    }

    public Main_VC(com.owary.model.Main_Model model) {
        this.model = model;
    }


}
