package com.game.pvz.ui.map;

/**
 * 【接口】摄像机
 */
public interface MapViewport {
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    void setPosition(double x, double y);
    void setSize(double width, double height);
    void centerOn(double x, double y);
    boolean contains(double x, double y);
}