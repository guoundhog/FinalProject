package com.example.finalproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void switchScene(String fxml) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            SceneManager.class.getResource("/fxml/" + fxml)
                    );

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}