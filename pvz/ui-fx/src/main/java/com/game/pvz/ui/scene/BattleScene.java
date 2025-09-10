package com.game.pvz.ui.scene;

import javafx.scene.Scene;

/**
 * 战场场景（类）
 */
public class BattleScene extends Scene {
    
    private int level;
    
    public BattleScene(int level) {
        super(null);
        this.level = level;
        initialize();
    }
    
    private void initialize() {
        // 实现战场场景的初始化逻辑
    }
    
    public int getLevel() {
        return level;
    }
    
    public void startBattle() {
        // 实现开始战斗的逻辑
    }
    
    public void stopBattle() {
        // 实现停止战斗的逻辑
    }
}