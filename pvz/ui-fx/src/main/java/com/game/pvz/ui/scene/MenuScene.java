package com.game.pvz.ui.scene;

import com.game.pvz.ui.app.Router;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 主菜单场景（类）
 */
public class MenuScene extends Scene {
    
    public MenuScene() {
        super(new Pane());
        initialize();
    }
    
    private void initialize() {
        Pane root = (Pane) getRoot();
        root.setStyle("-fx-background-color: #2a9d8f;");
        
        // 创建VBox作为布局容器
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(800, 600);
        
        // 添加标题
        Text title = new Text("植物大战僵尸");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setStyle("-fx-fill: #e9c46a;");
        
        // 添加开始游戏按钮
        Button startButton = new Button("开始游戏");
        startButton.setPrefSize(150, 50);
        startButton.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        startButton.setOnAction(e -> {
            Router.getInstance().showLevelSelectScene();
        });
        
        // 添加退出按钮
        Button exitButton = new Button("退出游戏");
        exitButton.setPrefSize(150, 50);
        exitButton.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        
        // 将元素添加到布局中
        layout.getChildren().addAll(title, startButton, exitButton);
        root.getChildren().add(layout);
    }
}