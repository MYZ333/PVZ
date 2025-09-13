package com.game.pvz.ui.app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX Application 入口
 */
public class PvzApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Router router = Router.getInstance();
        router.initialize(primaryStage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}