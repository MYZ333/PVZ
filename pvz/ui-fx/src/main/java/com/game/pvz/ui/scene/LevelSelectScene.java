package com.game.pvz.ui.scene;

import com.game.pvz.ui.app.Router;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 选关场景（类）
 */
public class LevelSelectScene extends Scene {
    
    public LevelSelectScene() {
        super(new Pane());
        initialize();
    }
    
    private void initialize() {
        Pane root = (Pane) getRoot();
        root.setStyle("-fx-background-color: #2a9d8f;");
        
        // 创建VBox作为布局容器
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefSize(800, 600);
        
        // 添加标题
        Text title = new Text("选择关卡");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setStyle("-fx-fill: #e9c46a;");
        
        // 创建关卡按钮网格
        GridPane levelGrid = new GridPane();
        levelGrid.setAlignment(Pos.CENTER);
        levelGrid.setHgap(20);
        levelGrid.setVgap(20);
        
        // 添加几个关卡按钮（这里简单实现3个关卡）
        for (int i = 1; i <= 3; i++) {
            Button levelButton = createLevelButton(i);
            levelGrid.add(levelButton, (i - 1) % 3, (i - 1) / 3);
        }
        
        // 添加返回主菜单按钮
        Button backButton = new Button("返回主菜单");
        backButton.setPrefSize(150, 40);
        backButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        backButton.setOnAction(e -> {
            Router.getInstance().showMenuScene();
        });
        
        // 将元素添加到布局中
        layout.getChildren().addAll(title, levelGrid, backButton);
        root.getChildren().add(layout);
    }
    
    private Button createLevelButton(int levelNumber) {
        Button button = new Button("关卡 " + levelNumber);
        button.setPrefSize(120, 80);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        // 为关卡按钮设置点击事件
        int level = levelNumber; // 保存levelNumber的副本，避免闭包问题
        button.setOnAction(e -> {
            Router.getInstance().showBattleScene(level);
        });
        
        return button;
    }
}