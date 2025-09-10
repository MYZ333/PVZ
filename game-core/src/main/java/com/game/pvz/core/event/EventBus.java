package com.game.pvz.core.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 事件总线，负责游戏内事件的发布和订阅
 */
public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    
    private final Map<Class<? extends GameEvent>, List<GameEventListener<? extends GameEvent>>> listeners;
    
    private EventBus() {
        this.listeners = new ConcurrentHashMap<>();
    }
    
    public static EventBus getInstance() {
        return INSTANCE;
    }
    
    /**
     * 注册事件监听器
     */
    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void register(Class<T> eventType, GameEventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add((GameEventListener<? extends GameEvent>) listener);
    }
    
    /**
     * 移除事件监听器
     */
    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void unregister(Class<T> eventType, GameEventListener<T> listener) {
        List<GameEventListener<? extends GameEvent>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove((GameEventListener<? extends GameEvent>) listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }
    
    /**
     * 发布事件
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends GameEvent> void publish(T event) {
        Class<? extends GameEvent> eventType = event.getClass();
        List<GameEventListener<? extends GameEvent>> eventListeners = listeners.get(eventType);
        
        if (eventListeners != null) {
            for (GameEventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }
    
    /**
     * 清除所有监听器
     */
    public void clear() {
        listeners.clear();
    }
}