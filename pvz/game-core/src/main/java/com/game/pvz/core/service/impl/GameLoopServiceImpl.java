package com.game.pvz.core.service.impl;

import com.game.pvz.core.service.GameLoopService;

/**
 * 主循环实现
 */
class GameLoopServiceImpl implements GameLoopService {
    
    private boolean running = false;
    private int updateRate = 60;
    private Thread gameThread;
    
    @Override
    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this::gameLoop);
            gameThread.start();
        }
    }
    
    @Override
    public void stop() {
        if (running) {
            running = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void setUpdateRate(int fps) {
        this.updateRate = fps;
    }
    
    private void gameLoop() {
        final long targetTime = 1000 / updateRate;
        
        while (running) {
            long startTime = System.currentTimeMillis();
            
            // 游戏逻辑更新
            update();
            
            // 帧率控制
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime < targetTime) {
                try {
                    Thread.sleep(targetTime - elapsedTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    private void update() {
        // 实现游戏逻辑更新
    }
}