package com.game.pvz.ui.app;

import com.game.pvz.ui.scene.BattleScene;
import com.game.pvz.ui.scene.LevelSelectScene;
import com.game.pvz.ui.scene.MenuScene;
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
        this.primaryStage.setHeight(700);
        this.primaryStage.setResizable(false);
        
        // 默认显示主菜单场景
        showMenuScene();
        
        this.primaryStage.show();
    }
    
    public void showMenuScene() {
        MenuScene menuScene = new MenuScene();
        setScene(menuScene);
    }
    
    public void showLevelSelectScene() {
        LevelSelectScene levelSelectScene = new LevelSelectScene();
        setScene(levelSelectScene);
    }
    
    public void showBattleScene(int level) {
        BattleScene battleScene = new BattleScene(level);
        setScene(battleScene);
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