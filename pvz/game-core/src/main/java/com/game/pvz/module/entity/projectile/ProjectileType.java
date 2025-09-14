package com.game.pvz.module.entity.projectile;

/**
 * 子弹类型枚举
 */
public enum ProjectileType {
    NONE(0, 0, false, false, 0),            // 无子弹（默认）
    PEANUT(20, 20, false, false, 0),        // 普通豌豆
    FIRE_PEANUT(30, 20, true, false, 3000), // 火焰豌豆（燃烧效果）
    ICE_PEANUT(10, 20, false, true, 2000),  // 冰冻豌豆（减速效果）
    CABBAGE(20, 15, false, false, 0),       // 卷心菜（投掷类）
    MELON(40, 10, false, false, 0);         // 西瓜（投掷类）
    
    private final int damage;         // 伤害值
    private final int speed;          // 移动速度
    private final boolean burns;      // 是否有燃烧效果
    private final boolean slows;      // 是否有减速效果
    private final long effectDuration; // 效果持续时间（毫秒）
    
    ProjectileType(int damage, int speed, boolean burns, boolean slows, long effectDuration) {
        this.damage = damage;
        this.speed = speed;
        this.burns = burns;
        this.slows = slows;
        this.effectDuration = effectDuration;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public boolean isBurns() {
        return burns;
    }
    
    public boolean isSlows() {
        return slows;
    }
    
    public long getEffectDuration() {
        return effectDuration;
    }
    
    /**
     * 判断子弹是否有特殊效果
     * @return 是否有特殊效果
     */
    public boolean hasSpecialEffect() {
        return burns || slows;
    }
}