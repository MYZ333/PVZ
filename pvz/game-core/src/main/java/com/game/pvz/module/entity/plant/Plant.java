package com.game.pvz.module.entity.plant;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Health;
import com.game.pvz.module.entity.Position;
import com.game.pvz.module.entity.zombie.Zombie;
import com.game.pvz.module.entity.projectile.Projectile;
import com.game.pvz.module.entity.projectile.ProjectileFactory;
import com.game.pvz.module.entity.projectile.ProjectileType;
import com.game.pvz.core.event.EventBus;
import com.game.pvz.core.event.PlantDamaged;
import com.game.pvz.core.event.PlantDied;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 植物实体（类）
 */
public class Plant implements GameObject {
    private final PlantType type;
    private final Position position;
    private Health health;
    private long lastAttackTime;
    private boolean isReadyToAttack;
    private long lastProduceTime; // 上次生产阳光的时间（用于向日葵）
    private boolean exploded; // 是否已爆炸（用于樱桃炸弹）
    private long plantedTime; // 植物被种下的时间（用于樱桃炸弹）

    protected Plant(PlantType type, Position position, Health health) {
        this.type = type;
        this.position = position;
        this.health = health;
        this.lastAttackTime = 0;
        this.isReadyToAttack = true;
        this.lastProduceTime = 0;
        this.exploded = false;
        this.plantedTime = 0; // 初始化种植时间
    }
    
    public PlantType getType() {
        return type;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public Health getHealth() {
        return health;
    }
    
    public void setHealth(Health health) {
        this.health = health;
    }
    
    public boolean isDead() {
        return health.isDead();
    }
    
    public void takeDamage(int damage) {
        Health oldHealth = this.health;
        this.health = this.health.takeDamage(damage);
        
        // 发布植物受到伤害事件
        int remainingHealth = this.health.current();
        PlantDamaged damagedEvent = new PlantDamaged(
                this, 
                damage, 
                remainingHealth,
                UUID.randomUUID(),
                Instant.now()
        );
        EventBus.getInstance().publish(damagedEvent);
        
        // 如果植物死亡，发布植物死亡事件
        if (this.health.isDead()) {
            PlantDied diedEvent = new PlantDied(
                    this, 
                    getLaneIndex(),
                    UUID.randomUUID(),
                    Instant.now()
            );
            EventBus.getInstance().publish(diedEvent);
        }
    }
    
    public boolean isReadyToAttack() {
        return isReadyToAttack;
    }
    
    public void setReadyToAttack(boolean readyToAttack) {
        isReadyToAttack = readyToAttack;
    }
    
    public long getLastAttackTime() {
        return lastAttackTime;
    }
    
    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
    
    public int getCost() {
        return type.getCost();
    }
    
    public int getDamage() {
        return type.getDamage();
    }
    
    public int getRange() {
        return type.getRange();
    }
    
    public long getLastProduceTime() {
        return lastProduceTime;
    }
    
    public void setLastProduceTime(long lastProduceTime) {
        this.lastProduceTime = lastProduceTime;
    }
    
    public boolean isExploded() {
        return exploded;
    }
    
    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public void setPlantedTime(long plantedTime) {
        this.plantedTime = plantedTime;
    }

    // 添加getPlantedTime方法
    public long getPlantedTime() {
        return plantedTime;
    }


    public List<Projectile> attack(long now, List<Zombie> zombies) {
        List<Projectile> projectiles = new java.util.ArrayList<>();
        // 检查攻击冷却时间
        if (now - lastAttackTime < getAttackCooldown()) {
            return projectiles;
        }
        
        switch (type) {
            case PEASHOOTER,REPEATER:
                // 查找范围内的第一个僵尸
                Zombie target = findFirstZombieInRange(zombies);
                if (target != null) {
                    System.out.println("找到目标僵尸，准备发射子弹！位置: " + target.getPosition().x() + ", " + target.getPosition().y());
                    // 普通豌豆射手发射一颗子弹
                    projectiles.add(createProjectile());

                    // 双发射手发射两颗子弹
                    if (type == PlantType.REPEATER) {
                        projectiles.add(createProjectile());
                    }

                    // 更新最后攻击时间
                    lastAttackTime = now;
                }
                break;
            // 向日葵和坚果墙没有主动攻击能力
            case SUNFLOWER, WALLNUT, CHERRY_BOMB :{}
        }
        
        return projectiles;
    }
    
    /**
     * 创建子弹
     * @return 创建的子弹
     */
    private Projectile createProjectile() {
        // 子弹从植物前方发射
        Position projectilePos = new Position(position.x() + 105, position.y());

        // 根据植物类型确定子弹类型
        ProjectileType projectileType = ProjectileType.PEANUT;

        // 使用ProjectileFactory创建子弹
        Projectile projectile = ProjectileFactory.getInstance().createProjectile(projectileType, projectilePos, getLaneIndex());

        System.out.println("创建子弹: 类型=" + projectileType + ", 位置=(" + projectilePos.x() + ", " + projectilePos.y() + ")");

        return projectile;
    }
    
    /**
     * 找到攻击范围内的第一个僵尸
     * @param zombies 僵尸列表
     * @return 第一个进入攻击范围的僵尸
     */
    private Zombie findFirstZombieInRange(List<Zombie> zombies) {
        // 假设攻击范围是向右的
        // 输出调试日志，确认方法被调用
        System.out.println("植物正在检测范围内僵尸... 植物位置: " + position.x() + ", " + position.y());
        System.out.println("当前僵尸数量: " + zombies.size());

        // 对每个僵尸进行调试输出
        for (Zombie z : zombies) {
            System.out.println("僵尸位置: " + z.getPosition().x() + ", " + z.getPosition().y() + ", 车道: " + z.getLaneIndex());
        }

        // 修正检测逻辑：获取最接近植物的僵尸
        return zombies.stream()
                .filter(zombie -> zombie.getLaneIndex() == getLaneIndex())
                .filter(zombie -> zombie.getPosition().x() >= position.x() - 100) // 稍微向左扩展一点
                .filter(zombie -> zombie.getPosition().x() <= position.x() + 1000) // 大幅向右扩展，覆盖整个屏幕宽度
                .min((z1, z2) -> Double.compare(z1.getPosition().x(), z2.getPosition().x())) // 使用max获取最右侧的僵尸
                .orElse(null);
    }
    
    /**
     * 樱桃炸弹爆炸，对范围内的所有僵尸造成伤害
     */
    private void explode(List<Zombie> zombies) {
        // 爆炸半径
        int explosionRadius = 200; // 爆炸范围扩大一倍

        System.out.println("樱桃炸弹爆炸！植物位置: (" + position.x() + ", " + position.y() + "), 爆炸半径: " + explosionRadius);

        // 对范围内的所有僵尸造成伤害
        for (Zombie zombie : zombies) {
            double distance = position.distance(zombie.getPosition());
            System.out.println("僵尸ID: " + zombie.getId() + ", 位置: (" + zombie.getPosition().x() + ", " + zombie.getPosition().y() + "), 距离: " + distance);
            if (distance <= explosionRadius) {
                // 造成10000点伤害
                zombie.takeDamage(1000);
                System.out.println("樱桃炸弹命中僵尸！ID: " + zombie.getId() + ", 造成10000点伤害，剩余生命值: " + zombie.getHealth().current() + ", 僵尸类型: " + zombie.getType().name());
            } else {
                System.out.println("僵尸在爆炸范围外，ID: " + zombie.getId() + ", 距离: " + distance);
            }
        }
    }
    
    /**
     * 获取植物所在的车道索引
     * @return 车道索引
     */
    public int getLaneIndex() {
        // 假设position的y坐标可以转换为车道索引
        // 实际实现可能需要根据游戏坐标系调整
        return (int)(position.y() / 82);
    }
    
    /**
     * 获取攻击冷却时间（毫秒）
     * @return 冷却时间
     */
    private long getAttackCooldown() {
        switch (type) {
            case PEASHOOTER -> { return 1500; } // 1.5秒
            case REPEATER -> { return 1500; }   // 1.5秒
            case SUNFLOWER, WALLNUT, CHERRY_BOMB -> { return Long.MAX_VALUE; } // 这些植物没有攻击冷却
            default -> { return 1000; }        // 默认1秒
        }
    }
    
    /**
     * 向日葵生产阳光
     * @param currentTime 当前时间
     * @return 是否生产了阳光
     */
    public boolean produceSun(long currentTime) {
        if (type != PlantType.SUNFLOWER) {
            return false;
        }

        // 确保lastProduceTime已初始化
        if (lastProduceTime == 0) {
            lastProduceTime = currentTime;
            return false;
        }

        // 向日葵每10秒生产一个阳光
        if (currentTime - lastProduceTime >= 10000) {
            System.out.println("向日葵生产阳光: " + currentTime + " - " + lastProduceTime + " >= 10000");
            lastProduceTime = currentTime;
            return true;
        }

        return false;
    }

    /**
     * 检查樱桃炸弹是否应该爆炸
     * @param currentTime 当前时间
     * @param zombies 僵尸列表
     * @return 是否已爆炸
     */
    public boolean checkAndExplodeIfNeeded(long currentTime, List<Zombie> zombies) {
        if (type != PlantType.CHERRY_BOMB) {
            return false;
        }

        // 添加详细调试日志
        System.out.println("=== 樱桃炸弹检查爆炸 ===");
        System.out.println("当前时间: " + currentTime);
        System.out.println("种植时间: " + plantedTime);
        System.out.println("是否已爆炸: " + exploded);
        System.out.println("时间差: " + (currentTime - plantedTime) + " 毫秒");

        boolean timeCondition = currentTime - plantedTime >= 1100;
        boolean plantedTimeSet = plantedTime > 0;
        boolean notExploded = !exploded;

        System.out.println("条件检查: 时间差>=2000ms? " + timeCondition);
        System.out.println("条件检查: 种植时间已设置? " + plantedTimeSet);
        System.out.println("条件检查: 未爆炸? " + notExploded);

        if (notExploded && plantedTimeSet && timeCondition) {
            exploded = true;
            System.out.println("所有条件满足，准备爆炸！僵尸列表大小: " + (zombies != null ? zombies.size() : "null"));

            if (zombies != null && !zombies.isEmpty()) {
                // 樱桃炸弹爆炸，对范围内所有僵尸造成伤害
                explode(zombies);
            } else {
                System.out.println("没有找到僵尸或僵尸列表为空");
            }

            // 爆炸后自我销毁
            takeDamage(health.current());
            System.out.println("樱桃炸弹已爆炸并自我销毁，剩余生命值: " + health.current());
            return true;
        } else {
            System.out.println("条件不满足，不爆炸");
            if (!timeCondition) {
                System.out.println("剩余等待时间: " + (1100 - (currentTime - plantedTime)) + " 毫秒");
            }
        }

        return false;
    }
}