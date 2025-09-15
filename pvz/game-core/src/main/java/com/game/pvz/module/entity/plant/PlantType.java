package com.game.pvz.module.entity.plant;

/**
 * 植物类型枚举
 */
public enum PlantType {
<<<<<<< HEAD

=======
>>>>>>> f301b6087c14b4819f630b2622fbff8764c4f319
    PEASHOOTER(10, 20, 1, 5000),       // 豌豆射手，5秒冷却
    SUNFLOWER(5, 50, 1, 7000),         // 向日葵，7秒冷却
    WALLNUT(5, 300, 1, 10000),         // 坚果墙，10秒冷却
    CHERRY_BOMB(15, 100, 1, 15000),    // 樱桃炸弹，15秒冷却
    REPEATER(20, 20, 2, 8000);         // 双发射手，8秒冷却

    private final int cost;        // 阳光成本
    private final int damage;      // 攻击力
    private final int range;       // 攻击范围
    private final int cooldown;    // 冷却时间(毫秒)

    PlantType(int cost, int damage, int range, int cooldown) {
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.cooldown = cooldown;
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
<<<<<<< HEAD

    public int getCooldown() {
        return cooldown;
    }

=======
    public int getCooldown() {
        return cooldown;
    }
>>>>>>> f301b6087c14b4819f630b2622fbff8764c4f319
}