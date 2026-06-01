package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends Entity {

    public int jumpLevel;

    public Player(double x, double y) {
        super(x, y, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);

        Image img = new Image(getClass().getResourceAsStream("/image/menu.jpg"));
        view = new ImageView(img);
        view.setFitWidth(GameConfig.PLAYER_WIDTH);
        view.setFitHeight(GameConfig.PLAYER_HEIGHT);
    }
}