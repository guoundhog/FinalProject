package com.example.finalproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.util.HashSet;
import java.util.Set;

public class Level1Controller {

    @FXML
    private AnchorPane root;

    @FXML
    private Group world;

    private Player mario;
    private ImageView goal;

    private final Set<KeyCode> keys = new HashSet<>();

    private double cameraX = 0;

    public void initialize() {

        createMap();
        createPlayer();
        createGoal();
        setupKeyboard();
        gameLoop();
    }

    private void createPlayer() {

        mario = new Player();

        // 角色起始位置
        mario.setLayoutX(100);

        // 讓角色一開始直接站在地板上
        mario.setLayoutY(GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT);

        world.getChildren().add(mario);
    }

    private void createGoal() {

        Image img = new Image(getClass().getResourceAsStream("/image/goal.png"));

        goal = new ImageView(img);

        goal.setFitWidth(GameConfig.TILE_SIZE);
        goal.setFitHeight(GameConfig.TILE_SIZE);

        // 終點位置
        goal.setLayoutX(4000);

        // 讓終點站在地板上
        goal.setLayoutY(GameConfig.GROUND_Y - GameConfig.TILE_SIZE);

        world.getChildren().add(goal);
    }

    private void createMap() {

        Image dark = new Image(getClass().getResourceAsStream("/image/grass_dark.png"));
        Image light = new Image(getClass().getResourceAsStream("/image/grass_light.png"));

        for (int i = 0; i < 80; i++) {

            ImageView block;

            if (i % 2 == 0) {
                block = new ImageView(dark);
            } else {
                block = new ImageView(light);
            }

            block.setFitWidth(GameConfig.TILE_SIZE);
            block.setFitHeight(GameConfig.TILE_SIZE);

            block.setLayoutX(i * GameConfig.TILE_SIZE);
            block.setLayoutY(GameConfig.GROUND_Y);

            world.getChildren().add(block);
        }
    }

    private void setupKeyboard() {

        root.setFocusTraversable(true);

        Platform.runLater(() -> root.requestFocus());

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
            mario.setLayoutX(mario.getLayoutX() - GameConfig.MOVE_SPEED);
        }

        if (keys.contains(KeyCode.D)) {
            mario.setLayoutX(mario.getLayoutX() + GameConfig.MOVE_SPEED);
        }

        if (keys.contains(KeyCode.SPACE) && mario.onGround) {
            mario.velocityY = GameConfig.JUMP_POWER;
            mario.onGround = false;
        }
    }

    private void applyGravity() {

        mario.velocityY += GameConfig.GRAVITY;

        mario.setLayoutY(mario.getLayoutY() + mario.velocityY);

        // 地板碰撞判定
        if (mario.getLayoutY() >= GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT) {

            mario.setLayoutY(GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT);
            mario.velocityY = 0;
            mario.onGround = true;
        }
    }

    private void updateCamera() {

        // Mario 還沒走到指定位置前，鏡頭不動
        if (mario.getLayoutX() > GameConfig.CAMERA_START_X) {
            cameraX = mario.getLayoutX() - GameConfig.CAMERA_START_X;
        }

        // 只移動 world，不移動 root
        world.setLayoutX(-cameraX);
    }

    private void checkWin() {

        if (mario.getBoundsInParent().intersects(goal.getBoundsInParent())) {
            SceneManager.switchScene("win.fxml");
        }
    }
}