package com.game.pvz.ui.scene;

import com.game.pvz.ui.app.Router;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

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
import java.util.ArrayList;
import java.util.List;
import javafx.stage.WindowEvent;

/**
 * 主菜单场景（类）
 */
public class MenuScene extends Scene {

    // 存储打开的子窗口
    private List<Stage> openWindows = new ArrayList<>();
    // 存储所有按钮
    private List<Button> allButtons = new ArrayList<>();
    // 主舞台引用
    private Stage primaryStage;
    // 开始游戏按钮的图片视图
    private ImageView startButtonView;
    // 开始按钮图片的放大倍数
    private double startButtonImageScale = 1.48; // 默认放大1.2倍
    // 退出按钮图片视图
    private ImageView exitButtonView;
    // 退出按钮图片的放大倍数（代码可控，可通过修改此值调整按钮大小）
    private double exitButtonImageScale = 1.28;
    // 帮助按钮图片视图
    private ImageView helpButtonView;
    // 帮助按钮图片的放大倍数
    private double helpButtonImageScale = 1.24;
    // 设置按钮图片视图
    private ImageView settingsButtonView;
    // 设置按钮图片的放大倍数
    private double settingsButtonImageScale = 1.45;
    // 返回菜单按钮图片视图
    private ImageView closeButtonView;
    // 返回菜单按钮图片的放大倍数
    private double closeButtonImageScale = 1.14; // 默认不放大
    // 帮助菜单返回按钮图片视图
    private ImageView helpCloseButtonView;
    // 帮助菜单返回按钮图片的放大倍数
    private double helpCloseButtonImageScale = 1.28; // 默认不放大

    
    public MenuScene() {
        super(new Pane());
        initialize();

        // 添加窗口关闭事件监听器
        this.setOnKeyPressed(event -> {
            // 这里可以根据需要添加键盘关闭逻辑
        });
    }
    
    // 设置主舞台引用
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        // 添加主舞台关闭事件监听器
        stage.setOnCloseRequest(event -> {
            // 关闭所有打开的子窗口
            closeAllOpenWindows();
        });
    }
    
    // 启用/禁用所有按钮
    private void setAllButtonsDisabled(boolean disabled) {
        for (Button button : allButtons) {
            button.setDisable(disabled);
        }
    }
    
    // 关闭所有打开的子窗口
    private void closeAllOpenWindows() {
        for (Stage stage : openWindows) {
            if (stage != null && stage.isShowing()) {
                stage.close();
            }
        }
        openWindows.clear();
    }
    
    // 添加窗口到打开的窗口列表
    private void addWindowToOpenList(Stage stage) {
        openWindows.add(stage);
        // 禁用所有按钮
        setAllButtonsDisabled(true);
        // 添加窗口关闭事件，当子窗口关闭时重新启用按钮
        stage.setOnCloseRequest(event -> {
            openWindows.remove(stage);
            if (openWindows.isEmpty()) {
                setAllButtonsDisabled(false);
            }
        });
        // 确保窗口真正关闭时也能处理
        stage.setOnHidden(event -> {
            openWindows.remove(stage);
            if (openWindows.isEmpty()) {
                setAllButtonsDisabled(false);
            }
        });
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
        // 创建开始游戏按钮的图片视图
        Image startButtonImage = ResourcePool.getInstance().getImage("/pic/kaishi2liang.png");
        if (startButtonImage != null) {
            this.startButtonView = new ImageView(startButtonImage);
            
            // 使用放大倍数控制图片大小
            double originalWidth = 480; // 原始宽度
            double originalHeight = 150; // 原始高度
            double scaledWidth = originalWidth * startButtonImageScale;
            double scaledHeight = originalHeight * startButtonImageScale;
            
            this.startButtonView.setFitWidth(scaledWidth);
            this.startButtonView.setFitHeight(scaledHeight);
            this.startButtonView.setPreserveRatio(true);
            this.startButtonView.setVisible(false); // 初始状态下不显示
            startButton.setGraphic(this.startButtonView);
            
            // 调整按钮大小以适应放大后的图片
            startButton.setPrefSize(scaledWidth, scaledHeight + 19); // 保留原来的额外高度
        }
        
        startButton.setLayoutX(483); // 开始游戏按钮X坐标
        startButton.setLayoutY(88); // 开始游戏按钮Y坐标
        startButton.setBackground(Background.EMPTY);
        startButton.setBorder(Border.EMPTY);
        
        // 鼠标停放时显示图片
        startButton.setOnMouseEntered(e -> {
            if (this.startButtonView != null) {
                this.startButtonView.setVisible(true);
            }
        });
        
        // 鼠标离开时隐藏图片
        startButton.setOnMouseExited(e -> {
            if (this.startButtonView != null && !startButton.isPressed()) {
                this.startButtonView.setVisible(false);
            }
        });
        
        // 点击时显示图片并移动位置
        startButton.setOnMousePressed(e -> {
            if (this.startButtonView != null) {
                this.startButtonView.setVisible(true);
                startButton.setLayoutX(startButton.getLayoutX() + 3);
                startButton.setLayoutY(startButton.getLayoutY() + 3);
            }
        });
        
        // 释放鼠标时恢复位置
        startButton.setOnMouseReleased(e -> {
            if (this.startButtonView != null) {
                startButton.setLayoutX(startButton.getLayoutX() - 5);
                startButton.setLayoutY(startButton.getLayoutY() - 5);
            }
        });
        
        startButton.setOnAction(e -> {
            Router.getInstance().showLevelSelectScene();
        });
        root.getChildren().add(startButton);
        // 添加到按钮列表
        allButtons.add(startButton);

        // 添加查看帮助按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button viewImageButton = new Button("");
        // 设置按钮大小以适应放大后的图片
        viewImageButton.setPrefSize(75 * this.helpButtonImageScale, 75 * this.helpButtonImageScale);
        viewImageButton.setLayoutX(957); // 查看帮助按钮X坐标
        viewImageButton.setLayoutY(765); // 查看帮助按钮Y坐标
        viewImageButton.setBackground(Background.EMPTY);
        viewImageButton.setBorder(Border.EMPTY);
        
        // 创建帮助按钮的图片视图
        Image helpButtonImage = ResourcePool.getInstance().getImage("/pic/bangzhuliang.png");
        if (helpButtonImage != null) {
            this.helpButtonView = new ImageView(helpButtonImage);
            // 使用缩放因子计算图片尺寸
            this.helpButtonView.setFitWidth(75 * this.helpButtonImageScale);
            this.helpButtonView.setFitHeight(75 * this.helpButtonImageScale);
            this.helpButtonView.setPreserveRatio(true);
            this.helpButtonView.setVisible(false); // 初始状态下不显示
            viewImageButton.setGraphic(this.helpButtonView);
        }
        
        // 鼠标停放时显示图片
        viewImageButton.setOnMouseEntered(e -> {
            if (this.helpButtonView != null) {
                this.helpButtonView.setVisible(true);
            }
        });
        
        // 鼠标离开时隐藏图片
        viewImageButton.setOnMouseExited(e -> {
            if (this.helpButtonView != null && !viewImageButton.isPressed()) {
                this.helpButtonView.setVisible(false);
            }
        });
        
        // 鼠标按下时保持显示图片但不移动
        viewImageButton.setOnMousePressed(e -> {
            if (this.helpButtonView != null) {
                this.helpButtonView.setVisible(true);
            }
        });
        
        // 鼠标释放时处理
        viewImageButton.setOnMouseReleased(e -> {
            // 释放时不需要特殊处理
        });
        
        viewImageButton.setOnAction(e -> {
            // 创建弹出窗口
            Stage popupStage = new Stage();
            popupStage.setTitle("");
            popupStage.setResizable(false);
            // 设置窗口背景透明
            popupStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            // 设置窗口置顶
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            // 设置窗口所有者，确保置顶
            if (primaryStage != null) {
                popupStage.initOwner(primaryStage);
            }
            // 添加到打开的窗口列表
            addWindowToOpenList(popupStage);

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
            closeButton.setPrefSize(180, 40);
            // 设置按钮位置（可以通过代码调节这些值）
            closeButton.setLayoutX(310); // 关闭按钮X坐标
            closeButton.setLayoutY(510); // 关闭按钮Y坐标
            closeButton.setBackground(Background.EMPTY);
            closeButton.setBorder(Border.EMPTY);
            
            // 创建返回菜单按钮的图片视图
            Image helpCloseButtonImage = ResourcePool.getInstance().getImage("/pic/bangzhuliang2.png");
            if (helpCloseButtonImage != null) {
                this.helpCloseButtonView = new ImageView(helpCloseButtonImage);
                // 使用放大倍数控制图片大小
                double originalWidth = 180; // 原始宽度
                double originalHeight = 40; // 原始高度
                double scaledWidth = originalWidth * this.helpCloseButtonImageScale;
                double scaledHeight = originalHeight * this.helpCloseButtonImageScale;
                
                this.helpCloseButtonView.setFitWidth(scaledWidth);
                this.helpCloseButtonView.setFitHeight(scaledHeight);
                this.helpCloseButtonView.setPreserveRatio(true);
                this.helpCloseButtonView.setVisible(false); // 初始状态下不显示
                closeButton.setGraphic(this.helpCloseButtonView);
                
                // 鼠标停放时显示图片
                closeButton.setOnMouseEntered(event -> {
                    this.helpCloseButtonView.setVisible(true);
                });
                
                // 鼠标离开时隐藏图片（除非按钮被按下）
                closeButton.setOnMouseExited(event -> {
                    if (!closeButton.isPressed()) {
                        this.helpCloseButtonView.setVisible(false);
                    }
                });
                
                // 鼠标按下时显示图片并向下移动3px
                closeButton.setOnMousePressed(event -> {
                    this.helpCloseButtonView.setVisible(true);
                    closeButton.setLayoutY(closeButton.getLayoutY() + 3);
                });
                
                // 鼠标释放时恢复位置并关闭窗口
                closeButton.setOnMouseReleased(event -> {
                    closeButton.setLayoutY(closeButton.getLayoutY() - 3);
                    popupStage.close();
                });
            }
            
            // 保留原来的点击事件，确保功能正常
            closeButton.setOnAction(event -> popupStage.close());

            // 创建一个Pane作为容器，以便使用绝对定位
            Pane contentPane = new Pane();
            contentPane.setStyle("-fx-background-color: transparent;");
            contentPane.getChildren().addAll(imageView, closeButton);

            // 将容器添加到布局
            popupLayout.getChildren().add(contentPane);

            // 设置场景并显示
            Scene popupScene = new Scene(popupLayout);
            // 设置场景填充为透明
            popupScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            popupStage.setScene(popupScene);
            popupStage.show();
        });
        root.getChildren().add(viewImageButton);
        // 添加到按钮列表
        allButtons.add(viewImageButton);

        // 添加设置按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button settingsButton = new Button("");
        // 设置按钮大小以适应放大后的图片
        settingsButton.setPrefSize(75 * this.settingsButtonImageScale, 75 * this.settingsButtonImageScale);
        settingsButton.setLayoutX(848); // 设置按钮X坐标
        settingsButton.setLayoutY(711); // 设置按钮Y坐标
        settingsButton.setBackground(Background.EMPTY);
        settingsButton.setBorder(Border.EMPTY);
        
        // 创建设置按钮的图片视图
        Image settingsButtonImage = ResourcePool.getInstance().getImage("/pic/shezhiliang.png");
        if (settingsButtonImage != null) {
            this.settingsButtonView = new ImageView(settingsButtonImage);
            // 使用缩放因子计算图片尺寸
            this.settingsButtonView.setFitWidth(75 * this.settingsButtonImageScale);
            this.settingsButtonView.setFitHeight(75 * this.settingsButtonImageScale);
            this.settingsButtonView.setPreserveRatio(true);
            this.settingsButtonView.setVisible(false); // 初始状态下不显示
            settingsButton.setGraphic(this.settingsButtonView);
        }
        
        // 鼠标停放时显示图片
        settingsButton.setOnMouseEntered(e -> {
            if (this.settingsButtonView != null) {
                this.settingsButtonView.setVisible(true);
            }
        });
        
        // 鼠标离开时隐藏图片
        settingsButton.setOnMouseExited(e -> {
            if (this.settingsButtonView != null && !settingsButton.isPressed()) {
                this.settingsButtonView.setVisible(false);
            }
        });
        
        // 鼠标按下时保持显示图片但不移动
        settingsButton.setOnMousePressed(e -> {
            if (this.settingsButtonView != null) {
                this.settingsButtonView.setVisible(true);
            }
        });
        
        // 鼠标释放时处理
        settingsButton.setOnMouseReleased(e -> {
            // 释放时不需要特殊处理
        });
        
        settingsButton.setOnAction(e -> {
            // 创建设置窗口
            Stage settingsStage = new Stage();
            settingsStage.setTitle("");
            settingsStage.setResizable(false);
            // 设置窗口背景透明
            settingsStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            // 设置窗口置顶
            settingsStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            // 设置窗口所有者，确保置顶
            if (primaryStage != null) {
                settingsStage.initOwner(primaryStage);
            }
            // 添加到打开的窗口列表
            addWindowToOpenList(settingsStage);

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
            // 缩小图片至少一倍
            settingsImageView.setFitWidth(settingsImage.getWidth() / 2);
            settingsImageView.setFitHeight(settingsImage.getHeight() / 2);

            // 添加返回菜单按钮
            Button closeSettingsButton = new Button("");
            closeSettingsButton.setPrefSize(370, 80);
            // 设置按钮位置，可通过代码调节
            closeSettingsButton.setLayoutX(175); // 按钮X坐标
            closeSettingsButton.setLayoutY(507); // 按钮Y坐标(向上调整位置)
            closeSettingsButton.setBackground(Background.EMPTY);
            closeSettingsButton.setBorder(Border.EMPTY);
            
            // 创建返回菜单按钮的图片视图
            Image closeButtonImage = ResourcePool.getInstance().getImage("/pic/shezhiliang2.png");
            if (closeButtonImage != null) {
                this.closeButtonView = new ImageView(closeButtonImage);
                // 使用放大倍数控制图片大小
                double originalWidth = 370; // 原始宽度
                double originalHeight = 80; // 原始高度
                double scaledWidth = originalWidth * this.closeButtonImageScale;
                double scaledHeight = originalHeight * this.closeButtonImageScale;
                
                this.closeButtonView.setFitWidth(scaledWidth);
                this.closeButtonView.setFitHeight(scaledHeight);
                this.closeButtonView.setPreserveRatio(true);
                this.closeButtonView.setVisible(false); // 初始状态下不显示
                closeSettingsButton.setGraphic(this.closeButtonView);
                
                // 鼠标停放时显示图片
                closeSettingsButton.setOnMouseEntered(event -> {
                    this.closeButtonView.setVisible(true);
                });
                
                // 鼠标离开时隐藏图片（除非按钮被按下）
                closeSettingsButton.setOnMouseExited(event -> {
                    if (!closeSettingsButton.isPressed()) {
                        this.closeButtonView.setVisible(false);
                    }
                });
                
                // 鼠标按下时显示图片并向下移动3px
                closeSettingsButton.setOnMousePressed(event -> {
                    this.closeButtonView.setVisible(true);
                    closeSettingsButton.setLayoutY(closeSettingsButton.getLayoutY() + 5);
                });
                
                // 鼠标释放时恢复位置并关闭窗口
                closeSettingsButton.setOnMouseReleased(event -> {
                    closeSettingsButton.setLayoutY(closeSettingsButton.getLayoutY() - 5);
                });
            }
            
            closeSettingsButton.setOnAction(event -> settingsStage.close());
            // 创建透明Pane用于绝对定位
            Pane transparentPane = new Pane();
            transparentPane.setStyle("-fx-background-color: transparent;");
            transparentPane.setPrefSize(settingsImage.getWidth() / 2, settingsImage.getHeight() / 2);
            transparentPane.getChildren().addAll(settingsTitle, difficultyText, settingsImageView, closeSettingsButton);

            // 将Pane添加到布局
            settingsLayout.getChildren().add(transparentPane);

            // 设置场景并显示
            Scene settingsScene = new Scene(settingsLayout);
            // 设置场景填充为透明
            settingsScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            settingsStage.setScene(settingsScene);
            settingsStage.show();
        });
        root.getChildren().add(settingsButton);
        // 添加到按钮列表
        allButtons.add(settingsButton);

        // 添加退出游戏按钮 - 你可以修改下面的坐标值来改变按钮位置
        Button exitButton = new Button("");
        // 设置按钮大小以适应放大后的图片
        exitButton.setPrefSize(75 * this.exitButtonImageScale, 75 * this.exitButtonImageScale);
        exitButton.setLayoutX(1062); // 退出游戏按钮X坐标
        exitButton.setLayoutY(749); // 退出游戏按钮Y坐标
        exitButton.setBackground(Background.EMPTY);
        exitButton.setBorder(Border.EMPTY);
        
        // 创建退出游戏按钮的图片视图
        Image exitButtonImage = ResourcePool.getInstance().getImage("/pic/tuichuliang.png");
        if (exitButtonImage != null) {
            this.exitButtonView = new ImageView(exitButtonImage);
            // 使用缩放因子计算图片尺寸
            this.exitButtonView.setFitWidth(75 * this.exitButtonImageScale);
            this.exitButtonView.setFitHeight(75 * this.exitButtonImageScale);
            this.exitButtonView.setPreserveRatio(true);
            this.exitButtonView.setVisible(false); // 初始状态下不显示
            exitButton.setGraphic(this.exitButtonView);
        }
        
        // 鼠标停放时显示图片
        exitButton.setOnMouseEntered(e -> {
            if (this.exitButtonView != null) {
                this.exitButtonView.setVisible(true);
            }
        });
        
        // 鼠标离开时隐藏图片
        exitButton.setOnMouseExited(e -> {
            if (this.exitButtonView != null && !exitButton.isPressed()) {
                this.exitButtonView.setVisible(false);
            }
        });
        
        // 点击时显示图片，但不移动
        exitButton.setOnMousePressed(e -> {
            if (this.exitButtonView != null) {
                this.exitButtonView.setVisible(true);
            }
        });
        
        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        root.getChildren().add(exitButton);
        // 添加到按钮列表
        allButtons.add(exitButton);

    }
}