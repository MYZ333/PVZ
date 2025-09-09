package com.game.pvz.core.event;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private final List<GameEventListener> listener = new ArrayList<>();

    //注册事件
    public void register(GameEventListener lis){
        listener.add(lis);
    }

    //发布事件，通知所有监听器
    public void post(GameEvent e){
        for(GameEventListener lis : listener){
            lis.onEvent(e);
        }
    }
}
