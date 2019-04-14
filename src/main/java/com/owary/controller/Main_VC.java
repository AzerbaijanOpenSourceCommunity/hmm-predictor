package com.owary.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE) {

                model.loadData(textArea, listView);

                System.out.println("Space Key Pressed");
            }
        });
    }

    public Main_VC(com.owary.model.Main_Model model) {
        this.model = model;
    }


}
