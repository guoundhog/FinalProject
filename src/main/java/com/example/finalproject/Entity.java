package com.example.finalproject;

import javafx.scene.image.ImageView;

public class Entity {

    double x, y;
    double width, height;

    double velocityX, velocityY;

    boolean onGround;

    ImageView view;

    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void render() {
        view.setX(x);
        view.setY(y);
    }
}
