package com.game.pvz.module.entity.zombie;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Health;
import com.game.pvz.module.entity.Position;
import com.game.pvz.module.entity.plant.Plant;
import com.game.pvz.core.event.EventBus;
import com.game.pvz.core.event.ZombieDamaged;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 僵尸实体（类）
 */
public class Zombie implements GameObject {
    private final ZombieType type;
    private Position position;
    private Health health;
    private int laneIndex;
    private boolean isDead;
    private long lastAttackTime;
    private boolean isEating; // 是否正在吃植物
    private Plant targetPlant; // 当前攻击的植物目标
    private long spawnTime; // 生成时间
    private boolean hasArmor; // 是否有护甲（如铁桶僵尸）
    private int armorHealth; // 护甲生命值
    private final UUID id; // 僵尸唯一标识
    
    protected Zombie(ZombieType type, Position position, Health health, int laneIndex) {
        this.type = type;
        this.position = position;
        this.health = health;
        this.laneIndex = laneIndex;
        this.isDead = false;
        this.lastAttackTime = 0;
        this.isEating = false;
        this.targetPlant = null;
        this.spawnTime = System.currentTimeMillis();
        this.hasArmor = type.hasArmor();
        this.armorHealth = type.getArmorHealth();
        this.id = UUID.randomUUID(); // 生成唯一ID
    }
    
    public ZombieType getType() {
        return type;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Health getHealth() {
        return health;
    }
    
    public void setHealth(Health health) {
        this.health = health;
    }
    
    public int getLaneIndex() {
        return laneIndex;
    }
    
    public void setLaneIndex(int laneIndex) {
        this.laneIndex = laneIndex;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public void setDead(boolean dead) {
        isDead = dead;
    }
    
    public long getLastAttackTime() {
        return lastAttackTime;
    }
    
    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
    
    public int getSpeed() {
        return type.getSpeed();
    }
    
    public boolean isEating() {
        return isEating;
    }
    
    public void setEating(boolean eating) {
        isEating = eating;
    }
    
    public Plant getTargetPlant() {
        return targetPlant;
    }
    
    public void setTargetPlant(Plant targetPlant) {
        this.targetPlant = targetPlant;
    }
    
    public long getSpawnTime() {
        return spawnTime;
    }
    
    public boolean hasArmor() {
        return hasArmor;
    }
    
    public int getArmorHealth() {
        return armorHealth;
    }
    
    /**
     * 获取僵尸唯一标识
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * 僵尸移动逻辑
     * @param deltaTime 时间间隔（毫秒）
     */
    public void move(long deltaTime) {
        // 如果正在吃植物，则不移动
        if (isEating) {
            return;
        }
        
        // 使用getActualSpeed()而不是getSpeed()，考虑状态加成
        double distanceToMove = (getActualSpeed() * deltaTime) / 1000.0;

        // 僵尸向左移动（X坐标减小）
        Position newPosition = new Position(position.x() - distanceToMove, position.y());
        setPosition(newPosition);
    }
    
    /**
     * 检查前方是否有植物
     * @param plants 所有植物列表
     * @return 前方最近的植物
     */
    public Plant checkForPlantsAhead(List<Plant> plants) {
        double detectionRange = 50; // 检测范围（像素）
        
        return plants.stream()
                .filter(plant -> plant.getLaneIndex() == laneIndex) // 确保在同一车道
                .filter(plant -> !plant.isDead()) // 只考虑存活的植物
                .filter(plant -> plant.getPosition().x() <= position.x() + detectionRange) // 在检测范围内
                .filter(plant -> plant.getPosition().x() >= 0) // 植物在屏幕内
                .min((p1, p2) -> Double.compare(p2.getPosition().x(), p1.getPosition().x())) // 找到最右边的植物
                .orElse(null);
    }
    
    /**
     * 僵尸攻击逻辑
     * @param currentTime 当前时间
     * @param plant 要攻击的植物
     */
    public void attackPlant(long currentTime, Plant plant) {
        // 检查攻击冷却
        long attackCooldown = type.getAttackCooldown();
        if (currentTime - lastAttackTime < attackCooldown) {
            return;
        }
        
        // 直接对植物造成伤害，不进行范围检查（简化逻辑）
        plant.takeDamage(type.getDamage());

        // 调试输出，验证伤害是否正确施加
        System.out.println("僵尸攻击植物: " + type.name() + " 造成 " + type.getDamage() + " 点伤害，植物剩余血量: " + plant.getHealth().current());

        lastAttackTime = currentTime;
        
        // 如果植物死亡，停止攻击
        if (plant.isDead()) {
            System.out.println("植物已被吃掉: " + plant.getType().name());
            isEating = false;
            targetPlant = null;
        }
    }
    
    /**
     * 僵尸受到伤害
     * @param damage 伤害值
     */
    public void takeDamage(int damage) {
        boolean armorHit = hasArmor && armorHealth > 0;
        int remainingDamage = damage;
        // 如果有护甲，先减少护甲生命值
        if (armorHit) {
            // 计算护甲能吸收的伤害
            int armorAbsorb = Math.min(armorHealth, damage);
            armorHealth -= armorAbsorb;
            remainingDamage = damage - armorAbsorb;

            if (armorHealth <= 0) {
                hasArmor = false;
                // 护甲被破坏时可能有特殊效果
                onArmorDestroyed();
            }
        }
        // 将剩余伤害应用到基础生命值
        if (remainingDamage > 0) {
            this.health = this.health.takeDamage(remainingDamage);
        }
        
        // 发布僵尸受到伤害事件
        ZombieDamaged damagedEvent = new ZombieDamaged(
                this,
                damage,
                armorHit,
                UUID.randomUUID(),
                Instant.now()
        );
        EventBus.getInstance().publish(damagedEvent);
        
        // 检查是否死亡
        if (health.isDead()) {
            isDead = true;
            isEating = false;
            targetPlant = null;
            // 僵尸死亡时的处理
            onDeath();
        }
    }
    
    /**
     * 护甲被破坏时的处理
     */
    private void onArmorDestroyed() {
        // 可以添加护甲被破坏时的效果，如播放音效、显示动画等
        // 例如：铁桶僵尸的铁桶被打破后，速度可能会增加
        if (type == ZombieType.BUCKETHEAD) {
            // 铁桶被打破后，僵尸速度可能会略微增加
            // 注意：这里可能需要修改type的speed值，或者在移动时考虑这个加成
        }
    }
    
    /**
     * 僵尸死亡时的处理
     */
    private void onDeath() {
        // 可以添加僵尸死亡时的效果，如播放音效、显示动画等
        // 某些特殊僵尸可能有死亡时的特殊效果
        if (type == ZombieType.GARGANTUAR) {
            // 巨人僵尸死亡时可能会爆炸或者留下小僵尸
        }
    }
    
    /**
     * 更新僵尸状态
     * @param currentTime 当前时间
     * @param plants 所有植物列表
     */
    public void update(long currentTime, List<Plant> plants) {
        // 如果已经死亡，不再更新
        if (isDead) {
            return;
        }
        
        // 检查前方是否有植物
        Plant frontPlant = checkForPlantsAhead(plants);
        
        if (frontPlant != null) {
            // 有植物在前方，开始攻击
            isEating = true;
            targetPlant = frontPlant;
            attackPlant(currentTime, frontPlant);
            // 当有植物时不移动
            return;
        } else {
            // 前方没有植物，继续移动
            isEating = false;
            targetPlant = null;
            // 使用当前时间差计算移动距离
            long deltaTime = currentTime - lastAttackTime;
            // 限制最大时间差，避免瞬间移动
            deltaTime = Math.min(33, deltaTime); // 最多33ms，约30FPS
            move(deltaTime);
        }
        
        // 移除这行代码，不再重置lastAttackTime，以保证攻击冷却机制正常工作
    }
    
    /**
     * 获取僵尸当前的移动速度（考虑可能的状态加成或惩罚）
     * @return 实际移动速度
     */
    public double getActualSpeed() {
        double baseSpeed = getSpeed();
        
        // 可以根据僵尸状态调整实际移动速度
        // 例如：铁桶僵尸的铁桶被打破后，速度可能会增加
        if (type == ZombieType.BUCKETHEAD && !hasArmor) {
            return baseSpeed * 1.2; // 铁桶被打破后速度增加20%
        }
        
        // 某些特殊僵尸可能有不同的速度调整
        switch (type) {
            case FLAG -> { return baseSpeed * 1.5; } // 旗帜僵尸速度较快
            case GARGANTUAR -> { return baseSpeed * 0.7; } // 巨人僵尸速度较慢
            default -> { return baseSpeed; }
        }
    }
}