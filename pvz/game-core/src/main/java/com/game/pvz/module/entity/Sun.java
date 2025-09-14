package com.game.pvz.module.entity;

import com.game.pvz.module.entity.GameObject;
import com.game.pvz.module.entity.Position;

/**
 * 阳光实体类
 * 实现点击收集阳光的功能
 */
public class Sun implements GameObject {
    private Position position; // 阳光位置
    private final int value; // 阳光价值
    private boolean collected; // 是否已被收集

    public Sun(Position position, int value) {
        this.position = position;
        this.value = value;
        this.collected = false;
    }

    /**
     * 设置阳光位置
     */
    public void setPosition(Position position) {
        this.position = position;
    }
    /**
     * 获取阳光位置
     */
    public Position getPosition() {
        return position;
    }

    /**
     * 获取阳光价值
     */
    public int getValue() {
        return value;
    }

    /**
     * 检查阳光是否已被收集
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * 收集阳光
     */
    public void collect() {
        this.collected = true;
    }
}