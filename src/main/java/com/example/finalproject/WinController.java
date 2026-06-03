package com.example.finalproject;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.*;

public class WinController {
    @FXML
    private Button btnPlayAgain;
    @FXML
    private Button btnbackMenu;
    private MediaPlayer winBGMPlayer;
    public void initialize() {
        // 可以在這裡添加一些初始化邏輯，例如設置按鈕事件等

        Media winBGM = new Media(getClass().getResource("/sound/ocean.mp3").toExternalForm());
        winBGMPlayer = new MediaPlayer(winBGM);
        winBGMPlayer.setVolume(0.7);
        winBGMPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        winBGMPlayer.play();
    }
    public void playAgain() {
        String lastScene = SceneManager.getLastGameFXML();

        if (lastScene != null) {
            winBGMPlayer.stop();
            SceneManager.switchScene(lastScene);
        }
    }

    public void backMenu() {
        winBGMPlayer.stop();
        SceneManager.switchScene("menu.fxml");
    }
}