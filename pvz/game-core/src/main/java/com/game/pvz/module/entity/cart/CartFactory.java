package com.game.pvz.module.entity.cart;

import com.game.pvz.module.entity.Position;

/**
 * 小推车工厂类
 * 负责创建各种类型的小推车实例
 */
public class CartFactory {

    private static final CartFactory INSTANCE = new CartFactory();

    private CartFactory() {
        // 私有构造函数，防止外部实例化
    }

    public static CartFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 创建指定类型的小推车
     */
    public Cart createCart(CartType type, Position position, int laneIndex) {
        return new Cart(type, position, laneIndex);
    }

    /**
     * 创建默认小推车
     */
    public Cart createDefaultCart(Position position, int laneIndex) {
        return createCart(CartType.DEFAULT, position, laneIndex);
    }
}