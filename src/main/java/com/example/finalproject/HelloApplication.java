package com.example.finalproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception  {
        SceneManager.setStage(stage);
        SceneManager.switchScene("menu.fxml");
        stage.show();
    }
//我是白癡
    public static void main(String[] args) {
        launch();
    }
}
