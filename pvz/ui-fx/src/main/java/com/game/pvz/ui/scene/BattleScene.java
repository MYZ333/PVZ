package com.game.pvz.ui.scene;

import com.game.pvz.core.event.EventBus;
import com.game.pvz.core.event.GameEventListener;
import com.game.pvz.core.event.ZombieSpawned;
import com.game.pvz.core.service.impl.GameLoopServiceImpl;
import com.game.pvz.core.service.impl.SpawnServiceImpl;
import com.game.pvz.core.service.impl.SunBankServiceImpl;
import com.game.pvz.core.service.GameLoopService;
import com.game.pvz.core.service.SpawnService;
import com.game.pvz.core.service.SunBankService;
import com.game.pvz.module.entity.Sun;
import com.game.pvz.module.entity.plant.Plant;
import com.game.pvz.module.entity.plant.PlantFactory;
import com.game.pvz.module.entity.plant.PlantType;
import com.game.pvz.module.entity.Position;
import com.game.pvz.module.entity.zombie.Zombie;
import com.game.pvz.module.entity.zombie.ZombieFactory;
import com.game.pvz.module.entity.zombie.ZombieType;
import com.game.pvz.ui.app.Router;
import com.game.pvz.ui.app.ResourcePool;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Random;
import com.game.pvz.module.entity.projectile.Projectile;
import com.game.pvz.module.entity.projectile.ProjectileType;
import java.util.concurrent.CopyOnWriteArrayList;
import com.game.pvz.module.entity.cart.Cart;
import com.game.pvz.module.entity.cart.CartFactory;
import com.game.pvz.module.entity.cart.CartType;
/**
 * 战场场景（类）
 */
public class BattleScene extends Scene {
    
    private int level;
    private boolean battleStarted = false;
    private GameLoopService gameLoopService;
    private SunBankService sunBankService;
    private GridPane gameGrid; // 游戏棋盘
    private HBox plantSelector; // 植物选择栏
    private Text sunAmountText; // 阳光数量显示
    private Text battleStatusText; // 战斗状态显示
    private Button startButton;
    private Pane sunLayer; // 用于显示阳光的层
    private Pane zombieLayer; // 用于显示僵尸的层
    private Pane projectileLayer; // 用于显示子弹的层
    private List<Sun> activeSuns; // 活跃的阳光实体列表
    private Random random;
    private PlantType selectedPlantType = null; // 当前选中的植物类型
    private List<Plant> plants = new ArrayList<>(); // 所有植物列表
    private List<Zombie> zombies = new ArrayList<>(); // 所有僵尸列表
    private List<Projectile> projectiles = new CopyOnWriteArrayList<>(); // 使用线程安全的列表
    private Map<String, Pane> plantCells = new HashMap<>(); // 植物单元格映射
    private Map<UUID, ImageView> zombieViews = new HashMap<>(); // 僵尸视图映射
    private Map<UUID, Pane> zombieContainers = new HashMap<>(); // 僵尸容器映射
    private Map<Projectile, Pane> projectileViews = new HashMap<>(); // 子弹视图映射，直接使用Projectile对象作为键
    private Map<PlantType, String> plantImagePaths = new HashMap<>(); // 植物图片路径映射
    private SpawnService spawnService; // 僵尸生成服务
    private StackPane gameContainer; // 添加gameContainer作为类成员变量
    private List<Cart> carts = new ArrayList<>(); // 所有小推车列表
    private Map<Integer, Region> cartViews = new HashMap<>(); // 小推车视图映射（按车道索引）


    public BattleScene(int level) {
        super(new Pane());
        this.level = level;
        System.out.println("BattleScene 构造函数被调用，关卡: " + level);
        // 初始化游戏服务
        this.gameLoopService = new GameLoopServiceImpl();
        this.sunBankService = new SunBankServiceImpl();
        this.spawnService = new SpawnServiceImpl();
        this.activeSuns = new ArrayList<>();
        this.random = new Random();
        System.out.println("游戏服务初始化完成，准备调用initialize()");
        initialize();
    }
    
    private void initialize() {
        System.out.println("BattleScene.initialize() 方法开始执行");
        Pane root = (Pane) getRoot();
        
        // 添加背景图片
        Image backgroundImage = ResourcePool.getInstance().getImage("/pic/background02.png");
        if (backgroundImage != null) {
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(1200);
            backgroundView.setFitHeight(700);
            backgroundView.setPreserveRatio(false);
            root.getChildren().add(0, backgroundView);
        } else {
            // 如果图片加载失败，使用备用背景色
            root.setStyle("-fx-background-color: #606c38;");
        }

        // 初始化植物图片路径映射（先空着，后续可以填充实际路径）
        plantImagePaths.put(PlantType.SUNFLOWER, ""); // 向日葵图片路径
        plantImagePaths.put(PlantType.PEASHOOTER, ""); // 豌豆射手图片路径
        plantImagePaths.put(PlantType.WALLNUT, ""); // 坚果墙图片路径
        plantImagePaths.put(PlantType.CHERRY_BOMB, ""); // 樱桃炸弹图片路径
        plantImagePaths.put(PlantType.REPEATER, ""); // 双发射手图片路径
        
        // 注册僵尸生成事件监听器
        registerZombieSpawnListener();
        
        // 创建并启动UI更新循环
        setupUpdateLoop();
        
        // 创建主布局容器
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        mainLayout.setPrefSize(1200, 700);

        
        // 顶部信息栏
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5));
        topBar.setStyle("-fx-background-color: #283618;");
        
        // 关卡信息
        Text levelText = new Text("关卡 " + level);
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        levelText.setFill(Color.WHITE);
        
        // 阳光数量显示
        sunAmountText = new Text("阳光: " + sunBankService.getSunAmount());
        sunAmountText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        sunAmountText.setFill(Color.YELLOW);
        
        // 战斗状态显示
        battleStatusText = new Text("战斗准备中...");
        battleStatusText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        battleStatusText.setFill(Color.WHITE);
        
        topBar.getChildren().addAll(levelText, sunAmountText, battleStatusText);
        
        // 游戏棋盘
        gameGrid = new GridPane();
        gameGrid.setHgap(2);
        gameGrid.setVgap(2);
     gameGrid.setAlignment(Pos.TOP_LEFT);
       // gameGrid.setStyle("-fx-background-color: #283618;");
        
        // 创建5x12的游戏网格（5行12列）
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 12; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(80, 80);


                // 设置网格为完全透明，无边框
                cell.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");
                
                // 保存行列信息到单元格
                final int finalRow = row;
                final int finalCol = col;
                String cellKey = finalRow + "," + finalCol;
                plantCells.put(cellKey, cell);
                
                gameGrid.add(cell, col, row);
            }
        }
        // 创建阳光层，用于显示阳光
        sunLayer = new Pane();

        sunLayer.setPrefSize(984, 500); // 明确设置与gameContainer相同的大小

        sunLayer.setMouseTransparent(false); // 允许阳光接收点击事件
        // 确保阳光层的背景是透明的，不会遮挡下层内容
        sunLayer.setStyle("-fx-background-color: transparent;");

        // 僵尸层，用于专门放置僵尸
        zombieLayer = new Pane();

        zombieLayer.setPrefSize(984, 500);

       zombieLayer.setMouseTransparent(false);

        // 创建子弹层，用于显示子弹
        projectileLayer = new Pane();

        projectileLayer.setPrefSize(984, 500);

        projectileLayer.setMouseTransparent(true);

        // 创建并放置小推车（在每个车道的最左侧） - 移到这里，确保zombieLayer已经初始化
        for (int laneIndex = 0; laneIndex < 5; laneIndex++) {
            // 计算小推车的位置（车道最左侧）

            // 从代码中可以看出，每个车道的Y坐标计算方式为：laneIndex * 82 + 5
            // 最左侧的X坐标为10
            Position cartPosition = new Position(10, laneIndex * 82 + 5);


            // 创建小推车实体
            Cart cart = CartFactory.getInstance().createDefaultCart(cartPosition, laneIndex);
            carts.add(cart);

            // 渲染小推车
            renderCart(cart);
        }
        // 创建一个容器来包裹游戏网格、僵尸层和阳光层
        gameContainer = new StackPane();

        gameContainer.setPrefSize(984, 500); // 设置适当的大小 (12列 x (80+2)像素)
        // 设置StackPane的对齐方式为左上角，确保gameGrid从(0,0)开始
        gameContainer.setAlignment(Pos.TOP_LEFT);
        
        // 创建一个HBox作为水平偏移容器，将格子向右平移一格
        HBox offsetContainer = new HBox();
        offsetContainer.setPrefSize(984, 500);
        offsetContainer.setAlignment(Pos.TOP_LEFT);
        // 设置左侧padding为一格的宽度(80+2=82像素)，实现向右平移一格的效果
        offsetContainer.setPadding(new Insets(0, 0, 0, 82));
        offsetContainer.getChildren().add(gameGrid);
        
        gameContainer.getChildren().add(offsetContainer); // 第一层：带有偏移的游戏网格

        gameContainer.getChildren().add(zombieLayer); // 第二层：僵尸层
        gameContainer.getChildren().add(projectileLayer); // 第三层：子弹层
        gameContainer.getChildren().add(sunLayer); // 第四层：阳光层（在最上层，确保可以点击）

        // 为gameContainer添加点击事件处理
        gameContainer.setOnMouseClicked(e -> {
            // 计算点击位置对应的网格坐标
            double x = e.getX();
            double y = e.getY();

            // 考虑网格间隔和水平偏移量（减去82像素的偏移）
            int row = (int)Math.floor(y / (80 + 2));
            int col = (int)Math.floor((x - 82) / (80 + 2));

            // 检查点击位置是否在有效网格内
            if (battleStarted && selectedPlantType != null && row >= 0 && row < 5 && col >= 0 && col < 12) {

                placePlant(selectedPlantType, row, col);
            }
        });

        // 植物选择栏
        plantSelector = new HBox(10);
        plantSelector.setAlignment(Pos.CENTER);
        plantSelector.setPadding(new Insets(10));
        plantSelector.setStyle("-fx-background-color: #283618;");
        
        // 添加可选植物按钮
        addPlantButton(PlantType.SUNFLOWER);
        addPlantButton(PlantType.PEASHOOTER);
        addPlantButton(PlantType.WALLNUT);
        addPlantButton(PlantType.CHERRY_BOMB);
        addPlantButton(PlantType.REPEATER);
        
        // 底部按钮栏
        HBox buttonBar = new HBox(20);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));
        
        // 开始战斗按钮
        startButton = new Button("开始战斗");
        startButton.setPrefSize(120, 40);
        startButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        startButton.setOnAction(e -> {
            startBattle();
        });
        
        // 返回选关按钮
        Button backButton = new Button("返回选关");
        backButton.setPrefSize(120, 40);
        backButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        backButton.setOnAction(e -> {
            stopBattle();
            Router.getInstance().showLevelSelectScene();
        });
        
        buttonBar.getChildren().addAll(startButton, backButton);
        
        // 将所有元素添加到主布局
        mainLayout.getChildren().addAll(topBar, gameContainer, plantSelector, buttonBar);
        // 添加这一行代码，确保gameContainer在水平方向上居中
        mainLayout.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(mainLayout);
    }
    
    /**
     * 添加植物选择按钮到植物选择栏
     */
    private void addPlantButton(PlantType type) {
        VBox plantBox = new VBox(5);
        plantBox.setAlignment(Pos.CENTER);
        
        // 植物图标按钮
        Button plantButton = new Button(type.name());
        plantButton.setPrefSize(80, 80);
        plantButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        plantButton.setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
        
        // 阳光成本显示
        Text costText = new Text("" + type.getCost());
        costText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        costText.setFill(Color.YELLOW);
        
        plantBox.getChildren().addAll(plantButton, costText);
        plantSelector.getChildren().add(plantBox);
        
        // 设置按钮点击事件
        plantButton.setOnAction(e -> {
            if (battleStarted) {
                if (selectedPlantType == type) {
                    // 如果再次点击已选中的植物，则取消选择
                    selectedPlantType = null;
                    plantButton.setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
                    battleStatusText.setText("已取消选择植物");
                } else {
                    // 检查阳光是否足够
                    if (sunBankService.getSunAmount() >= type.getCost()) {
                        // 先恢复其他按钮的样式
                        for (javafx.scene.Node node : plantSelector.getChildren()) {
                            if (node instanceof VBox) {
                                VBox box = (VBox) node;
                                if (box.getChildren().size() > 0 && box.getChildren().get(0) instanceof Button) {
                                    Button btn = (Button) box.getChildren().get(0);
                                    btn.setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
                                }
                            }
                        }
                        
                        // 选中当前植物
                        selectedPlantType = type;
                        plantButton.setStyle("-fx-background-color: #fefae0; -fx-text-fill: #bc6c25;");
                        battleStatusText.setText("已选择: " + type.name() + "，请点击格子放置");
                    } else {
                        battleStatusText.setText("阳光不足！");
                    }
                }
            } else {
                battleStatusText.setText("请先开始战斗！");
            }
        });
    }
    
    public int getLevel() {
        return level;
    }
    
    public void startBattle() {
        if (!battleStarted) {
            battleStarted = true;
            battleStatusText.setText("战斗进行中...");
            startButton.setDisable(true);
            startButton.setText("战斗中");

            // 启动游戏循环
            ((GameLoopServiceImpl) gameLoopService).setSpawnService(spawnService);
            gameLoopService.start();

            // 开始自动生成阳光
            new Thread(this::sunGenerationTask).start();

            // 直接创建并渲染一个僵尸，用于测试渲染逻辑
            System.out.println("直接创建并渲染一个测试僵尸...");
            Position position = new Position(984, 1 * 82 + 5); // 在第2个车道，使用与renderZombie相同的位置计算
            Zombie testZombie = ZombieFactory.getInstance().createZombie(ZombieType.NORMAL, position, 1);
            zombies.add(testZombie);
            renderZombie(testZombie, 1);
            System.out.println("测试僵尸已添加到场景");


            System.out.println("开始关卡 " + level + " 的战斗！");
        }
    }
    
    /**
     * 放置植物
     */
    private void placePlant(PlantType type, int row, int col) {
        // 检查格子是否已经有植物
        String cellKey = row + "," + col;
        Pane cell = plantCells.get(cellKey);
        if (cell.getChildren().size() > 0) {
            battleStatusText.setText("该位置已有植物！");
            return;
        }
        
        // 检查阳光是否足够
        if (sunBankService.getSunAmount() >= type.getCost()) {
            // 扣除阳光
            sunBankService.removeSun(type.getCost());
            sunAmountText.setText("阳光: " + sunBankService.getSunAmount());
            
            // 创建植物实体
            Position position = new Position(col * (80 + 2) + 40, row * (80 + 2) + 40); // 居中放置
            Plant plant = PlantFactory.getInstance().createPlant(type, position);
            plants.add(plant);
            
            // 渲染植物（用方块代表）
            renderPlant(plant, cell, type);
            
            battleStatusText.setText("已放置: " + type.name());
            
            // 取消选中状态
            selectedPlantType = null;
            // 恢复按钮样式
            for (javafx.scene.Node node : plantSelector.getChildren()) {
                if (node instanceof VBox) {
                    VBox box = (VBox) node;
                    if (box.getChildren().size() > 0 && box.getChildren().get(0) instanceof Button) {
                        Button btn = (Button) box.getChildren().get(0);
                        btn.setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
                    }
                }
            }
        } else {
            battleStatusText.setText("阳光不足！");
        }
    }
    
    /**
     * 渲染植物（用方块代表）
     */
    private void renderPlant(Plant plant, Pane cell, PlantType type) {
        // 根据植物类型设置不同颜色的方块
        Region plantView = new Region();
        plantView.setPrefSize(70, 70);
        plantView.setStyle(getPlantStyleByType(type));
        
        // 设置居中
        plantView.setLayoutX(5);
        plantView.setLayoutY(5);
        
        // 添加到单元格
        cell.getChildren().add(plantView);
    }
    
    /**
     * 根据植物类型获取对应的样式
     */
    private String getPlantStyleByType(PlantType type) {
        switch (type) {
            case SUNFLOWER:
                return "-fx-background-color: #FFD700; -fx-border-color: #FFA500; -fx-border-width: 2;";
            case PEASHOOTER:
                return "-fx-background-color: #32CD32; -fx-border-color: #228B22; -fx-border-width: 2;";
            case WALLNUT:
                return "-fx-background-color: #D2B48C; -fx-border-color: #A0522D; -fx-border-width: 2;";
            case CHERRY_BOMB:
                return "-fx-background-color: #FF0000; -fx-border-color: #8B0000; -fx-border-width: 2;";
            case REPEATER:
                return "-fx-background-color: #00FF00; -fx-border-color: #008000; -fx-border-width: 2;";
            default:
                return "-fx-background-color: #6495ED; -fx-border-color: #4169E1; -fx-border-width: 2;";
        }
    }
    
    public void stopBattle() {
        if (battleStarted) {
            battleStarted = false;
            battleStatusText.setText("战斗已停止");
            startButton.setDisable(false);
            startButton.setText("开始战斗");

            // 停止游戏循环
            gameLoopService.stop();

            // 清空所有阳光
            javafx.application.Platform.runLater(() -> {
                sunLayer.getChildren().clear();
                activeSuns.clear();
            });

            // 清理僵尸
            cleanupZombies();

            // 清理子弹
            if (projectileLayer != null) {
                projectileLayer.getChildren().clear();
            }
            projectiles.clear();
            projectileViews.clear();

            System.out.println("停止战斗！");
        }
    }
    
    /**
     * 注册僵尸生成事件监听器
     */
    private void registerZombieSpawnListener() {
        EventBus.getInstance().register(ZombieSpawned.class, new GameEventListener<ZombieSpawned>() {
            @Override
            public void onEvent(ZombieSpawned event) {
                // 在JavaFX应用线程中处理UI更新
                javafx.application.Platform.runLater(() -> {
                    // 添加僵尸到列表
                    zombies.add(event.zombie());
                    // 渲染僵尸
                    renderZombie(event.zombie(), event.laneIndex());
                });
            }
        });
    }
    
    /**
     * 渲染僵尸
     */
    private void renderZombie(Zombie zombie, int laneIndex) {
        try {
            // 检查游戏容器和僵尸层是否存在
            if (gameContainer == null || zombieLayer == null) {
                System.err.println("游戏容器或僵尸层未找到");
                return;
            }
            // 添加调试日志
            System.out.println("渲染僵尸: " + zombie.getType() + " 在车道 " + laneIndex);
            
            // 设置僵尸初始位置（从右侧进入）

            double x = 984; // 使用固定值，确保从屏幕右侧进入
            double y = laneIndex *82 + 5;  // 放置在对应车道

            
            // 创建僵尸视图容器
            Pane zombieContainer = new Pane();
            zombieContainer.setPrefSize(70, 70);
            zombieContainer.setLayoutX(x);
            zombieContainer.setLayoutY(y);
            
            // 直接使用彩色方块作为占位符
            addPlaceholderToContainer(zombieContainer, zombie);
            
            // 保存僵尸容器到映射中，以便更新位置
            zombieContainers.put(zombie.getId(), zombieContainer);

            // 添加到僵尸层
            zombieLayer.getChildren().add(zombieContainer);
            System.out.println("僵尸已添加到屏幕: " + zombie.getId() + " 位置: (" + x + ", " + y + ")");
        } catch (Exception e) {
            System.err.println("渲染僵尸错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 为僵尸容器添加占位符
     */
    private void addPlaceholderToContainer(Pane container, Zombie zombie) {
        Pane placeholder = new Pane();
        placeholder.setPrefSize(70, 70);
        
        // 根据僵尸类型设置不同颜色
        String style = "-fx-background-color: #8B4513; -fx-border-color: #654321; -fx-border-width: 2;";
        switch (zombie.getType()) {
            case NORMAL:
                style = "-fx-background-color: #8B4513; -fx-border-color: #654321; -fx-border-width: 2;";
                break;
            case FLAG:
                style = "-fx-background-color: #CD5C5C; -fx-border-color: #B22222; -fx-border-width: 2;";
                break;
            case BUCKETHEAD:
                style = "-fx-background-color: #708090; -fx-border-color: #4682B4; -fx-border-width: 2;";
                break;
            case FOOTBALL:
                style = "-fx-background-color: #FFD700; -fx-border-color: #FFA500; -fx-border-width: 2;";
                break;
            default:
                style = "-fx-background-color: #8B4513; -fx-border-color: #654321; -fx-border-width: 2;";
                break;
        }
        
        placeholder.setStyle(style);
        container.getChildren().add(placeholder);
    }
    
    /**
     * 清理僵尸资源
     */
    private void cleanupZombies() {
        // 清除所有僵尸容器
        if (zombieLayer != null) {
            for (Pane container : zombieContainers.values()) {
                if (zombieLayer.getChildren().contains(container)) {
                    zombieLayer.getChildren().remove(container);
                }
            }
        }
        
        // 清空僵尸列表和视图映射
        zombies.clear();
        zombieViews.clear();
        zombieContainers.clear();
    }
    
    /**
     * 阳光自动生成任务
     */
    private void sunGenerationTask() {
        while (battleStarted) {
            try {
                // 每5秒生成一些阳光
                Thread.sleep(2000+random.nextInt(5000));

                // 在JavaFX应用线程中更新UI
                javafx.application.Platform.runLater(() -> {
                    spawnSun();
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    private void spawnSun() {
        // 随机生成阳光的位置（确保在可见区域内）
        int sunValue = 25; // 阳光价值

        int x = 30 + random.nextInt(924); // 随机X坐标 (确保在984宽度内)
        int y = 30 + random.nextInt(440); // 随机Y坐标 (确保在500高度内)

        Position position = new Position(x, y);

        // 创建阳光实体
        Sun sun = new Sun(position, sunValue);
        activeSuns.add(sun);

        // 创建阳光的可视化表示
        Circle sunVisual = new Circle(position.x(), position.y(), 15);
        sunVisual.setFill(Color.YELLOW);
        sunVisual.setStroke(Color.GOLD);
        sunVisual.setStrokeWidth(2);

        // 添加点击事件
        sunVisual.setOnMouseClicked(e -> {
            if (!sun.isCollected()) {
                collectSun(sun, sunVisual);
            }
        });

        // 添加到阳光层
        sunLayer.getChildren().add(sunVisual);

        battleStatusText.setText("阳光出现了！");

        // 阳光自动消失的定时器
        new Thread(() -> {
            try {
                Thread.sleep(10000); // 10秒后自动消失
                javafx.application.Platform.runLater(() -> {
                    if (!sun.isCollected()) {
                        sunLayer.getChildren().remove(sunVisual);
                        activeSuns.remove(sun);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    /**
     * 收集阳光
     */
    private void collectSun(Sun sun, Circle sunVisual) {
        sun.collect();
        sunBankService.addSun(sun.getValue());
        sunAmountText.setText("阳光: " + sunBankService.getSunAmount());
        battleStatusText.setText("获得阳光 +" + sun.getValue());
        System.out.println("阳光被收集，当前阳光数量: " + sunBankService.getSunAmount()); // 添加调试信息

        // 从UI中移除阳光
        sunLayer.getChildren().remove(sunVisual);
        activeSuns.remove(sun);
    }
    /**
     * 设置UI更新循环
     */
    private void setupUpdateLoop() {
        // 使用JavaFX的AnimationTimer进行UI更新
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (battleStarted) {
                    // 更新僵尸位置
                    updateZombies();

                    // 植物攻击逻辑
                    long currentTimeMillis = now / 1_000_000;
                    updatePlants(currentTimeMillis);

                    // 更新子弹位置
                    updateProjectiles();
                }
            }
        };
        timer.start();
    }
    
    /**
     * 更新僵尸位置
     */
    private void updateZombies() {
        List<UUID> zombiesToRemove = new ArrayList<>();
        
        for (Zombie zombie : zombies) {
            UUID zombieId = zombie.getId();
            Pane container = zombieContainers.get(zombieId);
            
            if (container != null) {
                // 获取当前位置
                double currentX = container.getLayoutX();
                
                // 根据僵尸类型的移动速度更新位置
                double speed = getZombieSpeed(zombie.getType());
                double newX = currentX - speed;
                
                // 更新容器位置
                container.setLayoutX(newX);


                // 更新僵尸实体位置 - 移除Y轴偏移量，确保实体位置与渲染位置一致
                zombie.setPosition(new Position(newX, container.getLayoutY()));


                // 检查僵尸是否移出屏幕左侧
                if (newX + container.getPrefWidth() < 0) {
                    zombiesToRemove.add(zombieId);
                }
            }
        }
        // 处理小推车
        for (Cart cart : new ArrayList<>(carts)) {
            if (cart.isTriggered()) {
                // 小推车被触发，向右移动
                double moveSpeed = 5; // 小推车移动速度
                Region cartView = cartViews.get(cart.getLaneIndex());

                if (cartView != null) {
                    // 更新小推车视图位置
                    double newX = cartView.getLayoutX() + moveSpeed;
                    cartView.setLayoutX(newX);

                    // 更新小推车实体位置
                    cart.updatePosition(moveSpeed);

                    // 检查小推车是否清除碰到的僵尸
                    for (Zombie zombie : new ArrayList<>(zombies)) {
                        if (zombie.isDead()) continue;

                        if (zombie.getLaneIndex() == cart.getLaneIndex()) {
                            // 计算僵尸位置
                            Pane zombieContainer = zombieContainers.get(zombie.getId());
                            if (zombieContainer != null) {
                                double zombieX = zombieContainer.getLayoutX();

                                // 检查小推车是否碰到僵尸
                                if (zombieX <= newX + 60 && zombieX + 50 >= newX) {
                                    // 清除僵尸
                                    System.out.println("小推车清除僵尸！车道：" + cart.getLaneIndex());
                                    removeZombie(zombie.getId());
                                }
                            }
                        }
                    }

                    // 检查小推车是否移动到屏幕右侧，移出屏幕则消失

                    if (newX > 984) {

                        System.out.println("小推车移出屏幕！");
                        zombieLayer.getChildren().remove(cartView);
                        cartViews.remove(cart.getLaneIndex());
                        cart.setActive(false);
                        carts.remove(cart);
                    }
                }
            }
        }
        // 检查僵尸是否触碰到小推车
        for (Zombie zombie : zombies) {
            if (zombie.isDead()) continue;

            int laneIndex = zombie.getLaneIndex();
            Position zombiePos = zombie.getPosition();

            // 查找对应车道的小推车
            for (Cart cart : carts) {
                if (cart.getLaneIndex() == laneIndex && cart.isActive() && !cart.isTriggered()) {
                    Position cartPos = cart.getPosition();

                    // 检查僵尸是否触碰到小推车
                    if (zombiePos.x() <= cartPos.x() + 60) { // 小推车宽度为60
                        // 触发小推车
                        cart.trigger();

                        // 这里可以添加小推车触发后的逻辑，比如清除当前车道的所有僵尸
                        // 为了简化，这里只是打印一条消息
                        System.out.println("小推车被触发！车道：" + laneIndex);

//                        // 从视图中移除小推车
//                        Region cartView = cartViews.get(laneIndex);
//                        if (cartView != null) {
//                            zombieLayer.getChildren().remove(cartView);
//                            cartViews.remove(laneIndex);
//                        }
                    }
                }
            }
        }
        // 移除需要删除的僵尸
        for (UUID zombieId : zombiesToRemove) {
            removeZombie(zombieId);
        }
    }
    
    /**
     * 获取僵尸的移动速度
     */
    private double getZombieSpeed(ZombieType type) {
        // 根据僵尸类型设置不同的移动速度
        switch (type) {
            case NORMAL:
                return 0.5;
            case FLAG:
                return 0.7;
            case BUCKETHEAD:
                return 0.4;
            case FOOTBALL:
                return 0.6;
            default:
                return 0.5;
        }
    }
    
    /**
     * 移除僵尸
     */
    private void removeZombie(UUID zombieId) {
        // 从僵尸层中移除僵尸容器
        Pane container = zombieContainers.get(zombieId);
        if (container != null && zombieLayer != null && zombieLayer.getChildren().contains(container)) {
            zombieLayer.getChildren().remove(container);
        }

        // 从映射中移除
        zombieViews.remove(zombieId);
        zombieContainers.remove(zombieId);
        zombies.removeIf(z -> z.getId().equals(zombieId));
    }

    // 添加新方法：更新植物状态和处理攻击
    private void updatePlants(long now) {
        //System.out.println("updatePlants called with time: " + now + " ms");
        // 遍历所有植物
        for (Plant plant : plants) {
            // 添加植物位置日志
            System.out.println("植物位置: " + plant.getPosition().x() + ", " + plant.getPosition().y() + ", 类型: " + plant.getType());

            // 处理向日葵生产阳光
            if (plant.produceSun(now)) {
                System.out.println("向日葵生产阳光！位置: " + plant.getPosition().x() + ", " + plant.getPosition().y());
                // 向日葵生产了阳光，在植物位置上方生成阳光
                spawnSunAtPosition(plant.getPosition().x(), plant.getPosition().y() - 20);
            }

            // 处理植物攻击
            List<Projectile> newProjectiles = plant.attack(now, zombies);
            if (!newProjectiles.isEmpty()) {
                System.out.println("生成了" + newProjectiles.size() + "颗子弹");
                projectiles.addAll(newProjectiles);
                // 渲染新子弹
                for (Projectile projectile : newProjectiles) {
                    System.out.println("准备渲染子弹: 类型=" + projectile.getType() + ", 位置=(" + projectile.getPosition().x() + ", " + projectile.getPosition().y() + ")");
                    renderProjectile(projectile);
                }
            }
            else {
                System.out.println("没有生成子弹");
            }
        }
    }

    // 添加新方法：在指定位置生成阳光
    private void spawnSunAtPosition(double x, double y) {
        int sunValue = 25; // 阳光价值
        Position position = new Position(x, y);

        // 创建阳光实体
        Sun sun = new Sun(position, sunValue);
        activeSuns.add(sun);

        // 创建阳光的可视化表示
        Circle sunVisual = new Circle(position.x(), position.y(), 15);
        sunVisual.setFill(Color.YELLOW);
        sunVisual.setStroke(Color.GOLD);
        sunVisual.setStrokeWidth(2);

        // 添加点击事件
        sunVisual.setOnMouseClicked(e -> {
            if (!sun.isCollected()) {
                collectSun(sun, sunVisual);
            }
        });

        // 添加到阳光层
        sunLayer.getChildren().add(sunVisual);

        battleStatusText.setText("阳光出现了！");

        // 阳光自动消失的定时器
        new Thread(() -> {
            try {
                Thread.sleep(10000); // 10秒后自动消失
                javafx.application.Platform.runLater(() -> {
                    if (!sun.isCollected()) {
                        sunLayer.getChildren().remove(sunVisual);
                        activeSuns.remove(sun);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    // 添加新方法：渲染子弹
    private void renderProjectile(Projectile projectile) {
        // 创建子弹视图
        Pane projectileView = new Pane();
        projectileView.setPrefSize(15, 15);
        projectileView.setStyle(getProjectileStyleByType(projectile.getType()));


        // 设置子弹初始位置 - 使用与植物相同的车道高度计算方式(82)
        double x = projectile.getPosition().x(); // 直接使用子弹实体的X坐标
        double y = projectile.getLaneIndex() * 82 + 5 + 15;
        
        // 同步子弹实体的Y坐标与渲染位置
        Position newPosition = new Position(x, y - 15); // 减去子弹居中偏移
        projectile.setPosition(newPosition);


        projectileView.setLayoutX(x);
        projectileView.setLayoutY(y);

        // 添加调试日志，确认子弹被渲染
        System.out.println("渲染子弹: 类型=" + projectile.getType() + ", 位置=(" + x + ", " + y + "), 车道=" + projectile.getLaneIndex());

        // 保存子弹视图
        projectileViews.put(projectile, projectileView);

        // 添加到子弹层
        projectileLayer.getChildren().add(projectileView);
    }

    // 添加新方法：根据子弹类型获取样式
    private String getProjectileStyleByType(ProjectileType type) {
        switch (type) {
            case PEANUT:
                return "-fx-background-color: #00FFFF; -fx-border-color: #008080; -fx-border-width: 1;";
            case FIRE_PEANUT:
                return "-fx-background-color: #FF6347; -fx-border-color: #8B0000; -fx-border-width: 1;";
            case ICE_PEANUT:
                return "-fx-background-color: #87CEFA; -fx-border-color: #4682B4; -fx-border-width: 1;";
            case CABBAGE:
                return "-fx-background-color: #228B22; -fx-border-color: #006400; -fx-border-width: 1;";
            default:
                return "-fx-background-color: #FFFFFF; -fx-border-color: #808080; -fx-border-width: 1;";
        }
    }

    // 添加新方法：更新子弹位置和碰撞检测
    private void updateProjectiles() {
        List<Projectile> projectilesToRemove = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            Pane view = projectileViews.get(projectile);

            if (view != null) {
                // 使用Projectile类的updatePosition方法更新子弹位置
                projectile.updatePosition(16); // 假设每帧16ms

                
                // 更新子弹视图位置 - 直接使用实体位置
                view.setLayoutX(projectile.getPosition().x());

                // 检查是否超出屏幕
                if (projectile.getPosition().x() > 1200) {

                    projectilesToRemove.add(projectile);
                    continue;
                }

                // 检查子弹是否击中僵尸
                checkProjectileCollision(projectile);
            }
        }

        // 移除需要删除的子弹
        for (Projectile projectile : projectilesToRemove) {
            removeProjectile(projectile); // 传入整个Projectile对象
        }
    }

    // 添加新方法：检查子弹碰撞
    private void checkProjectileCollision(Projectile projectile) {
        // 首先检查子弹是否仍然有效
        if (!projectile.isActive() || projectile.isHasHitTarget()) {
            return;
        }
        
        // 遍历所有僵尸，检查碰撞
        for (Zombie zombie : zombies) {

            // 确保在同一车道
            if (zombie.getLaneIndex() != projectile.getLaneIndex()) {
                continue;
            }
            
            // 使用基于位置的碰撞检测，增大碰撞范围以提高检测成功率
            Position zombiePos = zombie.getPosition();
            Position projectilePos = projectile.getPosition();

            // 计算距离进行碰撞检测（使用圆形碰撞检测，半径为15，与子弹大小匹配）
            double dx = projectilePos.x() - zombiePos.x();
            double dy = projectilePos.y() - zombiePos.y();
            double distance = Math.sqrt(dx * dx + dy * dy);
            boolean collision = (distance <= 20); // 稍大于子弹半径，留出一点误差余量

            if (collision) {

                // 对僵尸造成伤害
                zombie.takeDamage(projectile.getDamage());
                
                // 标记子弹为已击中
                projectile.setHasHitTarget(true);
                projectile.setActive(false);

                // 检查僵尸是否死亡
                if (zombie.isDead()) {
                    removeZombie(zombie.getId());
                }

                // 移除子弹
                removeProjectile(projectile);
                break;
            }
        }
    }

    // 添加新方法：移除子弹
    private void removeProjectile(Projectile projectile) {  // 修改参数类型为Projectile
        Pane view = projectileViews.get(projectile);  // 使用Projectile对象作为key
        if (view != null && projectileLayer.getChildren().contains(view)) {
            projectileLayer.getChildren().remove(view);
        }

        projectileViews.remove(projectile);  // 使用Projectile对象作为key
        projectiles.remove(projectile);  // 正确移除Projectile对象
    }
    private void renderCart(Cart cart) {
        int laneIndex = cart.getLaneIndex();
        Position position = cart.getPosition();

        // 创建小推车视图（这里使用简单的矩形代表）
        Region cartView = new Region();
        cartView.setPrefSize(60, 40);
        cartView.setStyle("-fx-background-color: #8B4513; -fx-border-color: #000000; -fx-border-width: 2;");

        // 设置小推车的位置
        cartView.setLayoutX(position.x());
        cartView.setLayoutY(position.y());

        // 添加到僵尸层（因为小推车应该在植物层之上）
        zombieLayer.getChildren().add(cartView);

        // 保存小推车视图引用
        cartViews.put(laneIndex, cartView);
    }
}
