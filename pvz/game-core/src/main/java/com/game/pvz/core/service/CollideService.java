package com.game.pvz.core.service;

public interface CollideService {
    double getX();
    double getY();
    double getWidth();
    double getHeight();

    default boolean checkCollision(CollideService other){
        return true;
    }
}
