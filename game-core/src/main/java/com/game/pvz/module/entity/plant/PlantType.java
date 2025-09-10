package com.game.pvz.module.entity.plant;

/**
 * 植物类型枚举
 */
public enum PlantType {
    PEASHOOTER(100, 20, 1),       // 豌豆射手
    SUNFLOWER(50, 50, 1),         // 向日葵
    WALLNUT(5, 300, 1),           // 坚果墙
    CHERRY_BOMB(150, 100, 1),     // 樱桃炸弹
    REPEATER(200, 20, 2);         // 双发射手
    
    private final int cost;        // 阳光成本
    private final int damage;      // 攻击力
    private final int range;       // 攻击范围
    
    PlantType(int cost, int damage, int range) {
        this.cost = cost;
        this.damage = damage;
        this.range = range;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getRange() {
        return range;
    }
}