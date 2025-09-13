package com.game.pvz.desktop;

import com.game.pvz.ui.app.PvzApp;
import javafx.application.Application;

/**
 * 游戏启动器
 * 负责启动JavaFX应用程序
 */
public class Launcher {
    
    /**
     * 主入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Application.launch(PvzApp.class, args);
    }
}