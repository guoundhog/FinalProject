package com.example.finalproject;
import javafx.scene.control.Button;
import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
        SceneManager.switchScene("menu.fxml");
    }
}