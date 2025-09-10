package com.game.pvz.module.entity.projectile;

/**
 * 子弹类型枚举
 */
public enum ProjectileType {
    PEANUT(20, 20),          // 普通豌豆
    FIRE_PEANUT(30, 20),     // 火焰豌豆
    ICE_PEANUT(10, 20);      // 冰冻豌豆
    
    private final int damage;     // 伤害值
    private final int speed;      // 移动速度
    
    ProjectileType(int damage, int speed) {
        this.damage = damage;
        this.speed = speed;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getSpeed() {
        return speed;
    }
}