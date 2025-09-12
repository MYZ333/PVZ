package com.game.pvz.core.service;

/**
 * 【接口】阳光账户服务
 * 定义阳光账户的基本操作
 */
public interface SunBankService {
    /**
     * 获取当前阳光数量
     * @return 当前阳光数量
     */
    int getSunAmount();
    
    /**
     * 添加阳光
     * @param amount 要添加的阳光数量
     */
    void addSun(int amount);
    
    /**
     * 扣除阳光
     * @param amount 要扣除的阳光数量
     * @return 扣除是否成功
     */
    boolean removeSun(int amount);
    
    /**
     * 设置阳光数量
     * @param amount 要设置的阳光数量
     */
    void setSunAmount(int amount);
}