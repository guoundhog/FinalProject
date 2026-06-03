package com.example.finalproject;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.*;

public class LoseController {
    @FXML
    private Button btnPlayAgain;
    @FXML
    private Button btnbackMenu;
    private MediaPlayer gameOverBGMPlayer;
    public void initialize() {
        // 設定按鈕事件

        Media gameOverBGM = new Media(getClass().getResource("/sound/nima.mp3").toExternalForm());
        gameOverBGMPlayer = new MediaPlayer(gameOverBGM);
        gameOverBGMPlayer.setVolume(0.7);
        gameOverBGMPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        gameOverBGMPlayer.play();
    }

    public void playAgain() {
        String lastScene = SceneManager.getLastGameFXML();

        if (lastScene != null) {
            gameOverBGMPlayer.stop();
            SceneManager.switchScene(lastScene);
        }
    }

    public void backMenu() {
        gameOverBGMPlayer.stop();
        SceneManager.switchScene("menu.fxml");
    }
}