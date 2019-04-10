package com.owary.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;

public class Main_VC implements Initializable {

    private com.owary.model.Main_Model model;

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //listView.setFixedCellSize(255);
        model.loadData(listView);
    }

    public Main_VC(com.owary.model.Main_Model model) {
        this.model = model;
    }
}
