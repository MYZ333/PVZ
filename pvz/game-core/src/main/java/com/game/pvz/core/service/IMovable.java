package com.game.pvz.core.service;

public interface IMovable {
    void move(double dx,double dy);
    double getX();
    double getY();
    void setVelocity(double dx,double dy);
}
