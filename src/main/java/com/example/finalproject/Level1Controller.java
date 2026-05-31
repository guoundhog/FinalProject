package com.example.finalproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level1Controller {

    // ================= FXML 元件 =================
    @FXML
    private AnchorPane root;

    // world 代表遊戲世界，攝影機移動時只移動 world
    @FXML
    private Group world;

    // ================= 遊戲物件 =================
    private Player mario;
    private List<Enemy> enemies = new ArrayList<>();
    private ImageView goal;
    private ImageView background;

    // ================= 音效與音樂 =================
    private MediaPlayer bgmPlayer;
    private MediaPlayer jumpPlayer;
    private MediaPlayer landingPlayer;

    // ================= 設定選單 =================
    private VBox settingPane;
    private boolean settingOpen = false;
    private AnimationTimer timer;
    private boolean gameFinished = false;

    // ================= FPS 顯示 =================
    private Label fpsLabel;
    private long lastTime = 0;
    private int frames = 0;

    // ================= 鍵盤輸入 =================
    private final Set<KeyCode> keys = new HashSet<>();

    // ================= 攝影機 =================
    private double cameraX = 0;

    // ================= FPS 限制 =================
    // 40 FPS = 1 秒 40 幀
    // 每幀間隔 = 1_000_000_000 / 40 ns
    private final long FRAME_INTERVAL = 1_000_000_000L / 40;

    // 上一幀時間
    private long lastFrameTime = 0;

    // ================= 地圖資料 =================
    // 0 = 空氣
    // 1 = 地板
    // 2 = 石頭
    // 3 = 終點

    private final int[][] map = {
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,2,0,0,0,2,2,2,2,2,0,0,0,2,2,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,3,0,0,2},
            {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2}
    };

    // ================= 初始化 =================
    public void initialize() {
        createBackground();
        createMap();
        createPlayer();
        createEnemy();
        createFPSCounter();
        setupMusic();
        createSettingPane();
        setupKeyboard();
        gameLoop();
        fixLayerOrder();
    }

    // ================= 背景 =================
    private void createBackground() {
        Image img = new Image(getClass().getResourceAsStream("/image/background.jpg"));
        background = new ImageView(img);
        background.setFitWidth(GameConfig.WINDOW_WIDTH);
        background.setFitHeight(GameConfig.WINDOW_HEIGHT);
        // 背景加到 root，才不會跟著地圖移動
        root.getChildren().add(background);
    }

    // ================= 音樂與音效 =================
    private void setupMusic() {
        Media bgm = new Media(getClass().getResource("/sound/bgm.mp3").toExternalForm());
        bgmPlayer = new MediaPlayer(bgm);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.5);
        bgmPlayer.play();

        Media jump = new Media(getClass().getResource("/sound/jump.mp3").toExternalForm());
        jumpPlayer = new MediaPlayer(jump);
        jumpPlayer.setVolume(0.7);

        Media landing = new Media(getClass().getResource("/sound/pipe.mp3").toExternalForm());
        landingPlayer = new MediaPlayer(landing);
        landingPlayer.setVolume(0.7);
    }

    private void playJumpSound() {
        if (jumpPlayer == null) {
            return;
        }
        // 先 stop 再 play，避免連續跳躍時音效播不出來
        jumpPlayer.stop();
        jumpPlayer.play();
    }

    private void playLandingSound() {
        if (landingPlayer == null) {
            return;
        }
        // 先 stop 再 play，避免連續落地時音效播不出來
        landingPlayer.stop();
        landingPlayer.play();
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

        // FPS 加到 root，才不會跟著地圖移動
        root.getChildren().add(fpsLabel);
    }

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

    // ================= 設定選單 =================
    private void createSettingPane() {
        Text title = new Text("Setting");
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-size: 24px;");

        Text bgmText = new Text("BGM Volume");
        bgmText.setFill(Color.WHITE);

        Slider bgmSlider = new Slider(0, 1, 0.5);

        bgmSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            bgmPlayer.setVolume(newValue.doubleValue());
        });



        Text jumpText = new Text("Jump Volume");
        jumpText.setFill(Color.WHITE);

        Slider jumpSlider = new Slider(0, 1, 0.7);

        jumpSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            jumpPlayer.setVolume(newValue.doubleValue());
        });

        settingPane = new VBox(15);
        settingPane.getChildren().addAll(
                title,
                bgmText,
                bgmSlider,
                jumpText,
                jumpSlider
        );

        settingPane.setLayoutX(GameConfig.WINDOW_WIDTH / 2.0 - 150);
        settingPane.setLayoutY(GameConfig.WINDOW_HEIGHT / 2.0 - 120);
        settingPane.setPrefWidth(300);
        settingPane.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.75);" +
                        "-fx-padding: 30px;" +
                        "-fx-background-radius: 15px;"
        );

        // 一開始先隱藏，按 ESC 才顯示
        settingPane.setVisible(false);

        root.getChildren().add(settingPane);
    }

    private void toggleSettingPane() {
        settingOpen = !settingOpen;
        settingPane.setVisible(settingOpen);

        // 關閉設定選單後，把鍵盤焦點還給 root
        // 不然角色可能收不到 A、D、SPACE
        if (!settingOpen) {
            Platform.runLater(() -> root.requestFocus());
        }
    }



    // ================= 建立主角 =================
    private void createPlayer() {
        // 主角起始 X 座標，讓角色直接站在地板上
        mario = new Player(100, GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT);

        world.getChildren().add(mario.view);
    }

    // ================= 建立敵人 =================
    private void createEnemy() {
        // 主角起始 X 座標，讓角色直接站在地板上
        Enemy enemy = new Enemy(960, 448);
        enemies.add(enemy);

        world.getChildren().add(enemy.view);
    }

    // ================= 建立地圖 =================
    private void createMap() {
        Image grass = new Image(getClass().getResourceAsStream("/image/grass.jpg"));

        Image stone = new Image(getClass().getResourceAsStream("/image/stone0.png"));

        // 產生一直線地板
        // 逐列掃描地圖
        for (int row = 0; row < map.length; row++) {
            // 逐行掃描地圖
            for (int col = 1; col < map[row].length - 1; col++) {
                // 取得目前格子的代號
                int tile = map[row][col];

                ImageView block = null;

                // ================= 地板 =================
                // 深綠地板
                if (tile == 1) {
                    block = new ImageView(grass);
                }

                // 淺綠地板
                else if (tile == 2) {
                    block = new ImageView(stone);
                }

                // ================= 終點 =================
                else if (tile == 3) {
                    Image img = new Image(
                            getClass().getResourceAsStream("/image/goal.png")
                    );

                    goal = new ImageView(img);
                    goal.setFitWidth(GameConfig.TILE_SIZE);
                    goal.setFitHeight(GameConfig.TILE_SIZE);
                    goal.setLayoutX(col * GameConfig.TILE_SIZE);
                    goal.setLayoutY(row * GameConfig.TILE_SIZE);
                    world.getChildren().add(goal);
                }

                // ================= 放置地板 =================
                if (block != null) {
                    block.setFitWidth(GameConfig.TILE_SIZE);
                    block.setFitHeight(GameConfig.TILE_SIZE);
                    // col = X 座標
                    block.setLayoutX((col - 1) * GameConfig.TILE_SIZE - 3);
                    // row = Y 座標
                    block.setLayoutY(row * GameConfig.TILE_SIZE);

                    world.getChildren().add(block);
                }
            }
        }
    }



    // ================= 鍵盤設定 =================
    private void setupKeyboard() {
        root.setFocusTraversable(true);

        // 等畫面建立完成後再取得焦點，避免一開始鍵盤沒反應
        Platform.runLater(() -> root.requestFocus());

        root.setOnKeyPressed(e -> {
            // ESC 開關設定選單
            if (e.getCode() == KeyCode.ESCAPE) {
                toggleSettingPane();
                return;
            }

            keys.add(e.getCode());
        });

        root.setOnKeyReleased(e -> {
            //鬆開SPACE 視為該次跳躍結束
            if (e.getCode() == KeyCode.SPACE) {
                mario.jumpLevel = 4;
            }
            keys.remove(e.getCode());
        });
    }



    // ================= 遊戲主迴圈 =================
    private void gameLoop() {
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                // FPS 限制
                if (now - lastFrameTime < FRAME_INTERVAL) {return;}
                lastFrameTime = now;
                update();
            }
        };
        timer.start();
    }



    // ================= 每幀更新 =================
    private void update() {
        handleInput();
        updateEntity(mario);
        for (Entity e : enemies){
            updateEntity(e);
        }
        updateCamera();
        mario.render();
        for (Entity e : enemies){
            e.render();
        }
        checkWin();
        updateFPS();
    }



    // ================= 讀取鍵盤輸入 =================
    private void handleInput() {
        // A 鍵向左加速，Mario 不會超出左邊界
        if (keys.contains(KeyCode.A) && mario.x > 3) {
            mario.velocityX -= GameConfig.ACCELERATION;
        }

        // D 鍵向右加速，Mario 不會超出地圖右邊界
        if (keys.contains(KeyCode.D) && mario.x < map[0].length * GameConfig.TILE_SIZE - GameConfig.PLAYER_WIDTH - 3) {
            mario.velocityX += GameConfig.ACCELERATION;
        }

        // 沒有按左右鍵時，慢慢減速，產生滑行感
        if (!keys.contains(KeyCode.A) && !keys.contains(KeyCode.D)) {

            if (mario.velocityX > 0) {
                mario.velocityX -= GameConfig.FRICTION;

                if (mario.velocityX < GameConfig.FRICTION) {
                    mario.velocityX = 0;
                }
            }

            if (mario.velocityX < 0) {
                mario.velocityX += GameConfig.FRICTION;

                if (mario.velocityX > -GameConfig.FRICTION) {
                    mario.velocityX = 0;
                }
            }
        }

        // SPACE 跳躍，利用 jumpLevel 實現長按大跳
        if (keys.contains(KeyCode.SPACE) && (mario.onGround || (mario.jumpLevel > 0 && mario.jumpLevel < 4))) {
            if (mario.jumpLevel == 0){
                playJumpSound();
            }
            mario.velocityY = GameConfig.JUMP_POWER;
            mario.jumpLevel++;
            mario.onGround = false;
        }
    }

    // ================= Entity移動 =================
    private void updateEntity(Entity entity) {
        moveX(entity);
        moveY(entity);
    }

    private void moveX(Entity entity) {
        // 限制最大水平速度
        if (entity.velocityX > GameConfig.maxVelocityX) {
            entity.velocityX = GameConfig.maxVelocityX;
        }

        if (entity.velocityX < -GameConfig.maxVelocityX) {
            entity.velocityX = -GameConfig.maxVelocityX;
        }

        // 套用水平速度
        entity.x += entity.velocityX;

        // 方塊碰撞檢測
        handleXCollision(entity);
    }

    private void moveY(Entity entity) {
        // 套用速度
        entity.y += entity.velocityY;
        // 持續增加向下速度，達到一定程度時停止加速
        //速度上限之後要整活後可以移除
        if (entity.velocityY < GameConfig.maxVelocityY) {
            entity.velocityY += GameConfig.GRAVITY;
        }

        // 方塊碰撞檢測
        handleYCollision(entity);
    }



    // ================= 碰撞檢測 =================
    private boolean isSolid(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= map[0].length || tileY >= map.length){
            return false;
        }

        return (map[tileY][tileX] == 1) || (map[tileY][tileX] == 2);
    }

    private void handleXCollision(Entity entity) {
        // 更新 mapX mapY 座標
        int left = (int) (entity.x / GameConfig.TILE_SIZE);
        int right = (int) ((entity.x + GameConfig.PLAYER_WIDTH - 1) / GameConfig.TILE_SIZE);
        int top = (int) (entity.y / GameConfig.TILE_SIZE);
        int bottom = (int)((entity.y + GameConfig.PLAYER_HEIGHT - 1) / GameConfig.TILE_SIZE);

        //遍歷周圍格子
        for (int ty = top; ty <= bottom; ty++){
            if (isSolid(left, ty) && entity.velocityX < 0){
                entity.x = (left + 1) * GameConfig.TILE_SIZE;
                if (entity instanceof Enemy enemy){
                    enemy.velocityX *= -1;
                }else {
                    entity.velocityX = 0;
                }
            }else if (isSolid(right, ty) && entity.velocityX > 0) {
                entity.x = right * GameConfig.TILE_SIZE - GameConfig.PLAYER_WIDTH;
                if (entity instanceof Enemy enemy){
                    enemy.velocityX *= -1;
                }else {
                    entity.velocityX = 0;
                }
            }
        }
    }

    private void handleYCollision(Entity entity) {
        // 更新 mapX mapY 座標
        int left = (int) (entity.x / GameConfig.TILE_SIZE);
        int right = (int) ((entity.x + GameConfig.PLAYER_WIDTH - 1) / GameConfig.TILE_SIZE);
        int top = (int) (entity.y / GameConfig.TILE_SIZE);
        int bottom = (int)((entity.y + GameConfig.PLAYER_HEIGHT - 1) / GameConfig.TILE_SIZE);

        // 避免空中跳躍，可刪
        if (entity instanceof Player player){
            player.onGround = false;
        }

        //遍歷周圍格子
        for (int tx = left; tx <= right; tx++){
            if (isSolid(tx, top) && entity.velocityY < 0){
                entity.y = (top + 1) * GameConfig.TILE_SIZE;
                entity.velocityY = 0;
                if (entity instanceof Player player){
                    player.jumpLevel = 4;
                }
            }else if (isSolid(tx, bottom) && entity.velocityY > 0){
                entity.y = bottom * GameConfig.TILE_SIZE - GameConfig.PLAYER_HEIGHT;
                entity.velocityY = 0;
                if (entity instanceof Player player){
                    player.jumpLevel = 0;
                }
                entity.onGround = true;
                playLandingSound();
            }
        }
    }


    // ================= 攝影機系統 =================
    private void updateCamera() {
        // Mario 尚未走到指定位置前，鏡頭不移動

        //稍微讓Mario 可以左右移動，可用可不用
        if (cameraX < mario.x - 380 && cameraX < map[0].length*GameConfig.TILE_SIZE - GameConfig.WINDOW_WIDTH -6) {
            cameraX += Math.max((mario.x - 380 - cameraX) * 0.3, 2);
        } else if (cameraX > mario.x - 370 && cameraX > 0) {
            cameraX += Math.min((mario.x - 370 - cameraX) * 0.3, -2);
        }

        // 只移動遊戲世界，不移動 root
        world.setLayoutX(-cameraX);
    }



    // ================= 終點判定 =================
    private void checkWin() {
        // 主角碰到終點後切換到勝利畫面
        if (!gameFinished && mario.view.getBoundsInParent().intersects(goal.getBoundsInParent())) {
            gameFinished = true;

            if (timer != null) {
                timer.stop();
            }

            if (bgmPlayer != null) {
                bgmPlayer.stop();
            }

            SceneManager.switchScene("win.fxml");
        }
    }



    // ================= 圖層順序 =================
    private void fixLayerOrder() {
        // 背景放最底層
        background.toBack();
        // 遊戲物件在背景上面
        world.toFront();
        // FPS 與設定選單在最上層
        fpsLabel.toFront();
        settingPane.toFront();
    }
}