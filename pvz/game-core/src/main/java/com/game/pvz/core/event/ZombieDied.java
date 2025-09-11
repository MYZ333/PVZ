package com.game.pvz.core.event;

import com.game.pvz.module.entity.zombie.Zombie;
import java.time.Instant;
import java.util.UUID;

/**
 * 僵尸死亡事件（记录类）
 * 当僵尸生命值降至0时触发
 */
public record ZombieDied(
        Zombie zombie,      // 死亡的僵尸
        int laneIndex,      // 死亡时所在的车道索引
        boolean droppedSun, // 是否掉落阳光
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
        return "ZombieDied[" +
               "zombieType=" + zombie.getType() + ", " +
               "laneIndex=" + laneIndex + ", " +
               "droppedSun=" + droppedSun +
               "]";
    }
}