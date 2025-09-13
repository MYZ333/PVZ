package com.game.pvz.module.entity.projectile;

import com.game.pvz.module.entity.Position;

/**
 * 冰冻子弹（类）
 * Projectile的扩展类，实现冰冻子弹的特殊效果
 */
public class IceProjectile extends Projectile {
    
    private int slowDuration; // 减速持续时间（帧数）
    private double slowFactor; // 减速因子（0.0-1.0）
    private boolean isFrozen;
    
    /**
     * 构造冰冻子弹
     * @param position 初始位置
     * @param laneIndex 所在车道索引
     */
    public IceProjectile(Position position, int laneIndex) {
        super(ProjectileType.ICE_PEANUT, position, laneIndex);
        this.slowDuration = 60; // 减速持续60帧
        this.slowFactor = 0.5; // 减速50%
        this.isFrozen = true;
    }
    
    /**
     * 获取减速持续时间
     * @return 减速持续时间（帧数）
     */
    public int getSlowDuration() {
        return slowDuration;
    }
    
    /**
     * 获取减速因子
     * @return 减速因子（0.0-1.0）
     */
    public double getSlowFactor() {
        return slowFactor;
    }
    
    /**
     * 检查子弹是否处于冰冻状态
     * @return 是否处于冰冻状态
     */
    public boolean isFrozen() {
        return isFrozen;
    }
    
    /**
     * 设置子弹是否处于冰冻状态
     * @param frozen 是否处于冰冻状态
     */
    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }
    
    /**
     * 重写更新位置方法，添加冰冻特效逻辑
     * @param deltaTime 时间间隔（毫秒）
     */
    @Override
    public void updatePosition(long deltaTime) {
        super.updatePosition(deltaTime);
        // 可以在这里添加冰冻特效的更新逻辑
    }
    
    /**
     * 应用冰冻效果到目标
     * @param target 目标对象
     * @return 是否成功应用冰冻效果
     */
    public boolean applyFreezeEffect(Object target) {
        // 实现冰冻效果的应用逻辑
        // 注意：实际应用中需要根据游戏实体类型进行处理
        return isFrozen;
    }
}