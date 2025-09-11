package com.game.pvz.module.entity.zombie;

/**
 * 僵尸类型枚举
 */
public enum ZombieType {
    NORMAL(10, 100, false, 0, 25, 1500),            // 普通僵尸
    FLAG(10, 100, false, 0, 25, 1500),              // 旗帜僵尸
    CONEHEAD(10, 100, true, 200, 20, 1500),         // 路障僵尸
    BUCKETHEAD(10, 100, true, 400, 20, 1500),       // 铁桶僵尸
    FOOTBALL(15, 300, true, 700, 25, 1200),         // 橄榄球僵尸
    GARGANTUAR(5, 3000, true, 2000, 1000, 1000);      // 巨人僵尸
    
    private final int speed;           // 移动速度
    private final int health;          // 基础血量
    private final boolean hasArmor;    // 是否有护甲
    private final int armorHealth;     // 护甲生命值
    private final int damage;          // 攻击力
    private final long attackCooldown; // 攻击冷却时间（毫秒）
    
    ZombieType(int speed, int health, boolean hasArmor, int armorHealth, int damage, long attackCooldown) {
        this.speed = speed;
        this.health = health;
        this.hasArmor = hasArmor;
        this.armorHealth = armorHealth;
        this.damage = damage;
        this.attackCooldown = attackCooldown;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean hasArmor() {
        return hasArmor;
    }
    
    public int getArmorHealth() {
        return armorHealth;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public long getAttackCooldown() {
        return attackCooldown;
    }
    
    /**
     * 获取僵尸的总生命值（基础生命值+护甲生命值）
     * @return 总生命值
     */
    public int getTotalHealth() {
        return health + (hasArmor ? armorHealth : 0);
    }
    
    /**
     * 判断僵尸类型是否为精英僵尸
     * @return 是否为精英僵尸
     */
    public boolean isElite() {
        return this == GARGANTUAR || this == FOOTBALL;
    }
}