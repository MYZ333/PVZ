package com.game.pvz.core.event;

import com.game.pvz.module.entity.plant.Plant;
import java.time.Instant;
import java.util.UUID;

/**
 * 植物受到伤害事件（记录类）
 * 当植物受到伤害时触发
 */
public record PlantDamaged(
        Plant plant,        // 受到伤害的植物
        int damage,         // 伤害值
        int remainingHealth, // 剩余生命值
        UUID eventId,       // 事件ID
        Instant timestamp   // 事件时间戳
) implements GameEvent {
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "PlantDamaged[" +
               "plantType=" + plant.getType() + ", " +
               "damage=" + damage + ", " +
               "remainingHealth=" + remainingHealth + "/" + plant.getHealth().max() +
               "]";
    }
}