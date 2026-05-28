package com.example.finalproject;

public class GameConfig {

    // 視窗
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 600;

    // Tile
    public static final int TILE_SIZE = 64;

    // 地板
    public static final int GROUND_Y = (int) (WINDOW_HEIGHT*0.7);

    // 玩家
    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;
    public static final double ACCELERATION = 2.7; // 加速度，越大起步越快
    public static final double FRICTION = 2; // 摩擦力，越大停下越快
    public static final double MAX_MOVE_SPEED = 420; // 最大水平速度
    // 物理
    public static final double GRAVITY = 5;
    public static final double MOVE_SPEED = 10.5;
    public static final double JUMP_POWER = -40.7;

    // 攝影機
    public static final int CAMERA_START_X = (int) (WINDOW_WIDTH*0.3);
}
