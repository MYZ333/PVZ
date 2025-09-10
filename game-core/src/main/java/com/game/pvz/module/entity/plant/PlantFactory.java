package com.game.pvz.module.entity.plant;

import com.game.pvz.module.entity.Health;
import com.game.pvz.module.entity.Position;

/**
 * 植物工厂（类）
 * 负责创建各种类型的植物实例
 */
public class PlantFactory {
    
    private static final PlantFactory INSTANCE = new PlantFactory();
    
    private PlantFactory() {
        // 私有构造函数，防止外部实例化
    }
    
    public static PlantFactory getInstance() {
        return INSTANCE;
    }
    
    /**
     * 创建指定类型的植物
     */
    public Plant createPlant(PlantType type, Position position) {
        // 根据植物类型设置不同的初始血量
        int maxHealth = switch (type) {
            case WALLNUT -> 500;
            case SUNFLOWER, PEASHOOTER, REPEATER -> 300;
            case CHERRY_BOMB -> 50;
            default -> 300;
        };
        
        Health health = Health.full(maxHealth);
        return new Plant(type, position, health);
    }
    
    /**
     * 创建豌豆射手
     */
    public Plant createPeashooter(Position position) {
        return createPlant(PlantType.PEASHOOTER, position);
    }
    
    /**
     * 创建向日葵
     */
    public Plant createSunflower(Position position) {
        return createPlant(PlantType.SUNFLOWER, position);
    }
    
    /**
     * 创建坚果墙
     */
    public Plant createWallnut(Position position) {
        return createPlant(PlantType.WALLNUT, position);
    }
    
    /**
     * 创建樱桃炸弹
     */
    public Plant createCherryBomb(Position position) {
        return createPlant(PlantType.CHERRY_BOMB, position);
    }
    
    /**
     * 创建双发射手
     */
    public Plant createRepeater(Position position) {
        return createPlant(PlantType.REPEATER, position);
    }
}