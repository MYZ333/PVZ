package com.game.pvz.core.event;

/**
 * 游戏事件监听器接口
 */
@FunctionalInterface
public interface GameEventListener<T extends GameEvent> {
    /**
     * 处理游戏事件
     */
    void onEvent(T event);
}