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
    public static final double maxVelocityX = 10.5; // 最大水平速度
    public static final double maxVelocityY = -GameConfig.JUMP_POWER; // 最大垂直速度
    // 物理
    public static final double GRAVITY = 5.3;
    public static final double MOVE_SPEED = 10.5;
    public static final double JUMP_POWER = -40.7;

    // 攝影機
    public static final int CAMERA_START_X = (int) (WINDOW_WIDTH*0.3);
// ================= Tile ID =================

    public static final int AIR = 0;
    public static final int GROUND = 1;
    public static final int GOAL = 9;
    public static final int STONE = 4;
    public static final int ENEMY = 8;
    public static final int HARD_BLOCK = 5;
    public static final int SPECIAL_BLOCK = 6;
    public static final int BRIDGE = 7;
// =============== ENEMY DETAIL =================
    public static final int ENEMY_WIDTH = 50;
    public static final int ENEMY_HEIGHT = 50;
    public static final double ENEMY_SPEED = 2.5;
}