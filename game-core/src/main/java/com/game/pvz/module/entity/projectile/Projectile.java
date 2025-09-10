package com.game.pvz.module.entity.projectile;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Position;

/**
 * 子弹实体（类）
 */
public class Projectile implements GameObject {
    private final ProjectileType type;
    private Position position;
    private final int damage;
    private final int speed;
    private final int laneIndex;
    private boolean isActive;
    
    public Projectile(ProjectileType type, Position position, int laneIndex) {
        this.type = type;
        this.position = position;
        this.damage = type.getDamage();
        this.speed = type.getSpeed();
        this.laneIndex = laneIndex;
        this.isActive = true;
    }
    
    public ProjectileType getType() {
        return type;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public int getLaneIndex() {
        return laneIndex;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * 更新子弹位置
     * @param deltaTime 时间间隔
     */
    public void updatePosition(double deltaTime) {
        // 子弹通常向右移动（假设游戏是从左到右的视角）
        double newX = position.x() + (speed * deltaTime);
        this.position = new Position(newX, position.y());
    }
    
    /**
     * 子弹承受伤害（某些特殊子弹可能有生命值）
     * @param damage 伤害值
     * @return 是否被摧毁
     */
    public boolean takeDamage(int damage) {
        // 默认情况下，子弹通常不会承受伤害，但保持这个方法以保持接口一致性
        // 可以根据需要在子类中重写
        return false;
    }
    
    /**
     * 检查子弹是否"死亡"（不再活跃）
     * @return 是否死亡
     */
    public boolean isDead() {
        return !isActive;
    }
    
    /**
     * 计算与另一个对象的距离
     * @param other 另一个对象的位置
     * @return 距离
     */
    public double calculateDistance(Position other) {
        return position.distance(other);
    }
}