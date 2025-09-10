package com.game.pvz.ui.component;

import javafx.scene.canvas.Canvas;

/**
 * 战场画布（抽象类，继承Canvas）
 */
public abstract class BattleCanvas extends Canvas {
    
    public BattleCanvas(double width, double height) {
        super(width, height);
        initialize();
    }
    
    protected abstract void initialize();
    
    public abstract void render();
    
    public abstract void update(double deltaTime);
}