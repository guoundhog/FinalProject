package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends Entity {

    boolean alive = true;

    public Enemy(double x, double y) {
        super(x, y, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);

        velocityX = -2;
        velocityY = 0;

        Image img = new Image(getClass().getResourceAsStream("/image/mario.jpg"));
        view = new ImageView(img);
        view.setFitWidth(GameConfig.PLAYER_WIDTH);
        view.setFitHeight(GameConfig.PLAYER_HEIGHT);
    }
}
