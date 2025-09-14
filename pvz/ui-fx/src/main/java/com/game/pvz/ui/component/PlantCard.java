package com.game.pvz.ui.component;

import javafx.scene.control.Button;

/**
 * 植物卡片按钮（类）
 */
public class PlantCard extends Button {
    
    private String plantType;
    private int cost;
    private boolean isReady;
    
    public PlantCard(String plantType, int cost) {
        this.plantType = plantType;
        this.cost = cost;
        this.isReady = true;
        initialize();
    }
    
    private void initialize() {
        // 实现植物卡片的初始化逻辑
    }
    
    public String getPlantType() {
        return plantType;
    }
    
    public int getCost() {
        return cost;
    }
    
    public boolean isReady() {
        return isReady;
    }
    
    public void setReady(boolean ready) {
        isReady = ready;
    }
    
    public void startCooldown() {
        // 实现开始冷却的逻辑
    }
}