package com.game.pvz.core.service.impl;

import com.game.pvz.core.service.GameLoopService;
import com.game.pvz.core.service.SpawnService;
import com.game.pvz.core.event.EventBus;
import com.game.pvz.core.event.GameTick;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * 主循环实现
 */
public class GameLoopServiceImpl implements GameLoopService {
    
    private boolean running = false;
    private int updateRate = 60;
    private Thread gameThread;
    private long frameCount = 0;
    private Instant startTime;
    private SpawnService spawnService;
    private boolean stopSpawningZombies = false; // 新增：控制是否停止生成僵尸

    @Override
    public void start() {
        if (gameThread == null || !gameThread.isAlive()) {
            running = true;
            stopSpawningZombies = false;
            gameThread = new Thread(this::gameLoop);
            gameThread.setDaemon(true);
            gameThread.start();

            startTime = Instant.now();
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
    
    public void setSpawnService(SpawnService spawnService) {
        this.spawnService = spawnService;
    }
    // 新增：设置是否停止生成僵尸的方法
    public void setStopSpawningZombies(boolean stopSpawning) {
        this.stopSpawningZombies = stopSpawning;
        System.out.println(stopSpawning ? "已停止生成僵尸" : "已恢复生成僵尸");
    }

    // 新增：获取当前是否停止生成僵尸的状态
    public boolean isStopSpawningZombies() {
        return stopSpawningZombies;
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
        frameCount++;
        
        // 计算时间差
        Duration elapsedTime = Duration.between(startTime, Instant.now());
        Duration deltaTime = Duration.ofMillis(1000 / updateRate);
        
        // 发布游戏帧事件
        GameTick gameTick = new GameTick(
            frameCount,
            deltaTime,
            elapsedTime,
            UUID.randomUUID(),
            Instant.now()
        );
        EventBus.getInstance().publish(gameTick);
        
        // 添加调试日志
        if (frameCount % 60 == 0) {
            System.out.println("游戏帧: " + frameCount + ", 运行时间: " + elapsedTime.toSeconds() + "秒");
        }
        
    }
}