package com.game.pvz.module.entity.zombie;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Health;
import com.game.pvz.module.entity.Position;

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
    
    protected Zombie(ZombieType type, Position position, Health health, int laneIndex) {
        this.type = type;
        this.position = position;
        this.health = health;
        this.laneIndex = laneIndex;
        this.isDead = false;
        this.lastAttackTime = 0;
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
}