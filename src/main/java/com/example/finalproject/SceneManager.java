package com.example.finalproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
                    new FXMLLoader(SceneManager.class.getResource("/fxml/" + fxml));

            Parent root = loader.load();

            stage.setScene(new Scene(root, 800, 600));

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}