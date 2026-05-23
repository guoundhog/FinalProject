package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {

    public double velocityY = 0;

    public double maxVelocityY = -GameConfig.JUMP_POWER;

    public int jumpLevel = 0;

    public Player() {

        Image img =
                new Image(getClass().getResourceAsStream("/image/menu.jpg"));

        setImage(img);

        setFitWidth(50);
        setFitHeight(50);
    }
}