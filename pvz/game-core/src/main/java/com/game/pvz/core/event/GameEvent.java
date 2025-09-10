package com.game.pvz.core.event;

import java.time.Instant;
import java.util.UUID;

/**
 * 所有游戏事件的接口
 */
public interface GameEvent {
    UUID getEventId();
    Instant getTimestamp();
}