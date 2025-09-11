package com.game.pvz.module.entity.cart;

/**
 * 小推车类型枚举
 */
public enum CartType {
    DEFAULT(500); // 默认小推车，血量为500

    private final int health; // 小推车血量

    CartType(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
