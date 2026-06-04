package com.example.finalproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        SceneManager.setStage(stage);

        stage.setTitle("Mario Final Project");

        // 先不要讓視窗大小亂變
        stage.setResizable(false);

        SceneManager.switchScene("menu.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}