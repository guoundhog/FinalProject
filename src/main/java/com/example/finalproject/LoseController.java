package com.example.finalproject;

public class LoseController {

    public void playAgain() {
        SceneManager.switchScene("level1.fxml");
    }

    public void backMenu() {
        SceneManager.switchScene("menu.fxml");
    }
}