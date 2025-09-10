package com.game.pvz.module.entity.projectile;

import com.game.pvz.module.entity.Position;

/**
 * 火焰子弹（类）
 * Projectile的扩展类，实现火焰子弹的特殊效果
 */
public class FireProjectile extends Projectile {
    
    private int burnDamage; // 燃烧伤害
    private int burnDuration; // 燃烧持续时间（帧数）
    private boolean isBurning; // 是否正在燃烧
    
    /**
     * 构造火焰子弹
     * @param position 初始位置
     * @param laneIndex 所在车道索引
     */
    public FireProjectile(Position position, int laneIndex) {
        super(ProjectileType.FIRE_PEANUT, position, laneIndex);
        this.burnDamage = 5; // 每次燃烧造成的伤害
        this.burnDuration = 30; // 燃烧持续30帧
        this.isBurning = true; // 火焰子弹默认处于燃烧状态
    }
    
    /**
     * 获取燃烧伤害
     * @return 燃烧伤害值
     */
    public int getBurnDamage() {
        return burnDamage;
    }
    
    /**
     * 获取燃烧持续时间
     * @return 燃烧持续时间（帧数）
     */
    public int getBurnDuration() {
        return burnDuration;
    }
    
    /**
     * 检查子弹是否正在燃烧
     * @return 是否正在燃烧
     */
    public boolean isBurning() {
        return isBurning;
    }
    
    /**
     * 设置子弹是否燃烧
     * @param burning 是否燃烧
     */
    public void setBurning(boolean burning) {
        isBurning = burning;
    }
    
    /**
     * 重写更新位置方法，添加火焰特效逻辑
     * @param deltaTime 时间间隔
     */
    @Override
    public void updatePosition(double deltaTime) {
        super.updatePosition(deltaTime);
        // 可以在这里添加火焰特效的更新逻辑
        // 例如，随着时间推移，燃烧效果减弱
    }
    
    /**
     * 重写承受伤害方法，火焰子弹可能有特殊的伤害承受逻辑
     * @param damage 伤害值
     * @return 是否被摧毁
     */
    @Override
    public boolean takeDamage(int damage) {
        // 火焰子弹可能对某些类型的伤害免疫
        // 或者有特殊的承受伤害逻辑
        return false; // 默认情况下，火焰子弹不会被普通伤害摧毁
    }
    
    /**
     * 应用燃烧效果到目标
     * @param target 目标对象
     * @return 是否成功应用燃烧效果
     */
    public boolean applyBurnEffect(Object target) {
        // 实现燃烧效果的应用逻辑
        // 注意：实际应用中需要根据游戏实体类型进行处理
        return isBurning;
    }
}