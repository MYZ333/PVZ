package com.game.pvz.core.service;

/**
 * 【接口】游戏主循环驱动
 */
public interface GameLoopService {
    void start();
    void stop();
    boolean isRunning();
    void setUpdateRate(int fps);
}