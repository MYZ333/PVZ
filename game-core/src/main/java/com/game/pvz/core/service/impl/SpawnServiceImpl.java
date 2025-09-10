package com.game.pvz.core.service.impl;

import com.game.pvz.core.service.SpawnService;

/**
 * 刷怪实现
 */
class SpawnServiceImpl implements SpawnService {
    
    private double spawnRate = 1.0;
    
    @Override
    public void spawnZombie() {
        // 实现刷怪逻辑
    }
    
    @Override
    public void spawnSun() {
        // 实现刷阳光逻辑
    }
    
    @Override
    public void setSpawnRate(double rate) {
        this.spawnRate = rate;
    }
}