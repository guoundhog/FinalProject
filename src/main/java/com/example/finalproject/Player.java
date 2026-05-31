package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {

    public double velocityY = 0;
    public double velocityX = 0;

    public int jumpLevel;
    public int left;
    public int right;
    public int top;
    public int bottom;

    public boolean onGround;

    public Player() {

        Image img = new Image(getClass().getResourceAsStream("/image/menu.jpg"));

        setImage(img);

        setFitWidth(GameConfig.PLAYER_WIDTH);
        setFitHeight(GameConfig.PLAYER_HEIGHT);
    }
}