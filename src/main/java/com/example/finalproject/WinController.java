package com.example.finalproject;

public class WinController {

    public void playAgain() {

        SceneManager.switchScene("level1.fxml");
    }

    public void backMenu() {

        SceneManager.switchScene("menu.fxml");
    }
}