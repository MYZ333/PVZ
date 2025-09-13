package com.game.pvz.module.entity.projectile;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Position;
import com.game.pvz.module.entity.zombie.Zombie;

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
    private boolean hasHitTarget; // 是否已击中目标
    private long creationTime;    // 创建时间
    
    public Projectile(ProjectileType type, Position position, int laneIndex) {
        this.type = type;
        this.position = position;
        this.damage = type.getDamage();
        this.speed = type.getSpeed();
        this.laneIndex = laneIndex;
        this.isActive = true;
        this.hasHitTarget = false;
        this.creationTime = System.currentTimeMillis();
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
    
    public boolean isHasHitTarget() {
        return hasHitTarget;
    }
    
    public void setHasHitTarget(boolean hasHitTarget) {
        this.hasHitTarget = hasHitTarget;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * 更新子弹位置
     * @param deltaTime 时间间隔（毫秒）
     */
    public void updatePosition(long deltaTime) {
        // 子弹通常向右移动（假设游戏是从左到右的视角）
        double distance = (speed * deltaTime) / 1000.0;
        double newX = position.x() + distance;
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
    
    /**
     * 检查子弹是否击中僵尸
     * @param zombie 目标僵尸
     * @return 是否击中
     */
    public boolean checkCollision(Zombie zombie) {
        // 检查是否在同一车道
        if (zombie.getLaneIndex() != laneIndex) {
            return false;
        }
        
        // 检查僵尸是否存活
        if (zombie.isDead()) {
            return false;
        }
        
        // 检查子弹是否仍活跃
        if (!isActive || hasHitTarget) {
            return false;
        }
        
        // 根据子弹类型使用不同的碰撞检测策略
        switch (type) {
            case PEANUT:
            case ICE_PEANUT:
                // 普通豌豆和冰冻豌豆使用圆形碰撞检测
                double collisionRange = 30; // 碰撞范围
                double distance = calculateDistance(zombie.getPosition());
                return distance <= collisionRange;
            case MELON:
            case CABBAGE:
                // 投掷类子弹使用矩形碰撞检测，覆盖更大区域
                Position zombiePos = zombie.getPosition();
                double projectileX = position.x();
                double projectileY = position.y();
                
                // 检查子弹是否在僵尸的矩形范围内
                return projectileX >= zombiePos.x() - 35 && 
                       projectileX <= zombiePos.x() + 35 && 
                       projectileY >= zombiePos.y() - 35 && 
                       projectileY <= zombiePos.y() + 35;
            default:
                // 其他子弹类型使用默认的圆形碰撞检测
                return calculateDistance(zombie.getPosition()) <= 25;
        }
    }
    
    /**
     * 子弹击中僵尸时的处理
     * @param zombie 被击中的僵尸
     * @param currentTime 当前时间
     */
    public void onHit(Zombie zombie, long currentTime) {
        if (hasHitTarget || !isActive) {
            return;
        }
        
        // 对僵尸造成伤害
        zombie.takeDamage(damage);
        
        // 应用特殊效果
        applySpecialEffects(zombie, currentTime);
        
        // 标记为已击中
        hasHitTarget = true;
        setActive(false);
    }
    
    /**
     * 应用子弹的特殊效果
     * @param zombie 目标僵尸
     * @param currentTime 当前时间
     */
    private void applySpecialEffects(Zombie zombie, long currentTime) {
        if (!type.hasSpecialEffect()) {
            return;
        }
        
        // 处理燃烧效果
        if (type.isBurns()) {
            applyBurnEffect(zombie, currentTime);
        }
        
        // 处理减速效果
        if (type.isSlows()) {
            applySlowEffect(zombie, currentTime);
        }
    }
    
    /**
     * 应用燃烧效果
     * @param zombie 目标僵尸
     * @param currentTime 当前时间
     */
    private void applyBurnEffect(Zombie zombie, long currentTime) {
        // 燃烧效果的实现需要在Zombie类中添加相应的字段和方法
        // 这里只是一个示例，可以根据实际需求调整
        // 例如：zombie.setBurned(true);
        //       zombie.setBurnStartTime(currentTime);
        //       zombie.setBurnDuration(type.getEffectDuration());
    }
    
    /**
     * 应用减速效果
     * @param zombie 目标僵尸
     * @param currentTime 当前时间
     */
    private void applySlowEffect(Zombie zombie, long currentTime) {
        // 减速效果的实现需要在Zombie类中添加相应的字段和方法
        // 这里只是一个示例，可以根据实际需求调整
        // 例如：zombie.setSlowed(true);
        //       zombie.setSlowStartTime(currentTime);
        //       zombie.setSlowDuration(type.getEffectDuration());
        //       zombie.setSlowFactor(0.5); // 减速50%
    }
    
    /**
     * 更新子弹状态
     * @param currentTime 当前时间
     * @param deltaTime 时间间隔（毫秒）
     */
    public void update(long currentTime, long deltaTime) {
        // 更新位置
        updatePosition(deltaTime);
        
        // 可以添加其他更新逻辑，如子弹生命周期管理
        
        // 检查子弹是否超出屏幕范围（假设屏幕宽度为800像素）
        if (position.x() > 800) {
            setActive(false);
        }
    }
    
    /**
     * 获取子弹的渲染优先级
     * @return 优先级（数值越小，优先级越高）
     */
    public int getRenderPriority() {
        // 可以根据子弹类型设置不同的渲染优先级
        switch (type) {
            case MELON:
            case CABBAGE:
                return 1; // 投掷类子弹优先级较高
            default:
                return 2; // 其他子弹优先级较低
        }
    }
}