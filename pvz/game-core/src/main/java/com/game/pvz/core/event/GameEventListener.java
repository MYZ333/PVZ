package com.game.pvz.core.event;

public interface GameEventListener<T extends GameEvent> {
    void onEvent(T event);
}
