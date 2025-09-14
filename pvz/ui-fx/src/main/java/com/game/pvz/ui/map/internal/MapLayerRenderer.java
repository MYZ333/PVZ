package com.game.pvz.ui.map.internal;

import javafx.scene.canvas.GraphicsContext;

/**
 * 图层渲染器（类）
 */
public class MapLayerRenderer {
    
    private LayerType layerType;
    
    public MapLayerRenderer(LayerType layerType) {
        this.layerType = layerType;
    }
    
    public void render(GraphicsContext gc) {
        // 实现图层渲染的逻辑
    }
    
    public void update(double deltaTime) {
        // 实现图层更新的逻辑
    }
    
    public LayerType getLayerType() {
        return layerType;
    }
    
    public void setLayerType(LayerType layerType) {
        this.layerType = layerType;
    }
}