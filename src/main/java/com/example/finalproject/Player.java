package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {

    public double velocityY = 0;

    public boolean onGround = false;

    public Player() {

        Image img =
                new Image(getClass().getResourceAsStream("/image/mario.jpg"));

        setImage(img);

        setFitWidth(50);
        setFitHeight(50);
    }
}