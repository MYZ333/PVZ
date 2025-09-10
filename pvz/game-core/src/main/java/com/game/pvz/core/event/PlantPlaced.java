package com.game.pvz.core.event;

import com.game.pvz.module.entity.plant.Plant;
import java.time.Instant;
import java.util.UUID;

/**
 * 植物放置成功事件（记录类）
 * 当植物被成功放置到棋盘上时触发
 */
public record PlantPlaced(
        Plant plant,        // 被放置的植物
        int laneIndex,      // 放置的车道索引
        int rowIndex,       // 放置的行索引
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
        return "PlantPlaced[" +
               "plantType=" + plant.getType() + ", " +
               "position=(" + laneIndex + "," + rowIndex + "), " +
               "health=" + plant.getHealth().current() + "/" + plant.getHealth().max() +
               "]";
    }
}