package com.game.pvz.physics;

/**
 * 刚体数据（记录类）
 */
public record PhysicsBody(
        double mass,
        double friction,
        double restitution,
        boolean isStatic
) {
    
    public static PhysicsBody createDynamic(double mass, double friction, double restitution) {
        return new PhysicsBody(mass, friction, restitution, false);
    }
    
    public static PhysicsBody createStatic(double friction, double restitution) {
        return new PhysicsBody(0, friction, restitution, true);
    }
}