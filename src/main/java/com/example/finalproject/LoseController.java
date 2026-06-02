package com.example.finalproject;

public class LoseController {

    public void playAgain() {
        String lastScene = SceneManager.getLastGameFXML();

        if (lastScene != null) {
            SceneManager.switchScene(lastScene);
        }
    }

    public void backMenu() {
        SceneManager.switchScene("menu.fxml");
    }
}