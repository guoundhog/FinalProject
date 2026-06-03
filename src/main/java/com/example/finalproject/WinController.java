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