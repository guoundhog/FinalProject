package com.example.finalproject;
import javafx.scene.control.Button;
import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
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

import java.util.*;

public class SpecialController {

    // ================= FXML 元件 =================
    @FXML
    private AnchorPane root;

    // world 代表遊戲世界，攝影機移動時只移動 world
    @FXML
    private Group world;

    // ================= 遊戲物件 =================
    private Player mario;
    private final List<Enemy> enemies = new ArrayList<>();
    private ImageView goal;
    private final Map<String, ImageView> breakableBlocks = new HashMap<>();
    private final Map<String, Integer> breakableBlockHp = new HashMap<>();

    private ImageView background;
    private Image stone0;
    private Image stone1;
    private Image stone2;
    private Image stone3;
    private Image dangerStone;
    private Image hardBlockImage;
    private Image specialBlockImage;
    private Image bridgeImage;

    // ================= 音效與音樂 =================
    private MediaPlayer bgmPlayer;
    private MediaPlayer jumpPlayer;
    private MediaPlayer landingPlayer;

    // ================= 設定選單 =================
    private VBox settingPane;
    private boolean settingOpen = false;
    private AnimationTimer timer;
    private boolean gameFinished = false;
    private int life = 2; // 生命數，目前先設 1，之後可以改成 3

    // ================= FPS 顯示 =================
    private Label fpsLabel;
    private Label distanceLabel;
    private Label lifeLabel;
    private long lastTime = 0;
    private int frames = 0;

    // ================= 鍵盤輸入 =================
    private final Set<KeyCode> keys = new HashSet<>();

    // ================= 攝影機 =================
    private double cameraX = GameConfig.TILE_SIZE;

    // ================= FPS 限制 =================
    // 40 FPS = 1 秒 40 幀
    // 每幀間隔 = 1_000_000_000 / 40 ns
    private final long FRAME_INTERVAL = 1_000_000_000L / 40;

    // 上一幀時間
    private long lastFrameTime = 0;

// 0 = 空氣
// 1 = 地板
// 4 = 石頭
// 9 = 終點
//ENEMY = 8;
//HARD_BLOCK = 5;
//SPECIAL_BLOCK = 6;
//BRIDGE = 7;

    private final int[][] map = {
            {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,5},
            {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,5,0,0,0,0,0,5},
            {5,0,0,0,0,0,0,0,0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,5},
            {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,9,0,0,5},
            {5,0,0,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,8,8,8,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,0,0,0,0,0,0,0,0,0,0,0,5,0,5,0,5,5,5,5,5,5,5},
            {5,0,0,0,0,0,0,0,5,5,5,5,0,11,0,0,0,0,0,0,0,0,5,7,7,5,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,0,0,0,0,0,0,0,0,5,0,5,0,0,0,0,0,0,0,0,0,5},
            {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,0,0,0,0,0,5,0,5,0,0,0,0,0,0,0,0,0,0,0,5},
            {5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,0,0,5,0,5,0,5,0,0,0,0,0,0,0,0,0,0,0,5},
            {5,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,5}
    };

    // ================= 初始化 =================
    public void initialize() {
        createBackground();
        createMap();
        createPlayer();
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
        fpsLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;"
        );

        // FPS 固定在畫面左上角
        fpsLabel.setLayoutX(20);
        fpsLabel.setLayoutY(50);

        root.getChildren().add(fpsLabel);


        // 距離
        distanceLabel = new Label("Distance: 0 m");

        distanceLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        distanceLabel.setLayoutX(20);
        distanceLabel.setLayoutY(20);

        root.getChildren().add(distanceLabel);


        // 生命
        lifeLabel = new Label("Life: " + life);

        lifeLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        lifeLabel.setLayoutX(GameConfig.WINDOW_WIDTH - 120);
        lifeLabel.setLayoutY(20);

        root.getChildren().add(lifeLabel);
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

    private void updateHUD() {

        // 64像素 = 1公尺
        int distance = (int)(mario.x / GameConfig.TILE_SIZE);
        distanceLabel.setText("Distance: " + distance + " m");
        lifeLabel.setText("Life: " + life);
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


        Text landText = new Text("land Volume");
        landText.setFill(Color.WHITE);
        Slider landSlider = new Slider(0, 1, 0.7);
        landSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            landingPlayer.setVolume(newValue.doubleValue());
        });


        settingPane = new VBox(15);
        Button backButton = new Button("Back To Menu");
        backButton.setOnAction(e -> backToMenu());

        settingPane.getChildren().addAll(
                title,
                bgmText,
                bgmSlider,
                jumpText,
                jumpSlider,
                landText,
                landSlider,
                backButton
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

    private void backToMenu() {
        gameFinished = true;

        if (timer != null) {timer.stop();}
        if (bgmPlayer != null) {bgmPlayer.stop();}

        SceneManager.switchScene("menu.fxml");
    }

    private void playerDead() {
        life--;

        if (life <= 0) {
            gameFinished = true;

            if (timer != null) {timer.stop();}
            if (bgmPlayer != null) {bgmPlayer.stop();}

            SceneManager.switchScene("lose.fxml");
        }
    }

    // ================= 建立主角 =================
    private void createPlayer() {
        // 主角起始 X 座標，讓角色直接站在地板上
        mario = new Player(100, GameConfig.GROUND_Y - GameConfig.PLAYER_HEIGHT);

        world.getChildren().add(mario.view);
    }

    // ================= 建立敵人 =================
    private void createEnemy(double x, double y) {
        Enemy enemy = new Enemy(x, y);
        enemies.add(enemy);
        world.getChildren().add(enemy.view);
    }

    // ================= 建立地圖 =================
    private void createMap() {
        Image grass = new Image(getClass().getResourceAsStream("/image/grass.jpg"));

        stone0 = new Image(getClass().getResourceAsStream("/image/stone0.png"));
        stone1 = new Image(getClass().getResourceAsStream("/image/stone1.png"));
        stone2 = new Image(getClass().getResourceAsStream("/image/stone2.png"));
        stone3 = new Image(getClass().getResourceAsStream("/image/stone3.png"));
        dangerStone = new Image(getClass().getResourceAsStream("/image/dangerStone.png"));
        hardBlockImage = new Image(getClass().getResourceAsStream("/image/hardBlock.png"));
        specialBlockImage = new Image(getClass().getResourceAsStream("/image/grass_light.png"));
        bridgeImage = new Image(getClass().getResourceAsStream("/image/bridge.png"));

        // 產生一直線地板
        // 逐列掃描地圖
        for (int row = 0; row < map.length; row++) {
            // 逐行掃描地圖
            for (int col = 0; col < map[row].length; col++) {
                // 取得目前格子的代號
                int tile = map[row][col];

                ImageView block = null;

                // ================= 地板 =================

                // 草地
                if (tile == GameConfig.GROUND) {
                    block = new ImageView(grass);
                }

                // 石頭
                else if (tile == GameConfig.STONE) {
                    block = new ImageView(stone0);
                    String key = row + "," + col;
                    breakableBlocks.put(key, block);
                    breakableBlockHp.put(key, GameConfig.STONE);
                }
                else if (tile == GameConfig.INVISIBLE_STONE) {
                    block = new ImageView();
                    String key = row + "," + col;
                    breakableBlocks.put(key, block);
                }
                else if (tile == GameConfig.DANGER_STONE) {
                    block = new ImageView(stone0);
                    String key = row + "," + col;
                    breakableBlocks.put(key, block);
                }
                else if (tile == GameConfig.HARD_BLOCK) {
                    block = new ImageView(hardBlockImage);
                }
                else if (tile == GameConfig.SPECIAL_BLOCK) {
                    block = new ImageView(specialBlockImage);
                    String key = row + "," + col;
                    breakableBlocks.put(key, block);
                }
                else if (tile == GameConfig.BRIDGE) {
                    block = new ImageView(bridgeImage);
                }
                else if (tile == GameConfig.ENEMY) {
                    createEnemy(col * GameConfig.TILE_SIZE, (row+1) * GameConfig.TILE_SIZE - GameConfig.ENEMY_HEIGHT) ;
//                    createEnemy(col * GameConfig.TILE_SIZE, (row) * GameConfig.TILE_SIZE-50);
//                    System.out.println("enemy created at " + col * GameConfig.TILE_SIZE + ", " + ( (row) * GameConfig.TILE_SIZE - GameConfig.ENEMY_HEIGHT));
                    map[row][col] = GameConfig.AIR;
                }
                else if (tile == GameConfig.GOAL) {
                    Image img = new Image(getClass().getResourceAsStream("/image/goal.png"));

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
                    block.setLayoutX(col * GameConfig.TILE_SIZE);

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

            if (e.getCode() == KeyCode.SPACE && !mario.onGround) {
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
        updatePlayerSprite();
        for (Entity e : enemies){
            updateEntity(e);
        }
        updateCamera();
        mario.render();
        for (Entity e : enemies){
            e.render();
        }
        checkEnemyCollision();
        checkWin();
        checkDeath();
        updateHUD();
        updateFPS();
        if(mario.jumpLevel >4){
            System.out.println(mario.jumpLevel);
        }

    }

    // ================= 讀取鍵盤輸入 =================
    private void handleInput() {
        // A 鍵向左加速，Mario 不會超出左邊界
        if (keys.contains(KeyCode.A)) {
            mario.facingRight = false;
            mario.velocityX -= GameConfig.ACCELERATION;
        }

        // D 鍵向右加速，Mario 不會超出地圖右邊界
        if (keys.contains(KeyCode.D)){
            mario.facingRight = true;
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
                mario.onGround = false;
                playJumpSound();
            }
            mario.velocityY = GameConfig.JUMP_POWER;
            mario.jumpLevel++;

        }
    }

    // ================= Entity移動 =================
    private void updateEntity(Entity entity) {
        moveX(entity);
        moveY(entity);
    }

    private void updatePlayerSprite() {

        // 空中
        if (!mario.onGround) {
            mario.view.setImage(mario.actorJumpImage);
        }

        // 地面
        else {
            mario.view.setImage(mario.actorImage);
        }

        // 左右翻轉
        if (!mario.facingRight) {
            mario.view.setScaleX(1);
        }
        else {
            mario.view.setScaleX(-1);
        }
    }

    private void hitBlockFromBelow(int row, int col) {
        if (map[row][col] == GameConfig.HARD_BLOCK) {
            mario.velocityY = 0;
//            mario.velocityY = -mario.velocityY;
            return;
        }

        if (map[row][col] == GameConfig.INVISIBLE_STONE) {
            mario.velocityY = 0;
            ImageView block = breakableBlocks.get(row + "," + col);
            block.setImage(stone0);
            return;
        }

        if (map[row][col] == GameConfig.DANGER_STONE) {
            mario.velocityY = 0;
            ImageView block = breakableBlocks.get(row + "," + col);
            block.setImage(dangerStone);
            return;
        }

        if (map[row][col] == GameConfig.SPECIAL_BLOCK) {
            spawnSpecialItem(row, col);
            map[row][col] = GameConfig.AIR;
            removeBlockAt(row, col);
            return;
        }

        if (map[row][col] != GameConfig.STONE) {
            return;
        }
        playBlockHitAnimation(row, col);
        String key = row + "," + col;

        int hp = breakableBlockHp.get(key);

        hp--;

        if (hp == 3) {
            breakableBlocks.get(key).setImage(stone1);
        }
        else if (hp == 2) {
            breakableBlocks.get(key).setImage(stone2);
        }
        else if (hp == 1) {
            breakableBlocks.get(key).setImage(stone3);
        }
        else {
            ImageView block = breakableBlocks.get(key);

            world.getChildren().remove(block);

            breakableBlocks.remove(key);
            breakableBlockHp.remove(key);

            map[row][col] = 0;
        }

        breakableBlockHp.put(key, hp);
    }

    private boolean isBridge(int col, int row) {
        // 超出地圖範圍就當成不能碰撞
        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return false;
        }
        // 代表可碰撞地板
        return map[row][col] == GameConfig.BRIDGE;
    }

    private void playBlockHitAnimation(int row, int col) {
        String key = row + "," + col;
        ImageView block = breakableBlocks.get(key);
        if (block == null) {return;}

        TranslateTransition up = new TranslateTransition(Duration.millis(70), block);
        up.setByY(-12);

        TranslateTransition down = new TranslateTransition(Duration.millis(90), block);
        down.setByY(12);

        up.setOnFinished(e -> down.play());
        up.play();
    }

    private void spawnSpecialItem(int row, int col) {
        ImageView item = new ImageView(new Image(getClass().getResourceAsStream("/image/special_item.png")));
        item.setFitWidth(GameConfig.TILE_SIZE);
        item.setFitHeight(GameConfig.TILE_SIZE);
        item.setLayoutX(col * GameConfig.TILE_SIZE);
        item.setLayoutY((row - 1) * GameConfig.TILE_SIZE);
        world.getChildren().add(item);
    }

    private void removeBlockAt(int row, int col) {
        String key = row + "," + col;
        ImageView block = breakableBlocks.get(key);

        if (block != null) {
            world.getChildren().remove(block);
            breakableBlocks.remove(key);
            breakableBlockHp.remove(key);
        }
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
        if (!entity.onGround && entity.velocityY < GameConfig.maxVelocityY) {
            entity.velocityY += GameConfig.GRAVITY;
        }
        // 方塊碰撞檢測
        handleYCollision(entity);
    }

    // ================= 碰撞檢測 =================
    private boolean isSolidTile(int col, int row) {
        // 超出地圖範圍就當成不能碰撞
        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return false;
        }
        // 代表可碰撞地板
        return map[row][col] == GameConfig.GROUND
                || map[row][col] == GameConfig.STONE
                || map[row][col] == GameConfig.INVISIBLE_STONE
                || map[row][col] == GameConfig.DANGER_STONE
                || map[row][col] == GameConfig.HARD_BLOCK
                || map[row][col] == GameConfig.SPECIAL_BLOCK;
    }

    private void handleXCollision(Entity entity) {
        // 更新 mapX mapY 座標
        int left = (int) (entity.x / GameConfig.TILE_SIZE);
        int right = (int) ((entity.x + GameConfig.PLAYER_WIDTH - 1) / GameConfig.TILE_SIZE);
        int top = (int) (entity.y / GameConfig.TILE_SIZE);
        int bottom = (int) ((entity.y + GameConfig.PLAYER_HEIGHT - 1) / GameConfig.TILE_SIZE);

        //遍歷周圍格子
        for (int ty = top; ty <= bottom; ty++){
            if (isSolidTile(left, ty) && entity.velocityX < 0){
                entity.x = (left + 1) * GameConfig.TILE_SIZE;
                if (entity instanceof Enemy enemy){
                    enemy.velocityX *= -1;
                }else {
                    entity.velocityX = 0;
                }
            }else if (isSolidTile(right, ty) && entity.velocityX > 0) {
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
        int left = (int) (entity.x / GameConfig.TILE_SIZE);
        int right = (int) ((entity.x + GameConfig.PLAYER_WIDTH - 1) / GameConfig.TILE_SIZE);
        int mid = (int) ((entity.x + GameConfig.PLAYER_WIDTH/2) / GameConfig.TILE_SIZE);
        int top = (int) (entity.y / GameConfig.TILE_SIZE);
        int bottom = (int) ((entity.y + GameConfig.PLAYER_HEIGHT - 1) / GameConfig.TILE_SIZE);

        // 往上撞
        if (entity.velocityY < 0) {
            if (isSolidTile(left, top) || isSolidTile(right, top)) {

                entity.y = (top + 1) * GameConfig.TILE_SIZE;
                entity.velocityY = 0;

                if (entity instanceof Player player) {

                    player.jumpLevel = 4;
                    if(isSolidTile(mid, top)) {
                        hitBlockFromBelow(top, mid);
                    }
                    else if (isSolidTile(left, top)) {
                        hitBlockFromBelow(top, left);
                    }
                    else {
                        hitBlockFromBelow(top, right);
                    }
                }
            }
            else if (!isSolidTile(left, bottom) && !isSolidTile(right, bottom)) {
                entity.onGround = false;
            }
        }

        // 往下落
        else if (entity.velocityY > 0) {
            if (isSolidTile(left, bottom) || isSolidTile(right, bottom) || isBridge(left, bottom) || isBridge(right, bottom)) {

                entity.y = bottom * GameConfig.TILE_SIZE - GameConfig.PLAYER_HEIGHT;
                entity.velocityY = 0;

                if (entity instanceof Player player) {
                    player.jumpLevel = 0;

                    // 只有真正落地瞬間播放一次
                    if (!player.onGround) {
                        playLandingSound();
                    }
                }
                entity.onGround = true;
            }
            else {
                entity.onGround = false;
            }
        }

        else {
            if (!(isSolidTile(left, bottom + 1) || isSolidTile(right, bottom + 1))) {

                entity.onGround = false;
            }
        }
    }

    // ================= 攝影機系統 =================
    private void updateCamera() {
        // Mario 尚未走到指定位置前，鏡頭不移動

        //稍微讓Mario 可以左右移動，可用可不用
        if (cameraX < mario.x - 380 && cameraX < (map[0].length - 1)*GameConfig.TILE_SIZE - GameConfig.WINDOW_WIDTH - GameConfig.maxVelocityX) {
            cameraX += Math.max((mario.x - 380 - cameraX) * 0.3, 2);
        } else if (cameraX > mario.x - 370 && cameraX > GameConfig.TILE_SIZE+GameConfig.maxVelocityX){
            cameraX += Math.min((mario.x - 370 - cameraX) * 0.3, -2);
        }

        // 只移動遊戲世界，不移動 root
        world.setLayoutX(-cameraX);
    }

    // ================= 終點判定 =================
    private void checkEnemyCollision() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);

            if (mario.view.getBoundsInParent().intersects(enemy.view.getBoundsInParent())) {
                double marioBottom = mario.y + GameConfig.PLAYER_HEIGHT;
                double enemyTop = enemy.y;

                // Mario 正在往下掉，且腳底接近敵人頭頂，代表踩到敵人
                if (mario.velocityY > 0 && marioBottom - enemyTop > 0 && marioBottom - enemyTop < 20) {
                    playEnemyDeathAnimation(enemy);
                    enemies.remove(i);
                    mario.velocityY = GameConfig.JUMP_POWER / 1.15;
//                    mario.jumpLevel = 10; 忘記為啥這樣寫
                }
                else {
                    playerDead();
                }
            }
        }
    }

    private void playEnemyDeathAnimation(Enemy enemy) {
        ScaleTransition grow = new ScaleTransition(Duration.millis(80), enemy.view);
        grow.setToX(1.4);
        grow.setToY(1.4);

        ScaleTransition shrink = new ScaleTransition(Duration.millis(160), enemy.view);
        shrink.setToX(0);
        shrink.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(160), enemy.view);
        fade.setToValue(0);

        grow.setOnFinished(e -> {
            ParallelTransition disappear = new ParallelTransition(shrink, fade);
            disappear.setOnFinished(event -> world.getChildren().remove(enemy.view));
            disappear.play();
        });

        grow.play();
    }

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

            playGoalAnimation();
        }
    }

    private void playGoalAnimation() {
        if (timer != null) {timer.stop();}
        if (bgmPlayer != null) {bgmPlayer.stop();}

        RotateTransition rotate = new RotateTransition(Duration.millis(700), mario.view);
        rotate.setByAngle(720);

        ScaleTransition scale = new ScaleTransition(Duration.millis(700), mario.view);
        scale.setToX(0);
        scale.setToY(0);

        TranslateTransition move = new TranslateTransition(Duration.millis(700), mario.view);
        move.setToX(goal.getLayoutX() - mario.x);
        move.setToY(goal.getLayoutY() - mario.y);

        ParallelTransition animation = new ParallelTransition(rotate, scale, move);

        animation.setOnFinished(e -> SceneManager.switchScene("win.fxml"));

        animation.play();
    }

    private void checkDeath() {
        if (mario.y > GameConfig.WINDOW_HEIGHT + 200) {
            playerDead();
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