package com.game.pvz.ui.scene;

import com.game.pvz.ui.app.Router;


import com.game.pvz.ui.app.ResourcePool;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 选关场景（类）
 */
public class LevelSelectScene extends Scene {
    // 关卡按钮的图片视图和放大倍数
    private ImageView[] levelButtonViews = new ImageView[3];
    private double[] levelButtonImageScales = {1.6, 1.6, 1.6}; // 默认不放大
    private double originalButtonWidth = 80; // 原始按钮宽度
    private double originalButtonHeight = 80; // 原始按钮高度
    // 高亮图片的额外放大倍数，可以通过代码调整
    private double highlightImageExtraScale = 1.0; // 高亮图片比原图大10%
    // 关卡1按钮的自定义位置，可以通过代码调整
    private int level1ColIndex = 0; // 关卡1的列索引
    private int level1RowIndex = 0; // 关卡1的行索引
    // 返回按钮图片视图
    private ImageView backButtonView;
    // 返回按钮图片的放大倍数
    private double backButtonImageScale = 2.4; // 默认不放大
    
    public LevelSelectScene() {
        super(new Pane());
        initialize();
    }
    
    private void initialize() {
        Pane root = (Pane) getRoot();


        
        // 添加背景图片
        Image backgroundImage = ResourcePool.getInstance().getImage("/pic/levelback.png");
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

        
        // 创建VBox作为布局容器
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        layout.setPrefSize(1200, 900);
        layout.setStyle("-fx-background-color: transparent;"); // 设为透明，不遮挡背景图

        
        // 添加标题
        Text title = new Text("");
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
            int colIndex;
            int rowIndex;
            
            // 对于关卡1，使用自定义的行列索引位置（代码可控）
            if (i == 1) {
                colIndex = level1ColIndex;
                rowIndex = level1RowIndex;
            } else {
                // 对于其他关卡，使用原来的计算方式
                colIndex = (i - 1) % 3;
                rowIndex = (i - 1) / 3;
            }
            
            // 直接将按钮添加到网格中，不再使用额外的容器
            levelGrid.add(levelButton, colIndex, rowIndex);
        }
        
        // 添加返回主菜单按钮 - 放置在左下角
        Button backButton = new Button("");
        // 设置按钮大小以适应放大后的图片
        backButton.setPrefSize(75 * this.backButtonImageScale, 75 * this.backButtonImageScale);
        backButton.setLayoutX(40); // 左下角X坐标
        backButton.setLayoutY(730); // 左下角Y坐标
        backButton.setBackground(Background.EMPTY);
        backButton.setBorder(Border.EMPTY);
        
        // 创建返回按钮的图片视图
        Image backButtonImage = ResourcePool.getInstance().getImage("/pic/ret.png");
        if (backButtonImage != null) {
            this.backButtonView = new ImageView(backButtonImage);
            // 使用缩放因子计算图片尺寸
            this.backButtonView.setFitWidth(75 * this.backButtonImageScale);
            this.backButtonView.setFitHeight(75 * this.backButtonImageScale);
            this.backButtonView.setPreserveRatio(true);
            this.backButtonView.setVisible(true); // 初始状态下显示默认图片
            backButton.setGraphic(this.backButtonView);
        }
        
        // 鼠标停放时显示高亮图片
        backButton.setOnMouseEntered(e -> {
            if (this.backButtonView != null) {
                Image highlightedImage = ResourcePool.getInstance().getImage("/pic/retliang.png");
                if (highlightedImage != null) {
                    this.backButtonView.setImage(highlightedImage);
                }
            }
        });
        
        // 鼠标离开时恢复默认图片
        backButton.setOnMouseExited(e -> {
            if (this.backButtonView != null) {
                Image defaultImage = ResourcePool.getInstance().getImage("/pic/ret.png");
                if (defaultImage != null) {
                    this.backButtonView.setImage(defaultImage);
                }
            }
        });
        
        // 鼠标按下时没有反应（不移动位置）
        backButton.setOnMousePressed(e -> {
            // 按下时不做任何移动处理
        });
        
        backButton.setOnAction(e -> {
            Router.getInstance().showMenuScene();
        });
        
        // 将元素添加到布局中
        layout.getChildren().addAll(title, levelGrid);
        root.getChildren().add(layout);
        // 直接将返回按钮添加到root Pane中，确保绝对定位生效
        root.getChildren().add(backButton);
    }
    
    private Button createLevelButton(int levelNumber) {
        // 创建一个空文本的按钮
        Button button = new Button("");
        
        // 根据关卡编号加载对应的图片
        String imagePath = String.format("/pic/guanqia%d.png", levelNumber);
        Image levelImage = ResourcePool.getInstance().getImage(imagePath);
        
        if (levelImage != null) {
            // 创建图片视图
            ImageView imageView = new ImageView(levelImage);
            // 保存图片视图到数组中
            levelButtonViews[levelNumber - 1] = imageView;
            
            // 设置图片大小（使用原始大小乘以放大倍数）
            double scaledWidth = originalButtonWidth * levelButtonImageScales[levelNumber - 1];
            double scaledHeight = originalButtonHeight * levelButtonImageScales[levelNumber - 1];
            imageView.setFitWidth(scaledWidth);
            imageView.setFitHeight(scaledHeight);
            imageView.setPreserveRatio(true);
            
            // 设置按钮大小以适应放大后的图片
            button.setPrefSize(scaledWidth, scaledHeight);
            
            // 使用setGraphic方法将图片设置为按钮的图形内容
            // 这样点击图片就等同于点击按钮
            button.setGraphic(imageView);
            
            // 为所有关卡添加鼠标悬停和点击效果
            // 加载对应关卡的高亮图片
            String highlightImagePath = String.format("/pic/guanqia%dliang.png", levelNumber);
            Image highlightImage = ResourcePool.getInstance().getImage(highlightImagePath);
            
            if (highlightImage != null) {
                // 保存当前关卡索引，避免闭包问题
                final int currentLevelIndex = levelNumber - 1;
                
                // 鼠标悬停时切换到高亮图片
                button.setOnMouseEntered(e -> {
                    imageView.setImage(highlightImage);
                    // 高亮图片应用额外的放大倍数（可通过代码控制）
                    double highlightedScale = levelButtonImageScales[currentLevelIndex] * highlightImageExtraScale;
                    imageView.setFitWidth(originalButtonWidth * highlightedScale);
                    imageView.setFitHeight(originalButtonHeight * highlightedScale);
                });
                
                // 鼠标离开时切换回原图
                button.setOnMouseExited(e -> {
                    imageView.setImage(levelImage);
                    // 确保原图大小和设置的一致
                    imageView.setFitWidth(originalButtonWidth * levelButtonImageScales[currentLevelIndex]);
                    imageView.setFitHeight(originalButtonHeight * levelButtonImageScales[currentLevelIndex]);
                });
            }
            
            // 鼠标按下时图片向下移动3px
            button.setOnMousePressed(e -> {
                imageView.setTranslateY(3);
            });
            
            // 鼠标释放时恢复位置
            button.setOnMouseReleased(e -> {
                imageView.setTranslateY(0);
            });
        } else {
            // 如果图片加载失败，使用默认大小
            button.setPrefSize(originalButtonWidth, originalButtonHeight);
            // 显示文本作为备用
            button.setText("关卡 " + levelNumber);
            button.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        }
        
        // 设置按钮样式为透明，不显示边框和背景
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        
        // 为关卡按钮设置点击事件
        int level = levelNumber; // 保存levelNumber的副本，避免闭包问题
        button.setOnAction(event -> {
            Router.getInstance().showBattleScene(level);
        });
        
        return button;
    }
}