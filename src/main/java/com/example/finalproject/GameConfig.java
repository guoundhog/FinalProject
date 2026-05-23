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

    // 物理
    public static final double GRAVITY = 0.4;
    public static final double MOVE_SPEED = 3;
    public static final double JUMP_POWER = -14.2;

    // 攝影機
    public static final int CAMERA_START_X = (int) (WINDOW_WIDTH*0.3);
}
