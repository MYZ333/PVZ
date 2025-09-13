package com.game.pvz.module.entity.cart;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Position;

/**
 * 小推车实体类
 */
public class Cart implements GameObject {
    private final CartType type;
    private Position position;
    private final int laneIndex;
    private boolean isActive;
    private boolean isTriggered;

    protected Cart(CartType type, Position position, int laneIndex) {
        this.type = type;
        this.position = position;
        this.laneIndex = laneIndex;
        this.isActive = true;
        this.isTriggered = false;
    }

    public CartType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    // 更新位置的方法
    public void updatePosition(double deltaX) {
        this.position = new Position(position.x() + deltaX, position.y());
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    public void trigger() {
        this.isTriggered = true;
        // 可以在这里添加触发小推车的逻辑
    }

}