package com.example.finalproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.util.HashSet;
import java.util.Set;

public class Level1Controller {

    @FXML
    private AnchorPane root;

    private final double GRAVITY = 0.5;

    private final double MOVE_SPEED = 5;

    private final double JUMP_POWER = -12;

    private Player mario;

    private final Set<KeyCode> keys = new HashSet<>();

    private double cameraX = 0;

    private ImageView goal;

    public void initialize() {

        createMap();

        createPlayer();

        createGoal();

        setupKeyboard();

        gameLoop();
    }

    private void createPlayer() {

        mario = new Player();

        mario.setLayoutX(100);

        mario.setLayoutY(300);

        root.getChildren().add(mario);
    }

    private void createGoal() {

        Image img =
                new Image(getClass().getResourceAsStream("/image/goal.png"));

        goal = new ImageView(img);

        goal.setFitWidth(60);

        goal.setFitHeight(60);

        goal.setLayoutX(3000);

        goal.setLayoutY(440);

        root.getChildren().add(goal);
    }

    private void createMap() {

        Image dark =
                new Image(getClass().getResourceAsStream("/image/grass_dark.png"));

        Image light =
                new Image(getClass().getResourceAsStream("/image/grass_light.png"));

        for (int i = 0; i < 80; i++) {

            ImageView block;

            if (i % 2 == 0) {
                block = new ImageView(dark);
            } else {
                block = new ImageView(light);
            }

            block.setFitWidth(64);

            block.setFitHeight(64);

            block.setLayoutX(i * 64);

            block.setLayoutY(500);

            root.getChildren().add(block);
        }
    }

    private void setupKeyboard() {

        root.setFocusTraversable(true);
        Platform.runLater(() ->{
            root.requestFocus();
        });

        root.setOnKeyPressed(e -> keys.add(e.getCode()));

        root.setOnKeyReleased(e -> keys.remove(e.getCode()));
    }

    private void gameLoop() {

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                update();
            }
        };

        timer.start();
    }

    private void update() {

        movePlayer();

        applyGravity();

        updateCamera();

        checkWin();
    }

    private void movePlayer() {

        if (keys.contains(KeyCode.A)) {

            mario.setLayoutX(mario.getLayoutX() - MOVE_SPEED);
        }

        if (keys.contains(KeyCode.D)) {

            mario.setLayoutX(mario.getLayoutX() + MOVE_SPEED);
        }

        if (keys.contains(KeyCode.SPACE) && mario.onGround) {

            mario.velocityY = JUMP_POWER;

            mario.onGround = false;
        }
    }

    private void applyGravity() {

        mario.velocityY += GRAVITY;

        mario.setLayoutY(mario.getLayoutY() + mario.velocityY);

        if (mario.getLayoutY() >= 450) {

            mario.setLayoutY(450);

            mario.velocityY = 0;

            mario.onGround = true;
        }
    }

    private void updateCamera() {

        cameraX = mario.getLayoutX() - 400;

        root.setLayoutX(-cameraX);
    }

    private void checkWin() {

        if (mario.getBoundsInParent().intersects(goal.getBoundsInParent())) {

            SceneManager.switchScene("win.fxml");
        }
    }
}