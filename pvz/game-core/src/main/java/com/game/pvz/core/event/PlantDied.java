package com.game.pvz.core.event;

import com.game.pvz.module.entity.plant.Plant;
import java.time.Instant;
import java.util.UUID;

/**
 * 植物死亡事件（记录类）
 * 当植物生命值降至0时触发
 */
public record PlantDied(
        Plant plant,        // 死亡的植物
        int laneIndex,      // 死亡时所在的车道索引
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
        return "PlantDied[" +
               "plantType=" + plant.getType() + ", " +
               "laneIndex=" + laneIndex +
               "]";
    }
}