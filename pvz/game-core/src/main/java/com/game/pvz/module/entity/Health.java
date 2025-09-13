package com.game.pvz.module.entity;

/**
 * 血量值对象（记录类）
 */
public record Health(
        int current,  // 当前血量
        int max       // 最大血量
) {
    
    /**
     * 创建满血量的实例
     */
    public static Health full(int maxHealth) {
        return new Health(maxHealth, maxHealth);
    }
    
    /**
     * 减少血量
     */
    public Health takeDamage(int damage) {
        int newCurrent = Math.max(0, current - damage);
        return new Health(newCurrent, max);
    }
    
    /**
     * 增加血量
     */
    public Health heal(int amount) {
        int newCurrent = Math.min(max, current + amount);
        return new Health(newCurrent, max);
    }
    
    /**
     * 是否已经死亡（血量为0）
     */
    public boolean isDead() {
        return current <= 0;
    }
    
    /**
     * 获取当前血量百分比
     */
    public double getPercentage() {
        return (double) current / max;
    }
}