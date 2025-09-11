package com.game.pvz.ui.scene;

import com.game.pvz.ui.app.Router;
import com.game.pvz.ui.app.ResourcePool;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

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
        root.setPrefSize(1200, 900);

        // 添加背景图片
        Image backgroundImage = ResourcePool.getInstance().getImage("/pic/kaishi2.png");
        if (backgroundImage != null) {
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(1200);
            backgroundView.setFitHeight(900);
            backgroundView.setPreserveRatio(false);
            root.getChildren().add(0, backgroundView);
        } else {
            // 如果图片加载失败，使用备用背景色
            root.setStyle("-fx-background-color: #2a9d8f;");
        }

        // 添加标题
        Text title = new Text("");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setStyle("-fx-fill: #e9c46a;");
        title.setLayoutX(450); // 标题X坐标
        title.setLayoutY(200); // 标题Y坐标
        root.getChildren().add(title);

        // 添加开始游戏按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button startButton = new Button("");
        startButton.setPrefSize(480, 150);
        startButton.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        startButton.setLayoutX(620); // 开始游戏按钮X坐标
        startButton.setLayoutY(140); // 开始游戏按钮Y坐标
        startButton.setBackground(Background.EMPTY);
        startButton.setBorder(Border.EMPTY);
        startButton.setOnAction(e -> {
            Router.getInstance().showLevelSelectScene();
        });
        root.getChildren().add(startButton);

        // 添加查看帮助按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button viewImageButton = new Button("");
        viewImageButton.setPrefSize(75, 75);
        viewImageButton.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        viewImageButton.setLayoutX(970); // 查看帮助按钮X坐标
        viewImageButton.setLayoutY(770); // 查看帮助按钮Y坐标
        viewImageButton.setBackground(Background.EMPTY);
        viewImageButton.setBorder(Border.EMPTY);
        viewImageButton.setOnAction(e -> {
            // 创建弹出窗口
            Stage popupStage = new Stage();
            popupStage.setTitle("游戏帮助");
            popupStage.setResizable(false);
            // 设置窗口背景透明
            popupStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

            // 创建布局
            VBox popupLayout = new VBox(20);
            popupLayout.setAlignment(Pos.CENTER);
            popupLayout.setPadding(new Insets(20));
            // 设置布局背景透明
            popupLayout.setStyle("-fx-background-color: transparent;");

            // 添加图片
            Image popupImage = ResourcePool.getInstance().getImage("/pic/bangzhu.png");
            ImageView imageView = new ImageView(popupImage);
            imageView.setPreserveRatio(true);

            // 添加关闭按钮
            Button closeButton = new Button("");
            closeButton.setPrefSize(100, 40);
            closeButton.setOnAction(event -> popupStage.close());

            // 将元素添加到布局
            popupLayout.getChildren().addAll(imageView, closeButton);

            // 设置场景并显示
            Scene popupScene = new Scene(popupLayout);
            popupStage.setScene(popupScene);
            popupStage.show();
        });
        root.getChildren().add(viewImageButton);

        // 添加设置按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button settingsButton = new Button("");
        settingsButton.setPrefSize(75, 75);
        settingsButton.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        settingsButton.setLayoutX(870); // 设置按钮X坐标
        settingsButton.setLayoutY(720); // 设置按钮Y坐标
        settingsButton.setBackground(Background.EMPTY);
        settingsButton.setBorder(Border.EMPTY);
        settingsButton.setOnAction(e -> {
            // 创建设置窗口
            Stage settingsStage = new Stage();
            settingsStage.setTitle("游戏设置");
            settingsStage.setResizable(false);
            // 设置窗口背景透明
            settingsStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

            // 创建布局
            VBox settingsLayout = new VBox(20);
            settingsLayout.setAlignment(Pos.CENTER);
            settingsLayout.setPadding(new Insets(30));
            // 设置布局背景透明
            settingsLayout.setStyle("-fx-background-color: transparent;");

            // 添加设置标题
            Text settingsTitle = new Text("");
            settingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));

            // 添加难度设置文本
            Text difficultyText = new Text("");
            difficultyText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

            // 添加设置背景图片
            Image settingsImage = ResourcePool.getInstance().getImage("/pic/shezhi.png");
            ImageView settingsImageView = new ImageView(settingsImage);
            settingsImageView.setPreserveRatio(true);

            // 添加关闭按钮
            Button closeSettingsButton = new Button("关闭");
            closeSettingsButton.setPrefSize(100, 40);
            closeSettingsButton.setOnAction(event -> settingsStage.close());

            // 将元素添加到布局
            settingsLayout.getChildren().addAll(settingsTitle, difficultyText, settingsImageView, closeSettingsButton);

            // 设置场景并显示
            Scene settingsScene = new Scene(settingsLayout);
            settingsStage.setScene(settingsScene);
            settingsStage.show();
        });
        root.getChildren().add(settingsButton);

        // 添加退出游戏按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button exitButton = new Button("");
        exitButton.setPrefSize(75, 75);
        exitButton.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        exitButton.setLayoutX(1080); // 退出游戏按钮X坐标
        exitButton.setLayoutY(755); // 退出游戏按钮Y坐标
        exitButton.setBackground(Background.EMPTY);
        exitButton.setBorder(Border.EMPTY);
        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        root.getChildren().add(exitButton);
    }
}