package com.example.finalproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {

        SceneManager.setStage(stage);

        SceneManager.switchScene("menu.fxml");

        stage.setTitle("Final Project");
    }

    public static void main(String[] args) {
        launch();
    }
}