package com.example.finalproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Level1Controller {
    @FXML
    private Text level1;

    @FXML
    private Rectangle blackBackground;

    private Timeline showLevel;

    @FXML
    public void initialize() {
        showLevel = new Timeline(new KeyFrame(Duration.millis(1000), e -> showScene()));
        showLevel.setCycleCount(1);
        showLevel.play();
    }

    @FXML
    public void showScene(){
        level1.setOpacity(0);
        blackBackground.setOpacity(0);
    }
}
