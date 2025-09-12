package com.game.pvz.module.entity.zombie;

import com.game.pvz.module.entity.Health;
import com.game.pvz.module.entity.Position;

/**
 * 僵尸工厂（类）
 * 负责创建各种类型的僵尸实例
 */
public class ZombieFactory {
    
    private static final ZombieFactory INSTANCE = new ZombieFactory();
    
    private ZombieFactory() {
        // 私有构造函数，防止外部实例化
    }
    
    public static ZombieFactory getInstance() {
        return INSTANCE;
    }
    
    /**
     * 创建指定类型的僵尸
     */
    public Zombie createZombie(ZombieType type, Position position, int laneIndex) {
        Health health = Health.full(type.getHealth());
        return new Zombie(type, position, health, laneIndex);
    }
    
    /**
     * 创建普通僵尸
     */
    public Zombie createNormalZombie(Position position, int laneIndex) {
        return createZombie(ZombieType.NORMAL, position, laneIndex);
    }
    
    /**
     * 创建旗帜僵尸
     */
    public Zombie createFlagZombie(Position position, int laneIndex) {
        return createZombie(ZombieType.FLAG, position, laneIndex);
    }
    
    /**
     * 创建路障僵尸
     */
    public Zombie createConeheadZombie(Position position, int laneIndex) {
        return createZombie(ZombieType.CONEHEAD, position, laneIndex);
    }
    
    /**
     * 创建铁桶僵尸
     */
    public Zombie createBucketheadZombie(Position position, int laneIndex) {
        return createZombie(ZombieType.BUCKETHEAD, position, laneIndex);
    }
    
    /**
     * 创建橄榄球僵尸
     */
    public Zombie createFootballZombie(Position position, int laneIndex) {
        return createZombie(ZombieType.FOOTBALL, position, laneIndex);
    }
}