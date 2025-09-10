package com.game.pvz.physics;

import com.game.pvz.module.entity.Position;

/**
 * 包围盒（记录类）
 */
public record Bounds(
        double x,
        double y,
        double width,
        double height
) {
    
    public static Bounds fromPosition(Position position, double width, double height) {
        return new Bounds(position.x(), position.y(), width, height);
    }
    
    public boolean contains(Position position) {
        return position.x() >= x && position.x() <= x + width &&
               position.y() >= y && position.y() <= y + height;
    }
    
    public boolean intersects(Bounds other) {
        return x < other.x + other.width &&
               x + width > other.x &&
               y < other.y + other.height &&
               y + height > other.y;
    }
}