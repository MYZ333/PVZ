package com.game.pvz.module.entity.plant;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Health;
import com.game.pvz.module.entity.Position;

/**
 * 植物实体（类）
 */
public class Plant implements GameObject {
    private final PlantType type;
    private final Position position;
    private Health health;
    private long lastAttackTime;
    private boolean isReadyToAttack;
    
    protected Plant(PlantType type, Position position, Health health) {
        this.type = type;
        this.position = position;
        this.health = health;
        this.lastAttackTime = 0;
        this.isReadyToAttack = true;
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
        this.health = this.health.takeDamage(damage);
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
}