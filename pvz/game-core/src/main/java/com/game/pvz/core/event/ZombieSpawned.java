package com.game.pvz.core.event;

import com.game.pvz.module.entity.zombie.Zombie;
import java.time.Instant;
import java.util.UUID;

/**
 * 僵尸生成事件（记录类）
 * 当僵尸被生成时触发
 */
public record ZombieSpawned(
        Zombie zombie,      // 生成的僵尸
        int laneIndex,      // 生成的车道索引
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
        return "ZombieSpawned[" +
               "zombieType=" + zombie.getType() + ", " +
               "laneIndex=" + laneIndex + ", " +
               "position=" + zombie.getPosition() +
               "]";
    }
}