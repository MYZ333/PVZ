package com.game.pvz.module.entity.zombie;

/**
 * 僵尸类型枚举
 */
public enum ZombieType {
    NORMAL(10, 100),            // 普通僵尸
    FLAG(10, 100),              // 旗帜僵尸
    CONEHEAD(10, 300),          // 路障僵尸
    BUCKETHEAD(10, 500),        // 铁桶僵尸
    FOOTBALL(15, 1000);         // 橄榄球僵尸
    
    private final int speed;     // 移动速度
    private final int health;    // 初始血量
    
    ZombieType(int speed, int health) {
        this.speed = speed;
        this.health = health;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public int getHealth() {
        return health;
    }
}