package com.game.pvz.core.service.impl;

import com.game.pvz.core.service.SpawnService;
import com.game.pvz.core.event.EventBus;
import com.game.pvz.core.event.ZombieSpawned;
import com.game.pvz.module.entity.Position;
import com.game.pvz.module.entity.zombie.Zombie;
import com.game.pvz.module.entity.zombie.ZombieFactory;
import com.game.pvz.module.entity.zombie.ZombieType;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

/**
 * 刷怪实现
 */
public class SpawnServiceImpl implements SpawnService {
    
    private double spawnRate = 1.0;
    private Random random = new Random();
    private int frameIndex = 0;
    
    @Override
    public void spawnZombie() {
        // 强制生成僵尸，不考虑概率，以便测试
        System.out.println("开始生成僵尸...");
        
        // 随机选择一个车道（0-4）
        int laneIndex = random.nextInt(5);
        System.out.println("选择车道: " + laneIndex);
        
        // 设置僵尸初始位置（屏幕右侧）
        Position position = new Position(900, laneIndex * 80 + 40);
        
        // 简化僵尸类型选择，每次都生成不同类型的僵尸用于测试
        ZombieType[] types = {ZombieType.NORMAL, ZombieType.FLAG, ZombieType.CONEHEAD, ZombieType.BUCKETHEAD, ZombieType.FOOTBALL};
        ZombieType type = types[frameIndex % types.length];
        frameIndex++;
        System.out.println("选择僵尸类型: " + type);
        
        // 创建僵尸
        Zombie zombie = ZombieFactory.getInstance().createZombie(type, position, laneIndex);
        System.out.println("创建僵尸成功: " + zombie.getId() + " 类型: " + zombie.getType());
        
        // 发布僵尸生成事件，供UI层处理
        ZombieSpawned event = new ZombieSpawned(
            zombie,
            laneIndex,
            UUID.randomUUID(),
            Instant.now()
        );
        System.out.println("发布僵尸生成事件: " + event);
        EventBus.getInstance().publish(event);
        System.out.println("僵尸生成事件已发布");
    }
    
    @Override
    public void spawnSun() {
        // 实现刷阳光逻辑
    }
    
    @Override
    public void setSpawnRate(double rate) {
        this.spawnRate = rate;
    }
}