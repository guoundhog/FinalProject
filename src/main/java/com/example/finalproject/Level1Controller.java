package com.example.finalproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.util.HashSet;
import java.util.Set;

public class Level1Controller {

    // ================= FXML 元件 =================
    @FXML
    private AnchorPane root;
    // world 代表整個遊戲世界
    // 攝影機移動時只移動 world
    @FXML
    private Group world;

    // ================= 遊戲物件 =================
    // 主角 Mario
    private Player mario;
    // 終點方塊
    private ImageView goal;

    // ================= FPS 顯示 =================
    // 左上角 FPS Label
    private Label fpsLabel;
    // 上次更新 FPS 的時間
    private long lastTime = 0;
    // 一秒內經過幾幀
    private int frames = 0;

    // ================= 鍵盤輸入 =================
    // 用來記錄目前按住哪些鍵
    private final Set<KeyCode> keys = new HashSet<>();

    // ================= 攝影機 =================
    // 攝影機目前位置
    private double cameraX = 0;

    // ================= 初始化 =================
    public void initialize() {
        createMap();
        createPlayer();
        createGoal();
        createFPSCounter();
        setupKeyboard();
        gameLoop();
    }

    // ================= FPS 顯示 =================

    private void createFPSCounter() {

        fpsLabel = new Label("FPS: 0");

        fpsLabel.setStyle("-fx-text-fill: white;" +
                          "-fx-font-size: 20px;" +
                          "-fx-background-color: black;");

        // FPS 固定在畫面左上角
        fpsLabel.setLayoutX(20);
        fpsLabel.setLayoutY(20);

        // 注意：
        // FPS 要加到 root
        // 才不會跟著地圖一起移動
        root.getChildren().add(fpsLabel);
    }

    // ================= 建立主角 =================

    private void createPlayer() {
        mario = new Player();
        // 主角起始 X 座標
        mario.setLayoutX(100);
        // 讓角色直接站在地板上
        mario.setLayoutY(GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT);
        world.getChildren().add(mario);
    }

    // ================= 建立終點 =================
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

    // ================= 建立地圖 =================
    private void createMap() {
        // 深綠地板
        Image dark =
                new Image(getClass().getResourceAsStream("/image/grass_dark.png"));
        // 淺綠地板
        Image light =new Image(getClass().getResourceAsStream("/image/grass_light.png"));
        // 產生一直線地板
        for (int i = 0; i < 80; i++) {
            ImageView block;
            // 深淺交錯
            if (i % 2 == 0) {
                block = new ImageView(dark);
            }
            else {
                block = new ImageView(light);
            }

            // 方塊大小
            block.setFitWidth(GameConfig.TILE_SIZE);
            block.setFitHeight(GameConfig.TILE_SIZE);

            // 地板位置
            block.setLayoutX(i * GameConfig.TILE_SIZE);

            block.setLayoutY(GameConfig.GROUND_Y);

            world.getChildren().add(block);
        }
    }



    // ================= 鍵盤設定 =================
    private void setupKeyboard() {

        // root 可以接收鍵盤輸入
        root.setFocusTraversable(true);
        // 延遲取得焦點
        // 避免一開始無法操作
        Platform.runLater(() -> root.requestFocus());
        // 按下按鍵
        root.setOnKeyPressed(e -> keys.add(e.getCode()));
        // 放開按鍵
        root.setOnKeyReleased(e -> keys.remove(e.getCode()));
    }

        // ================= 遊戲主迴圈 =================
    private void gameLoop() {

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    // ================= 每幀更新 =================
    private void update() {
        movePlayer();
        applyGravity();
        updateCamera();
        checkWin();
        updateFPS();
    }

    // ================= FPS 更新 =================
    private void updateFPS() {

        frames++;
        long now = System.nanoTime();

        // 每秒更新一次 FPS
        if (now - lastTime >= 1_000_000_000L) {
            fpsLabel.setText("FPS: " + frames);
            frames = 0;
            lastTime = now;
        }
    }

    // ================= 玩家移動 =================
    private void movePlayer() {
        // A 鍵向左
        if (keys.contains(KeyCode.A)) {
            mario.setLayoutX(mario.getLayoutX() - GameConfig.MOVE_SPEED);
        }

        // D 鍵向右
        if (keys.contains(KeyCode.D)) {

            mario.setLayoutX(mario.getLayoutX() + GameConfig.MOVE_SPEED);
        }

        // SPACE 跳躍
        // 必須站在地板上才能跳
        if (keys.contains(KeyCode.SPACE) && mario.onGround) {

            mario.velocityY = GameConfig.JUMP_POWER;
            mario.onGround = false;
        }
    }

    // ================= 重力系統 =================
    private void applyGravity() {

        // 持續增加向下速度
        mario.velocityY += GameConfig.GRAVITY;

        // 套用速度
        mario.setLayoutY(mario.getLayoutY() + mario.velocityY);

        // 地板碰撞判定
        if (mario.getLayoutY() >= GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT) {

            // 強制站回地板
            mario.setLayoutY(GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT);
            // 停止下落
            mario.velocityY = 0;
            // 設定為站在地板上
            mario.onGround = true;
        }
    }


    // ================= 攝影機系統 =================
    private void updateCamera() {

        // Mario 尚未走到指定位置前
        // 鏡頭不移動
        if (mario.getLayoutX() > GameConfig.CAMERA_START_X) {

            cameraX = mario.getLayoutX() - GameConfig.CAMERA_START_X;
        }

        // 只移動遊戲世界
        // 不移動 root
        world.setLayoutX(-cameraX);
    }

    // ================= 終點判定 =================

    private void checkWin() {

        // 主角碰到終點
        if (mario.getBoundsInParent().intersects(goal.getBoundsInParent())) {
            SceneManager.switchScene("win.fxml");
        }
    }
}