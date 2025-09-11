package com.game.pvz.core.service;

/**
 * 【接口】刷怪/刷阳光
 */
public interface SpawnService {
    void spawnZombie();
    void spawnSun();
    void setSpawnRate(double rate);
}