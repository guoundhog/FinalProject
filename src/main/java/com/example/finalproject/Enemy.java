package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends Entity {

    boolean alive = true;

    public Enemy(double x, double y) {
        super(x, y, GameConfig.ENEMY_WIDTH, GameConfig.ENEMY_HEIGHT);
        velocityX = -GameConfig.ENEMY_SPEED;
        velocityY = 0;

        Image img = new Image(getClass().getResourceAsStream("/image/enemy.png"));
        view = new ImageView(img);
        view.setFitWidth(GameConfig.PLAYER_WIDTH);
        view.setFitHeight(GameConfig.PLAYER_HEIGHT);
        render();
    }
}
