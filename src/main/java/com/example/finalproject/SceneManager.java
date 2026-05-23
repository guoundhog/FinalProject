package com.example.finalproject;

import javafx.application.Platform;
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

            // 固定場景大小
            Scene scene = new Scene(
                    root,
                    GameConfig.WINDOW_WIDTH,
                    GameConfig.WINDOW_HEIGHT
            );

            stage.setScene(scene);

            // 讓視窗依照 scene 重新計算大小
            stage.sizeToScene();

            stage.show();

            // 強制 JavaFX 下一幀重新排版
            Platform.runLater(() -> {
                stage.sizeToScene();
                root.requestLayout();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}