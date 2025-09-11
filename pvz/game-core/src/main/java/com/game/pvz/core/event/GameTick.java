package com.game.pvz.core.event;

import java.time.Duration;
//import com.game.pvz.module.entity.plant.Plant;
import java.time.Instant;
import java.util.UUID;
/**
 * 每帧逻辑触发事件（记录类）
 * 记录游戏主循环每帧的时间信息
 */
public record GameTick(
        long frameCount,           // 当前帧计数
        Duration deltaTime,        // 两帧之间的时间差
        Duration elapsedTime,      // 自游戏开始以来的总时间
        UUID eventId,              // 事件ID
        Instant timestamp          // 事件时间戳
) implements GameEvent
{

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
        return "GameTick[" +
               "frameCount=" + frameCount + ", " +
               "deltaTime=" + deltaTime.toMillis() + "ms, " +
               "elapsedTime=" + elapsedTime.toSeconds() + "s" +
               "]";
    }
}