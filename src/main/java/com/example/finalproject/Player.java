package com.example.finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends Entity {
    public Image actorImage;
    public Image actorJumpImage;

    public boolean facingRight = true;
    public int jumpLevel;

    public Player(double x, double y) {
        super(x, y, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);

        actorImage = new Image(getClass().getResourceAsStream("/image/actor.png"));
        actorJumpImage = new Image(getClass().getResourceAsStream("/image/actorJump.png"));

        view = new ImageView(actorImage);
        view.setFitWidth(GameConfig.PLAYER_WIDTH);
        view.setFitHeight(GameConfig.PLAYER_HEIGHT);
    }
}