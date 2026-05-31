package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends ImageView {

    public double velocityX;
    public boolean dead;

    public Enemy(Image image) {
        super(image);

        velocityX = GameConfig.ENEMY_SPEED;
        dead = false;

        setFitWidth(GameConfig.ENEMY_WIDTH);
        setFitHeight(GameConfig.ENEMY_HEIGHT);
    }

    public void reverseDirection() {
        velocityX = -velocityX;
    }

    public void die() {
        dead = true;
    }
}