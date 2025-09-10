package com.game.pvz.ui.map;

import javafx.scene.canvas.GraphicsContext;

/**
 * 【接口】地图渲染器
 */
public interface MapRenderer {
    void render(GraphicsContext gc);
    void update(double deltaTime);
    void setViewport(MapViewport viewport);
}