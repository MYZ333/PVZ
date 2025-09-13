package com.game.pvz.physics;

import com.game.pvz.module.entity.Position;

/**
 * 物理工具（类）
 */
public class PhysicsUtils {
    
    private PhysicsUtils() {
        // 私有构造函数，防止外部实例化
    }
    
    /**
     * 检查两个包围盒是否相交
     */
    public static boolean checkCollision(Bounds a, Bounds b) {
        return a.intersects(b);
    }
    
    /**
     * 计算两点之间的距离
     */
    public static double distance(Position a, Position b) {
        return a.distance(b);
    }
    
    /**
     * 线性插值
     */
    public static double lerp(double start, double end, double t) {
        return start + (end - start) * Math.max(0, Math.min(1, t));
    }
    
    /**
     * 限制值在指定范围内
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}