package com.game.pvz.ui.component;

import javafx.scene.control.Label;

/**
 * 阳光数值标签（类）
 */
public class SunLabel extends Label {
    
    private int sunAmount;
    
    public SunLabel(int initialAmount) {
        this.sunAmount = initialAmount;
        initialize();
        updateText();
    }
    
    private void initialize() {
        // 实现阳光标签的初始化逻辑
    }
    
    public int getSunAmount() {
        return sunAmount;
    }
    
    public void setSunAmount(int amount) {
        this.sunAmount = amount;
        updateText();
    }
    
    public void addSun(int amount) {
        this.sunAmount += amount;
        updateText();
    }
    
    public void removeSun(int amount) {
        this.sunAmount -= amount;
        if (this.sunAmount < 0) {
            this.sunAmount = 0;
        }
        updateText();
    }
    
    private void updateText() {
        setText("阳光: " + sunAmount);
    }
}