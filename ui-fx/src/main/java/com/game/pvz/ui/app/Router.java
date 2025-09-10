package com.game.pvz.ui.app;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 场景路由（类）
 * 负责管理不同场景之间的切换
 */
public class Router {
    
    private static final Router INSTANCE = new Router();
    private Stage primaryStage;
    
    private Router() {
        // 私有构造函数，防止外部实例化
    }
    
    public static Router getInstance() {
        return INSTANCE;
    }
    
    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("植物大战僵尸");
        this.primaryStage.setWidth(800);
        this.primaryStage.setHeight(600);
        this.primaryStage.setResizable(false);
        
        // 默认显示主菜单场景
        showMenuScene();
        
        this.primaryStage.show();
    }
    
    public void showMenuScene() {
        // 实现显示主菜单场景的逻辑
    }
    
    public void showLevelSelectScene() {
        // 实现显示选关场景的逻辑
    }
    
    public void showBattleScene(int level) {
        // 实现显示战场场景的逻辑
    }
    
    public void setScene(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}