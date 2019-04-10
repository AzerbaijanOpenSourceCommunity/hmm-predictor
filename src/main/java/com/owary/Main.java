/* Developers: Yusif Aliyev & Mensur Qulami */

package com.owary;

import com.owary.controller.Main_VC;
import com.owary.model.Main_Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/EditorView.fxml"));
        loader.setControllerFactory(t -> new Main_VC(new Main_Model()));
        Parent content = loader.load();

        Scene scene = new Scene(content);

        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //new Texter().run();
        launch(args);
    }
}
