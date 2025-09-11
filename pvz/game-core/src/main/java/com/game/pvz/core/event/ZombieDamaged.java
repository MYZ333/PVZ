package com.game.pvz.core.event;

import com.game.pvz.module.entity.zombie.Zombie;
import java.time.Instant;
import java.util.UUID;

/**
 * 僵尸受到伤害事件（记录类）
 * 当僵尸受到伤害时触发
 */
public record ZombieDamaged(
        Zombie zombie,      // 受到伤害的僵尸
        int damage,         // 伤害值
        boolean armorHit,   // 是否击中护甲
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
        return "ZombieDamaged[" +
               "zombieType=" + zombie.getType() + ", " +
               "damage=" + damage + ", " +
               "armorHit=" + armorHit + ", " +
               "remainingHealth=" + zombie.getHealth().current() + "/" + zombie.getHealth().max() +
               "]";
    }
}