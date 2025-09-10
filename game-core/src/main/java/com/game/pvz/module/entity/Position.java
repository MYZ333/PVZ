package com.game.pvz.module.entity;

/**
 * 不可变坐标（记录类）
 */
public record Position(
        double x,  // X坐标
        double y   // Y坐标
) {
    
    /**
     * 创建新的位置，通过偏移当前位置
     */
    public Position offset(double dx, double dy) {
        return new Position(this.x + dx, this.y + dy);
    }
    
    /**
     * 计算与另一个点的距离
     */
    public double distance(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}