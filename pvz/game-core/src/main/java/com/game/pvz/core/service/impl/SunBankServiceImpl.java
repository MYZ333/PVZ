package com.game.pvz.core.service.impl;
import com.game.pvz.core.service.SunBankService;
/**
 * 阳光账户实现
 */
public class SunBankServiceImpl implements SunBankService {
    
    private int sunAmount = 50;
    
    public int getSunAmount() {
        return sunAmount;
    }
    
    public void addSun(int amount) {
        this.sunAmount += amount;
    }
    
    public boolean removeSun(int amount) {
        if (this.sunAmount >= amount) {
            this.sunAmount -= amount;
            return true;
        }
        return false;
    }
    
    public void setSunAmount(int amount) {
        this.sunAmount = Math.max(0, amount);
    }
}