package com.game.pvz.module.entity.projectile;

import com.game.pvz.module.entity.Position;

/**
 * 子弹工厂（类）
 * 负责创建各种类型的子弹实例
 */
public class ProjectileFactory {
    
    private static final ProjectileFactory INSTANCE = new ProjectileFactory();
    
    private ProjectileFactory() {
        // 私有构造函数，防止外部实例化
    }
    
    public static ProjectileFactory getInstance() {
        return INSTANCE;
    }
    
    /**
     * 创建指定类型的子弹
     * @param type 子弹类型
     * @param position 初始位置
     * @param laneIndex 所在车道索引
     * @return 创建的子弹实例
     */
    public Projectile createProjectile(ProjectileType type, Position position, int laneIndex) {
        return new Projectile(type, position, laneIndex);
    }
    
    /**
     * 创建普通豌豆子弹
     * @param position 初始位置
     * @param laneIndex 所在车道索引
     * @return 普通豌豆子弹实例
     */
    public Projectile createPeanut(Position position, int laneIndex) {
        return createProjectile(ProjectileType.PEANUT, position, laneIndex);
    }
    
    /**
     * 创建火焰豌豆子弹
     * @param position 初始位置
     * @param laneIndex 所在车道索引
     * @return 火焰豌豆子弹实例
     */
    public Projectile createFirePeanut(Position position, int laneIndex) {
        return createProjectile(ProjectileType.FIRE_PEANUT, position, laneIndex);
    }
    
    /**
     * 创建冰冻豌豆子弹
     * @param position 初始位置
     * @param laneIndex 所在车道索引
     * @return 冰冻豌豆子弹实例
     */
    public Projectile createIcePeanut(Position position, int laneIndex) {
        return createProjectile(ProjectileType.ICE_PEANUT, position, laneIndex);
    }
}