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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

import java.util.*;

import com.game.pvz.module.entity.projectile.Projectile;
import com.game.pvz.module.entity.projectile.ProjectileType;
import java.util.concurrent.CopyOnWriteArrayList;
import com.game.pvz.module.entity.cart.Cart;
import com.game.pvz.module.entity.cart.CartFactory;
import com.game.pvz.module.entity.cart.CartType;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;


/**
 * 战场场景（类）
 */
public class BattleScene extends Scene {
    private enum ZombieState {
        WALKING,  // 行走（移动+播放走路动画）
        ATTACKING, // 攻击（停止移动+播放攻击动画）
        DEAD      // 死亡（停止所有行为+播放死亡动画）
    }


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
    // 1. 首先，我们需要一个集合来跟踪所有下落中的阳光
    private List<Sun> fallingSuns = new ArrayList<>();
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
    private Map<UUID, ZombieState> zombieStates = new HashMap<>();
    private Map<UUID, Timeline> zombieAnimationTimelines = new HashMap<>();
    private Map<ZombieType, String> zombieImageBasePaths = new HashMap<>();
    private Map<ZombieType, Integer> zombieFrameCounts = new HashMap<>();
    private Map<Projectile, Pane> projectileViews = new HashMap<>(); // 子弹视图映射，直接使用Projectile对象作为键
    private Map<PlantType, String> plantImagePaths = new HashMap<>(); // 植物图片路径映射
    private final Map<Sun, Double> sunTargetPositions = new HashMap<>();
    private final Map<Sun, Node> sunVisuals = new HashMap<>();// 阳光目标位置映射
    private SpawnService spawnService; // 僵尸生成服务
    private StackPane gameContainer; // 添加gameContainer作为类成员变量
    private List<Cart> carts = new ArrayList<>(); // 所有小推车列表
    private Map<Integer, Node> cartViews = new HashMap<>(); // 小推车视图映射（按车道索引）
    private boolean isShovelMode = false; // 铲子模式标志
    private Button shovelButton; // 铲子按钮
    private boolean gameOver = false;
    private Pane gamePanel;
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
        plantImagePaths.put(PlantType.SUNFLOWER, "/plants/sunflower/sunflower-1.png"); // 向日葵图片路径
        plantImagePaths.put(PlantType.PEASHOOTER, "/plants/peashooter/peashooter-1.png"); // 豌豆射手图片路径
        plantImagePaths.put(PlantType.WALLNUT, "/plants/wallnut/wallnut-1.png"); // 坚果墙图片路径
        plantImagePaths.put(PlantType.CHERRY_BOMB, "/plants/cherrybomb/cherry_bomb-1.png"); // 樱桃炸弹图片路径
        plantImagePaths.put(PlantType.REPEATER, "/plants/repeater/repeater-1.png"); // 双发射手图片路径

                                  initZombieImageConfig();

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
            Position cartPosition = new Position(10, laneIndex * 82 + 5+40);



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
            if (battleStarted && row >= 0 && row < 5 && col >= 0 && col < 12) {
                if (isShovelMode) {
                    // 铲子模式下，尝试铲除植物
                    removePlant(row, col);
                } else if (selectedPlantType != null) {
                    // 普通模式下，尝试放置植物
                    placePlant(selectedPlantType, row, col);
                }
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
        // 添加铲子按钮
        shovelButton = new Button("铲子");
        shovelButton.setPrefSize(80, 150);
        shovelButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        shovelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        VBox shovelBox = new VBox(5);
        shovelBox.setAlignment(Pos.CENTER);
        shovelBox.getChildren().add(shovelButton);
        plantSelector.getChildren().add(shovelBox);
        // 设置铲子按钮点击事件
        shovelButton.setOnAction(e -> {
            if (battleStarted) {
                isShovelMode = !isShovelMode;
                if (isShovelMode) {
                    shovelButton.setStyle("-fx-background-color: #fefae0; -fx-text-fill: #6c757d;");
                    battleStatusText.setText("已选择铲子，请点击植物铲除");
                    selectedPlantType = null; // 取消植物选择
                    // 恢复植物按钮样式
                    for (Node node : plantSelector.getChildren()) {
                        if (node instanceof VBox) {
                            VBox box = (VBox) node;
                            if (box.getChildren().size() > 0 && box.getChildren().get(0) instanceof Button && box != shovelBox) {
                                Button btn = (Button) box.getChildren().get(0);
                                btn.setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
                            }
                        }
                    }
                } else {
                    shovelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
                    battleStatusText.setText("已取消铲子模式");
                }
            } else {
                battleStatusText.setText("请先开始战斗！");
            }
        });
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

                                                    private void initZombieImageConfig(){
                                                        // 普通僵尸：路径+4帧（示例：/zombies/normal/normal-0.png 到 normal-3.png）
                                                        zombieImageBasePaths.put(ZombieType.NORMAL, "/zombies/normal/normal");
                                                        zombieFrameCounts.put(ZombieType.NORMAL, 4);

                                                        // 旗帜僵尸：路径+4帧
                                                        zombieImageBasePaths.put(ZombieType.FLAG, "/zombies/flag/flag");
                                                        zombieFrameCounts.put(ZombieType.FLAG, 4);

                                                        // 铁桶僵尸：路径+4帧
                                                        zombieImageBasePaths.put(ZombieType.BUCKETHEAD, "/zombies/buckethead/buckethead");
                                                        zombieFrameCounts.put(ZombieType.BUCKETHEAD, 4);

                                                        // 橄榄球僵尸：路径+4帧
                                                        zombieImageBasePaths.put(ZombieType.FOOTBALL, "/zombies/football/football");
                                                        zombieFrameCounts.put(ZombieType.FOOTBALL, 4);

                                                        zombieImageBasePaths.put(ZombieType.CONEHEAD, "/zombies/conehead/conehead");
                                                        zombieFrameCounts.put(ZombieType.CONEHEAD, 4);
                                                    }
    /**
     * 添加植物选择按钮到植物选择栏
     */
    private void addPlantButton(PlantType type) {
        VBox plantBox = new VBox(5);
        plantBox.setAlignment(Pos.CENTER);
        
        // 植物图标按钮
        Button plantButton = new Button(type.name());
        plantButton.setPrefSize(80, 150);
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
                // 点击植物卡片时取消铲子模式
                if (isShovelMode) {
                    isShovelMode = false;
                    shovelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
                }
                if (selectedPlantType == type) {
                    // 如果再次点击已选中的植物，则取消选择
                    selectedPlantType = null;
                    plantButton.setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
                    battleStatusText.setText("已取消选择植物");
                } else {
                    // 检查阳光是否足够
                    if (sunBankService.getSunAmount() >= type.getCost()) {
                        // 先恢复其他按钮的样式
                        for (Node node : plantSelector.getChildren()) {
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
            double x = col * (80 + 2);
            double y = row * (80 + 2);
            Position position = new Position(x, y);
            Plant plant = PlantFactory.getInstance().createPlant(type, position);
            plants.add(plant);
            
            // 为樱桃炸弹设置种植时间
            if (type == PlantType.CHERRY_BOMB) {
                plant.setPlantedTime(System.currentTimeMillis());
            }

            // 渲染植物（用方块代表）
            renderPlant(plant, cell, type);
            
            battleStatusText.setText("已放置: " + type.name());
            
            // 取消选中状态
            selectedPlantType = null;
            // 恢复按钮样式
            for (Node node : plantSelector.getChildren()) {
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
        // 1. 加载该植物的所有序列帧图片
        List<Image> frames = loadPlantFrames(type);
        if (frames.isEmpty()) {
            // 若图片加载失败，回退到方块显示
            renderFallbackSquare(plant, cell, type);
            return;
        }

        // 2. 创建ImageView作为动画载体
        ImageView plantView = new ImageView(frames.get(0)); // 初始显示第一帧
        plantView.setFitWidth(70);  // 适配格子大小
        plantView.setFitHeight(70);
        plantView.setPreserveRatio(true); // 保持图片比例

        // 3. 居中显示
        plantView.setLayoutX(5);
        plantView.setLayoutY(5);

        // 4. 添加到单元格
        cell.getChildren().add(plantView);

        // 5. 播放放置动画（可选，增强体验）
//        playPlacementAnimation(plantView);

        // 6. 播放循环帧动画
        Timeline frameAnimation = createFrameAnimation(plantView, frames);
        frameAnimation.play();

        // 7. 保存动画引用，便于后续停止

    }

    /**
     * 加载植物的序列帧图片
     */
    private List<Image> loadPlantFrames(PlantType type) {
        List<Image> frames = new ArrayList<>();
        String basePath;

        // 根据植物类型设置图片路径
        switch (type) {
            case SUNFLOWER:
                basePath = "/plants/sunflower/sunflower-";
                break;
            case PEASHOOTER:
                basePath = "/plants/peashooter/peashooter-";
                break;
            case WALLNUT:
                basePath = "/plants/wallnut/wallnut-";
                break;
            case CHERRY_BOMB:
                basePath = "/plants/cherrybomb/cherry_bomb-";
                break;
            case REPEATER:
                basePath = "/plants/repeater/repeater-";
                break;
            default:
                return frames; // 未知类型返回空列表
        }

        // 加载n帧（可根据实际图片数量调整）
        int framenum = 0;
        switch (type)
        {
            case SUNFLOWER:
                framenum=4;
                break;
            case PEASHOOTER:
                framenum=6;
                break;
            case WALLNUT:
                framenum=3;
                break;
            case CHERRY_BOMB:
                 framenum=7;
                break;
            case REPEATER:
                 framenum=7;
                break;
        }
        for (int i = 0; i <framenum; i++) {
            try {
                Image frame = new Image(getClass().getResourceAsStream(basePath + (i+1) + ".png"));
                if (!frame.isError()) {
                    frames.add(frame);
                }
            } catch (Exception e) {
                System.err.println("加载动画帧失败: " + basePath + i + ".png");
            }
        }
        return frames;
    }

    /**
     * 创建帧动画（循环切换图片）
     */
    private Timeline createFrameAnimation(ImageView plantView, List<Image> frames) {
        int frameCount = frames.size();
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE); // 无限循环

        // 每100ms切换一帧（可调整速度）
        Duration frameDuration = Duration.millis(200);

        for (int i = 0; i < frameCount; i++) {
            final int frameIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    frameDuration.multiply(i),  // 第i帧的时间点
                    e -> plantView.setImage(frames.get(frameIndex)) // 切换到第i帧
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // 最后一帧后回到第一帧，形成循环
        KeyFrame lastFrame = new KeyFrame(
                frameDuration.multiply(frameCount),
                e -> plantView.setImage(frames.get(0))
        );
        timeline.getKeyFrames().add(lastFrame);

        return timeline;
    }

    /**
     * 放置时的过渡动画（缩放效果）
     */
//    private void playPlacementAnimation(ImageView plantView) {
//        plantView.setScaleX(0.5);
//        plantView.setScaleY(0.5);
//
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.millis(200),
//                        e -> {
//                            plantView.setScaleX(1);
//                            plantView.setScaleY(1);
//                        })
//        );
//        timeline.play();
//    }

    /**
     * 图片加载失败时的 fallback：显示方块
     */
// 在 BattleScene 类中添加此方法
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
    private void renderFallbackSquare(Plant plant, Pane cell, PlantType type) {
        // 复用之前的方块渲染逻辑
        Region plantView = new Region();
        plantView.setPrefSize(70, 70);
        plantView.setStyle(getPlantStyleByType(type));
        plantView.setLayoutX(5);
        plantView.setLayoutY(5);
        cell.getChildren().add(plantView);
    }
//    private void renderPlant(Plant plant, Pane cell, PlantType type) {
//        // 根据植物类型设置不同颜色的方块
//        Region plantView = new Region();
//        plantView.setPrefSize(70, 70);
//        plantView.setStyle(getPlantStyleByType(type));
//
//        // 设置居中
//        plantView.setLayoutX(5);
//        plantView.setLayoutY(5);
//
//        // 添加到单元格
//        cell.getChildren().add(plantView);
//    }
//
//    /**
//     * 根据植物类型获取对应的样式
//     */
//    private String getPlantStyleByType(PlantType type) {
//        switch (type) {
//            case SUNFLOWER:
//                return "-fx-background-color: #FFD700; -fx-border-color: #FFA500; -fx-border-width: 2;";
//            case PEASHOOTER:
//                return "-fx-background-color: #32CD32; -fx-border-color: #228B22; -fx-border-width: 2;";
//            case WALLNUT:
//                return "-fx-background-color: #D2B48C; -fx-border-color: #A0522D; -fx-border-width: 2;";
//            case CHERRY_BOMB:
//                return "-fx-background-color: #FF0000; -fx-border-color: #8B0000; -fx-border-width: 2;";
//            case REPEATER:
//                return "-fx-background-color: #00FF00; -fx-border-color: #008000; -fx-border-width: 2;";
//            default:
//                return "-fx-background-color: #6495ED; -fx-border-color: #4169E1; -fx-border-width: 2;";
//        }
//    }
    
    public void stopBattle() {
        if (battleStarted) {
            battleStarted = false;
            gameOver = false; // 重置游戏结束状态
            battleStatusText.setText("战斗已停止");
            startButton.setDisable(false);
            startButton.setText("开始战斗");

            // 停止游戏循环
            gameLoopService.stop();

            // 清空所有阳光
            Platform.runLater(() -> {
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
                Platform.runLater(() -> {
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
    /**
     * 渲染僵尸（支持动画，替代原占位符逻辑）
     */
                    private void renderZombie(Zombie zombie, int laneIndex) {
                        try {
                            if (gameContainer == null || zombieLayer == null) {
                                System.err.println("游戏容器或僵尸层未找到");
                                return;
                            }
                            System.out.println("渲染僵尸: " + zombie.getType() + " 在车道 " + laneIndex + "，ID: " + zombie.getId());

                            // 1. 计算初始位置（从右侧进入，与原有逻辑一致）
                            double x = 984;
                            double y = laneIndex * 82 + 5;

                            // 2. 创建僵尸容器（承载动画视图）
                            Pane zombieContainer = new Pane();
                            zombieContainer.setPrefSize(70, 70); // 与原有占位符大小一致
                            zombieContainer.setLayoutX(x);
                            zombieContainer.setLayoutY(y);

                            // 3. 初始化僵尸状态为“行走”
                            zombieStates.put(zombie.getId(), ZombieState.WALKING);

                            // 4. 加载走路动画并添加到容器
                            ImageView zombieView = loadZombieAnimation(zombie, ZombieState.WALKING);
                            if (zombieView != null) {
                                zombieContainer.getChildren().add(zombieView);
                                // 保存僵尸视图引用（替换原有占位符逻辑）
                                zombieViews.put(zombie.getId(), zombieView);
                            } else {
                                // 图片加载失败时，回退到彩色方块（兼容原有逻辑）
                                addPlaceholderToContainer(zombieContainer, zombie);
                            }

                            // 5. 保存容器与动画时间线
                            zombieContainers.put(zombie.getId(), zombieContainer);
                            zombieLayer.getChildren().add(zombieContainer);
                            System.out.println("僵尸已添加到屏幕: " + zombie.getId() + " 位置: (" + x + ", " + y + ")");

                        } catch (Exception e) {
                            System.err.println("渲染僵尸错误: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

//    private void renderZombie(Zombie zombie, int laneIndex) {
//        try {
//            // 检查游戏容器和僵尸层是否存在
//            if (gameContainer == null || zombieLayer == null) {
//                System.err.println("游戏容器或僵尸层未找到");
//                return;
//            }
//            // 添加调试日志
//            System.out.println("渲染僵尸: " + zombie.getType() + " 在车道 " + laneIndex);
//
//            // 设置僵尸初始位置（从右侧进入）
//
//            double x = 984; // 使用固定值，确保从屏幕右侧进入
//            double y = laneIndex *82 + 5;  // 放置在对应车道
//
//
//            // 创建僵尸视图容器
//            Pane zombieContainer = new Pane();
//            zombieContainer.setPrefSize(70, 70);
//            zombieContainer.setLayoutX(x);
//            zombieContainer.setLayoutY(y);
//
//            // 直接使用彩色方块作为占位符
//            addPlaceholderToContainer(zombieContainer, zombie);
//
//            // 保存僵尸容器到映射中，以便更新位置
//            zombieContainers.put(zombie.getId(), zombieContainer);
//
//            // 添加到僵尸层
//            zombieLayer.getChildren().add(zombieContainer);
//            System.out.println("僵尸已添加到屏幕: " + zombie.getId() + " 位置: (" + x + ", " + y + ")");
//        } catch (Exception e) {
//            System.err.println("渲染僵尸错误: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
                                                                    private ImageView loadZombieAnimation(Zombie zombie, ZombieState targetState) {
                                                                        UUID zombieId = zombie.getId();
                                                                        ZombieType type = zombie.getType();

                                                                        // 1. 获取当前僵尸的视图（若已存在则复用，避免重复创建）
                                                                        ImageView zombieView = zombieViews.get(zombieId);
                                                                        if (zombieView == null) {
                                                                            zombieView = new ImageView();
                                                                            zombieView.setFitWidth(100);  // 与容器大小匹配
                                                                            zombieView.setFitHeight(100);
                                                                            zombieView.setPreserveRatio(true);
                                                                        }

                                                                        // 2. 停止当前动画（避免多动画冲突）
                                                                        Timeline existingTimeline = zombieAnimationTimelines.get(zombieId);
                                                                        if (existingTimeline != null) {
                                                                            existingTimeline.stop();
                                                                        }

                                                                        // 3. 加载目标状态的序列帧（按“类型+状态”拼接路径）
                                                                        String basePath = zombieImageBasePaths.get(type);
                                                                        int frameCount = zombieFrameCounts.getOrDefault(type, 4);
                                                                        List<Image> frames = new ArrayList<>();

                                                                        // 拼接路径规则：基础路径 + 状态缩写 + 帧索引 + .png
                                                                        // 示例：普通僵尸走路帧 → /zombies/normal/normal-walk-0.png
                                                                        String stateSuffix;
                                                                        if (targetState == ZombieState.WALKING) {
                                                                            stateSuffix = "run-";
                                                                        } else if (targetState == ZombieState.ATTACKING) {
                                                                            stateSuffix = "-"; // 明确攻击状态的后缀，避免使用模糊的"-"
                                                                        } else if (targetState == ZombieState.DEAD) {
                                                                            stateSuffix = "death-"; // 死亡状态专用后缀
                                                                        } else {
                                                                            stateSuffix = ""; // 默认值
                                                                        }
                                                                        for (int i = 0; i < frameCount; i++) {
                                                                            String imagePath = basePath + stateSuffix + (i+1) + ".png";
                                                                            try {
                                                                                Image frame = new Image(getClass().getResourceAsStream(imagePath));
                                                                                if (!frame.isError()) {
                                                                                    frames.add(frame);
                                                                                } else {
                                                                                    System.err.println("僵尸帧图片加载失败（无效图片）: " + imagePath);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                System.err.println("僵尸帧图片加载异常: " + imagePath + "，原因: " + e.getMessage());
                                                                            }
                                                                        }

                                                                        // 4. 若帧加载失败，回退到占位符
                                                                        if (frames.isEmpty()) {
                                                                            System.err.println("僵尸 " + type + " " + targetState + " 动画帧加载失败，使用占位符");
                                                                            return null;
                                                                        }

                                                                        // 5. 创建动画时间线（循环播放序列帧）
                                                                        Timeline timeline = new Timeline();
                                                                        timeline.setCycleCount(Timeline.INDEFINITE); // 无限循环
                                                                        Duration frameInterval = Duration.millis(200); // 每帧间隔200ms（可调整速度）

                                                                        // 添加每帧的切换逻辑
                                                                        // 循环创建关键帧（逐帧播放动画）
                                                                        for (int i = 0; i < frames.size(); i++) {
                                                                            // 关键：为每个循环迭代创建独立的final变量，避免Lambda捕获变化的i
                                                                            final int currentFrameIndex = i;
                                                                            ImageView finalZombieView1 = zombieView;
                                                                            KeyFrame keyFrame = new KeyFrame(
                                                                                    // 时间点使用currentFrameIndex（与Lambda中保持一致）
                                                                                    frameInterval.multiply(currentFrameIndex),
                                                                                    e -> finalZombieView1.setImage(frames.get(currentFrameIndex))
                                                                            );
                                                                            timeline.getKeyFrames().add(keyFrame);
                                                                        }
                                                                        final int firstFrame = 0;
                                                                        ImageView finalZombieView = zombieView;
                                                                        KeyFrame loopFrame = new KeyFrame(
                                                                                frameInterval.multiply(frames.size()),
                                                                                e -> finalZombieView.setImage(frames.get(firstFrame))
                                                                        );
                                                                        timeline.getKeyFrames().add(loopFrame);


                                                                        // 6. 保存时间线并启动动画
                                                                        zombieAnimationTimelines.put(zombieId, timeline);
                                                                        timeline.play();

                                                                        return zombieView;
                                                                    }
                                    private void switchZombieState(UUID zombieId, ZombieState newState) {
                                        // 1. 校验状态合法性（避免重复切换）
                                        ZombieState currentState = zombieStates.getOrDefault(zombieId, ZombieState.WALKING);
                                        if (currentState == newState) {
                                            return;
                                        }

                                        // 2. 更新状态
                                        zombieStates.put(zombieId, newState);

                                        // 3. 获取僵尸对象与视图
                                        Zombie zombie = getZombieById(zombieId); // 需实现：根据ID从zombies列表中找僵尸
                                        if (zombie == null) {
                                            System.err.println("切换状态失败：未找到僵尸 ID: " + zombieId);
                                            return;
                                        }

                                        // 4. 切换动画（根据新状态加载对应序列帧）
                                        loadZombieAnimation(zombie, newState);
                                    }

    /**
     * 工具方法：根据ID获取僵尸对象（需实现）
     */
    private Zombie getZombieById(UUID zombieId) {
        for (Zombie zombie : zombies) {
            if (zombie.getId().equals(zombieId)) {
                return zombie;
            }
        }
        return null;
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
        while (battleStarted&& !gameOver) {
            try {
                // 每5秒生成一些阳光
                Thread.sleep(2000+random.nextInt(5000));

                // 在JavaFX应用线程中更新UI
                Platform.runLater(() -> {
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
        int startY = -30; // 从屏幕顶部外开始
        int targetY = 30 + random.nextInt(400); // 目标Y坐标 (确保在500高度内)

        //Position position = new Position(x, y);

        // 创建阳光实体，并存储目标位置信息
        Sun sun = new Sun(new Position(x, startY), sunValue);
        activeSuns.add(sun);
        fallingSuns.add(sun);
        // 存储目标位置到sun对象
        sunTargetPositions.put(sun, (double) targetY);
        // 创建阳光的可视化表示

//        final Sun sun = new Sun(new Position(x, startY), sunValue);
//        activeSuns.add(sun);
//        fallingSuns.add(sun);
//        sunTargetPositions.put(sun, (double) targetY);

// 创建阳光的可视化表示（使用自定义图片）
        ImageView sunVisual;
        ImageView sunVisual1=null;
        try {
            // 加载自定义阳光图片（替换为你的图片路径）
            // 建议使用透明背景的PNG图片
            Image sunImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pic/sun.png")));

            sunVisual1 = new ImageView(sunImage);
            // 设置图片大小（根据你的图片尺寸调整）
            sunVisual1.setFitWidth(60);  // 宽度
            sunVisual1.setFitHeight(60); // 高度
            sunVisual1.setPreserveRatio(true); // 保持图片比例

            // 计算位置（使图片中心与原圆形中心一致）
            sunVisual1.setX(x - sunVisual1.getFitWidth() / 2);
            sunVisual1.setY(startY - sunVisual1.getFitHeight());


        } catch (Exception e) {
            // 图片加载失败时使用备用圆形
            System.err.println("阳光图片加载失败，使用备用图形: " + e.getMessage());
            sunVisual1 = new ImageView();
            // 创建备用圆形
            Circle fallbackCircle = new Circle(x, startY, 15);
            fallbackCircle.setFill(Color.YELLOW);
            fallbackCircle.setStroke(Color.GOLD);
            fallbackCircle.setStrokeWidth(2);
            // 将圆形添加到面板（实际项目中可使用更优雅的处理方式）
            gamePanel.getChildren().add(fallbackCircle);
        }

// 确保sunVisuals是Map<Sun, Node>类型
//        sunVisuals.put(sun, sunVisual);
//        gamePanel.getChildren().add(sunVisual);

//        Circle sunVisual = new Circle(x, startY, 15);
//        sunVisual.setFill(Color.YELLOW);
//        sunVisual.setStroke(Color.GOLD);
//        sunVisual.setStrokeWidth(2);

        // 添加点击事件
        sunVisual = sunVisual1;
        sunVisual.setOnMouseClicked(e -> {
            if (!sun.isCollected()) {
                collectSun(sun, sunVisual, sunTargetPositions);
            }
        });

        // 添加到阳光层
        sunLayer.getChildren().add(sunVisual);

        // 存储阳光视图引用，用于更新位置
        final Map<Sun, Node> sunViews = new HashMap<>();
        sunViews.put(sun, sunVisual);
        battleStatusText.setText("阳光出现了！");


       // 创建下落动画
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(20), event -> {
                    if (sun.isCollected() || !activeSuns.contains(sun)) {
                        return;
                    }

                    // 获取当前位置和目标位置
                    Position currentPos = sun.getPosition();
                    double targetYPos = sunTargetPositions.get(sun);
                    ImageView visual = (ImageView) sunViews.get(sun);


                    // 如果还没到达目标位置，继续下落
                    if (currentPos.y() < targetYPos) {
                        // 计算新位置（带加速度的下落效果）
                        double newY = currentPos.y() + 2 + (currentPos.y() - startY) * 0.01;

                        // 更新实体位置

                        Position newPos = new Position(currentPos.x(), newY);
                        sun.setPosition(newPos);

                        // 更新视图位置
                        visual.setY(newY);
                    } else {
                        // 到达目标位置后，开始轻微晃动
                        double wiggleAmount = Math.sin(System.currentTimeMillis() * 0.01) * 3;
                        visual.setY(targetYPos + wiggleAmount);
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // 阳光自动消失的定时器
        new Thread(() -> {
            try {
                Thread.sleep(10000); // 10秒后自动消失
                Platform.runLater(() -> {
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
    private void collectSun(Sun sun, Node sunVisual, Map<Sun, Double> sunTargetPositions) {
        sun.collect();
        sunBankService.addSun(sun.getValue());
        sunAmountText.setText("阳光: " + sunBankService.getSunAmount());
        battleStatusText.setText("获得阳光 +" + sun.getValue());
        System.out.println("阳光被收集，当前阳光数量: " + sunBankService.getSunAmount());

        // 从UI中移除阳光
        sunLayer.getChildren().remove(sunVisual);
        activeSuns.remove(sun);
        fallingSuns.remove(sun);
        sunTargetPositions.remove(sun);
    }
    /**
     * 设置UI更新循环
     */
    private void setupUpdateLoop() {
        // 使用JavaFX的AnimationTimer进行UI更新
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (battleStarted&& !gameOver) {
                    // 更新僵尸位置
                    updateZombies();

                    // 植物攻击逻辑
                    long currentTimeMillis = System.currentTimeMillis();
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
                            long currentTime = System.currentTimeMillis();

                            for (Zombie zombie : zombies) {
                                UUID zombieId = zombie.getId();
                                Pane container = zombieContainers.get(zombieId);

                                if (zombie.isDead()) {
                                    zombiesToRemove.add(zombieId);
                                    switchZombieState(zombieId, ZombieState.DEAD); // 切换为死亡状态（需额外实现死亡动画）
                                    continue;
                                }

                                if (container != null) {
                                    double currentX = container.getLayoutX();
                                    double speed = getZombieSpeed(zombie.getType());
                                    double newX = currentX - speed;

                                    // ---------------------- 新增：状态控制逻辑 ----------------------
                                    ZombieState currentState = zombieStates.getOrDefault(zombieId, ZombieState.WALKING);
                                    boolean hasPlant = hasPlantBlocking(zombie, newX); // 复用原有植物阻挡检测

                                    // 1. 有植物阻挡 → 切换为攻击状态，停止移动
                                    if (hasPlant) {
                                        newX = currentX; // 停止移动
                                        switchZombieState(zombieId, ZombieState.ATTACKING); // 播放攻击动画

                                        // 原有攻击逻辑（不变）
                                        Plant frontPlant = zombie.checkForPlantsAhead(plants);
                                        if (frontPlant != null && !frontPlant.isDead()) {
                                            zombie.attackPlant(currentTime, frontPlant);
                                        }
                                    }
                                    // 2. 无植物阻挡 → 切换为行走状态，恢复移动
                                    else if (currentState == ZombieState.ATTACKING) {
                                        switchZombieState(zombieId, ZombieState.WALKING); // 播放走路动画
                                    }
                                    // ----------------------------------------------------------------

                                    // 更新容器位置（仅行走状态会移动）
                                    container.setLayoutX(newX);
                                    zombie.setPosition(new Position(newX, container.getLayoutY()));

                                    // 原有游戏结束检测逻辑（不变）
                                    if (newX + container.getPrefWidth() < 82) {
                                        gameOver = true;
                                        showGameOverDialog();
                                        break;
                                    }
                                }
                            }
//        List<UUID> zombiesToRemove = new ArrayList<>();
//
//        // 获取当前时间（毫秒）
//        long currentTime = System.currentTimeMillis();
//
//        for (Zombie zombie : zombies) {
//            UUID zombieId = zombie.getId();
//            Pane container = zombieContainers.get(zombieId);
//
//            // 检查僵尸是否已经死亡
//            if (zombie.isDead()) {
//                zombiesToRemove.add(zombieId);
//                continue;
//            }
//
//            if (container != null) {
//                // 获取当前位置
//                double currentX = container.getLayoutX();
//
//                // 根据僵尸类型的移动速度更新位置
//                double speed = getZombieSpeed(zombie.getType());
//                double newX = currentX - speed;
//                // 检查是否有植物阻挡
//                if (hasPlantBlocking(zombie, newX)) {
//                    // 如果有植物阻挡，就不更新位置
//                    newX = currentX;
//
//                    // 尝试攻击前方的植物
//                    Plant frontPlant = zombie.checkForPlantsAhead(plants);
//                    if (frontPlant != null && !frontPlant.isDead()) {
//                        zombie.attackPlant(currentTime, frontPlant);
//                    }
//                }
//                // 更新容器位置
//                container.setLayoutX(newX);
//
//
//                // 更新僵尸实体位置 - 移除Y轴偏移量，确保实体位置与渲染位置一致
//                zombie.setPosition(new Position(newX, container.getLayoutY()));
//
//
//                // 检查僵尸是否到达终点（游戏失败条件）
//                if (newX + container.getPrefWidth() < 82) { // 82是游戏网格的偏移量，代表玩家的房子位置
//                    // 僵尸到达终点，游戏失败
//                    gameOver = true;
//                    showGameOverDialog();
//                    break; // 一旦检测到游戏失败，立即退出循环
//                }
//            }
//        }
        if (!gameOver) {
            // 处理小推车
            for (Cart cart : new ArrayList<>(carts)) {
                if (cart.isTriggered()) {
                    // 小推车被触发，向右移动
                    double moveSpeed = 5; // 小推车移动速度
                    Node cartView = cartViews.get(cart.getLaneIndex());

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

                        }
                    }
                }
            }
            // 移除需要删除的僵尸
            for (UUID zombieId : zombiesToRemove) {
                removeZombie(zombieId);
            }
        }
    }
    /**
     * 检查是否有植物阻挡了僵尸
     */
    private boolean hasPlantBlocking(Zombie zombie, double newX) {
        // 获取僵尸所在车道
        int laneIndex = zombie.getLaneIndex();
        //double zombieY = zombie.getPosition().y();

        // 遍历所有植物，检查是否在相同车道且阻挡了僵尸
        for (Plant plant : plants) {
            Position plantPos = plant.getPosition();

            // 计算植物所在的车道（根据Y坐标）
            int plantLane = (int)(plantPos.y() / (80 + 2));

            // 检查是否在同一车道
            if (plantLane == laneIndex) {
                // 获取植物实体的X坐标（绝对坐标）
                double plantX = plantPos.x();
                // 植物宽度约为70，僵尸宽度约为70
                // 计算植物的右边缘位置
                double plantRightEdge = plantX + 70;
                // 计算僵尸的左边缘位置（新位置）
                double zombieLeftEdge = newX;

                // 当僵尸的左边缘接近植物的右边缘时进行阻挡
                // 这里使用5像素的触发距离，使碰撞检测更灵敏
                if (zombieLeftEdge <= plantRightEdge + 75 && zombieLeftEdge > plantRightEdge - 5) {
                    // 将僵尸位置调整到植物右边缘前方约5像素的位置
                    Pane container = zombieContainers.get(zombie.getId());
                    if (container != null) {
                        // 设置僵尸刚好在植物右边缘前方
                        container.setLayoutX(plantRightEdge + 5);
                        zombie.setPosition(new Position(plantRightEdge + 5, container.getLayoutY()));
                    }
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * 获取僵尸的移动速度
     */
    private double getZombieSpeed(ZombieType type) {
        // 根据僵尸类型设置不同的移动速度
        switch (type) {
            case NORMAL:
                return 0.15;
            case FLAG:
                return 0.16;
            case BUCKETHEAD:
                return 0.1;
            case FOOTBALL:
                return 0.22;
            default:
                return 0.15;
        }
    }
    
    /**
     * 移除僵尸
     */
    private void removeZombie(UUID zombieId) {
                            // 1. 停止动画时间线
                            Timeline timeline = zombieAnimationTimelines.get(zombieId);
                            if (timeline != null) {
                                timeline.stop();
                                zombieAnimationTimelines.remove(zombieId);
                            }

                            // 2. 清理状态与视图（原有逻辑）
                            Pane container = zombieContainers.get(zombieId);
                            if (container != null && zombieLayer != null && zombieLayer.getChildren().contains(container)) {
                                zombieLayer.getChildren().remove(container);
                            }
                            zombieViews.remove(zombieId);
                            zombieContainers.remove(zombieId);
                            zombieStates.remove(zombieId); // 移除状态记录
                            zombies.removeIf(z -> z.getId().equals(zombieId));
//        // 从僵尸层中移除僵尸容器
//        Pane container = zombieContainers.get(zombieId);
//        if (container != null && zombieLayer != null && zombieLayer.getChildren().contains(container)) {
//            zombieLayer.getChildren().remove(container);
//        }
//
//        // 从映射中移除
//        zombieViews.remove(zombieId);
//        zombieContainers.remove(zombieId);
//        zombies.removeIf(z -> z.getId().equals(zombieId));
    }

    // 添加新方法：更新植物状态和处理攻击
    private void updatePlants(long now) {
        //System.out.println("updatePlants called with time: " + now + " ms");

        // 创建一个列表来存储需要移除的植物
        List<Plant> plantsToRemove = new ArrayList<>();

        // 遍历所有植物
        for (Plant plant : plants) {
            // 添加植物位置日志
            System.out.println("植物位置: " + plant.getPosition().x() + ", " + plant.getPosition().y() + ", 类型: " + plant.getType());

            // 检查樱桃炸弹是否需要爆炸
            if (plant.getType() == PlantType.CHERRY_BOMB) {
                // 直接使用已转换为毫秒的时间戳，不再重复转换
                System.out.println("调用checkAndExplodeIfNeeded - 传入时间: " + now + ", plantedTime: " + plant.getPlantedTime());
                plant.checkAndExplodeIfNeeded(now, zombies);
            }

            // 处理向日葵生产阳光
            if (plant.produceSun(now)) {
                System.out.println("向日葵生产阳光！位置: " + plant.getPosition().x() + ", " + plant.getPosition().y());
                // 向日葵生产了阳光，在植物位置上方生成阳光
                spawnSunAtPosition(plant.getPosition().x()+150, plant.getPosition().y());
            }

            // 检查植物是否死亡
            if (plant.isDead()) {
                plantsToRemove.add(plant);
                continue; // 跳过已死亡植物的其他处理
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

        // 移除死亡的植物
        for (Plant deadPlant : plantsToRemove) {
            plants.remove(deadPlant);

            // 清理植物视图
            // 从植物位置计算出对应的格子行和列（与placePlant方法保持一致的计算逻辑）
            int col = (int)(deadPlant.getPosition().x() / (80 + 2));
            int row = (int)(deadPlant.getPosition().y() / (80 + 2));

            // 获取对应的单元格并清除植物视图
            String cellKey = row + "," + col;
            Pane cell = plantCells.get(cellKey);
            if (cell != null) {
                cell.getChildren().clear(); // 清除单元格中的所有子节点（即植物视图）
            }

            System.out.println("移除死亡植物: " + deadPlant.getType().name() + ", 位置: (" + deadPlant.getPosition().x() + ", " + deadPlant.getPosition().y() + "), 格子: (" + row + ", " + col + ")");
        }
    }

    // 添加新方法：在指定位置生成阳光
    private void spawnSunAtPosition(double x, double y) {
        int sunValue = 25; // 阳光价值
        Position position = new Position(x, y);
        // 创建阳光实体
        Sun sun = new Sun(position, sunValue);
        activeSuns.add(sun);
        final ImageView sunVisual;

        try {
            Image sunImage = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/pic/sun.png"),
                    "阳光图片不存在: /pic/sun.png"
            ));

            // 2. 只初始化一次，之后不再修改（有效final）
            sunVisual = new ImageView(sunImage);
            sunVisual.setFitWidth(60);
            sunVisual.setFitHeight(60);
            sunVisual.setPreserveRatio(true);
            sunVisual.setX(x - sunVisual.getFitWidth() / 2);
            sunVisual.setY(y - sunVisual.getFitHeight() / 2);

            // 3. lambda中使用final变量是安全的
            sunVisual.setOnMouseClicked(e -> {
                if (!sun.isCollected()) {
                    // 这里引用sunVisual是安全的，因为它是final
                    collectSun(sun, sunVisual, sunTargetPositions);
                }
            });

            if (sunLayer != null) {
                sunLayer.getChildren().add(sunVisual);
            }

            battleStatusText.setText("阳光出现了！");

            // 4. 定时器lambda中使用final变量
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    Platform.runLater(() -> {
                        if (!sun.isCollected() && sunLayer != null) {
                            // 这里引用sunVisual是安全的
                            sunLayer.getChildren().remove(sunVisual);
                            activeSuns.remove(sun);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

        } catch (Exception e) {
            System.err.println("阳光图片加载失败: " + e.getMessage());
            activeSuns.remove(sun);
            // 5. 异常时必须给final变量赋值（或提前初始化）
//            sunVisual = null;
        }
//        // 创建阳光的可视化表示
//        Circle sunVisual = new Circle(position.x(), position.y(), 15);
//        sunVisual.setFill(Color.YELLOW);
//        sunVisual.setStroke(Color.GOLD);
//        sunVisual.setStrokeWidth(2);
//
//        // 添加点击事件
//        sunVisual.setOnMouseClicked(e -> {
//            if (!sun.isCollected()) {
//                collectSun(sun, sunVisual, sunTargetPositions);
//            }
//        });
//
//        // 添加到阳光层
//        sunLayer.getChildren().add(sunVisual);
//
//        battleStatusText.setText("阳光出现了！");
//
//        // 阳光自动消失的定时器
//        new Thread(() -> {
//            try {
//                Thread.sleep(10000); // 10秒后自动消失
//                Platform.runLater(() -> {
//                    if (!sun.isCollected()) {
//                        sunLayer.getChildren().remove(sunVisual);
//                        activeSuns.remove(sun);
//                    }
//                });
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }).start();
    }

    // 添加新方法：渲染子弹
    private void renderProjectile(Projectile projectile) {
        // 创建子弹视图
        Pane projectileView = new Pane();
        projectileView.setPrefSize(20, 20);
        projectileView.setStyle(getProjectileStyleByType(projectile.getType()));


        // 设置子弹初始位置 - 从植物右侧边缘发射（植物X坐标+70像素）
        double plantX = projectile.getPosition().x();
        double adjustedX = plantX + 50; // 从植物右侧边缘发射
        double y = projectile.getLaneIndex() * 82 + 5+15;

        // 同步子弹实体的位置
        Position newPosition = new Position(adjustedX, y - 15);
        projectile.setPosition(newPosition);

        projectileView.setLayoutX(adjustedX);
        projectileView.setLayoutY(y);

        // 添加调试日志，确认子弹被渲染
        System.out.println("渲染子弹: 类型=" + projectile.getType() + ", 位置=(" + adjustedX + ", " + y + "), 车道=" + projectile.getLaneIndex());

        // 保存子弹视图
        projectileViews.put(projectile, projectileView);

        // 添加到子弹层
        projectileLayer.getChildren().add(projectileView);
    }
//
    // 添加新方法：根据子弹类型获取样式
    private String getProjectileStyleByType(ProjectileType type) {
        switch (type) {
            case PEANUT:
                return "-fx-background-color: #663399; -fx-background-radius: 50%;";
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
        ImageView cartView = new ImageView();
        try {
            // 加载小推车图片（替换为你的图片路径）
            Image cartImage = new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/pic/cart.png")));

            cartView.setImage(cartImage);
            // 设置图片大小（保持原矩形的60x40尺寸）
            cartView.setFitWidth(90);
            cartView.setFitHeight(60);
            cartView.setPreserveRatio(true); // 保持图片比例

            // 可选：图片加载前的占位样式（类似原矩形）
            cartView.setStyle("-fx-background-color: #8B4513;");

        } catch (Exception e) {
            // 图片加载失败时使用原矩形作为备用
            System.err.println("小推车图片加载失败，使用备用矩形: " + e.getMessage());
            Region fallbackCart = new Region();
            fallbackCart.setPrefSize(60, 40);
            fallbackCart.setStyle("-fx-background-color: #8B4513; -fx-border-color: #000000; -fx-border-width: 2;");

            // 将备用矩形添加到图层并保存引用
            zombieLayer.getChildren().add(fallbackCart);
            cartViews.put(laneIndex, fallbackCart);
            return; // 终止当前方法，避免后续错误
        }

        // 设置小推车的位置（与原逻辑一致）
        cartView.setLayoutX(position.x());
        cartView.setLayoutY(position.y());

        // 添加到僵尸层（保持原有层级）
        zombieLayer.getChildren().add(cartView);

        // 保存小推车视图引用（ImageView是Node的子类，可直接放入原集合）
        cartViews.put(laneIndex, cartView);
    }
//        // 创建小推车视图（这里使用简单的矩形代表）
//        Region cartView = new Region();
//        cartView.setPrefSize(60, 40);
//        cartView.setStyle("-fx-background-color: #8B4513; -fx-border-color: #000000; -fx-border-width: 2;");
//
//        // 设置小推车的位置
//        cartView.setLayoutX(position.x());
//        cartView.setLayoutY(position.y());
//
//        // 添加到僵尸层（因为小推车应该在植物层之上）
//        zombieLayer.getChildren().add(cartView);
//
//        // 保存小推车视图引用
//        cartViews.put(laneIndex, cartView);

    /**
     * 铲除植物
     */
    private void removePlant(int row, int col) {
        // 获取对应的单元格
        String cellKey = row + "," + col;
        Pane cell = plantCells.get(cellKey);

        // 检查单元格是否有植物
        if (cell.getChildren().size() > 0) {
            // 计算植物位置 - 使用与placePlant相同的计算方式
            Position plantPosition = new Position(col * (80 + 2) + 5, row * (80 + 2) + 5);

            // 查找并移除对应的植物实体
            Plant plantToRemove = null;
            for (Plant plant : plants) {
                if (Math.abs(plant.getPosition().x() - plantPosition.x()) < 10 &&
                        Math.abs(plant.getPosition().y() - plantPosition.y()) < 10) {
                    plantToRemove = plant;
                    break;
                }
            }

            if (plantToRemove != null) {
                // 从列表中移除植物
                plants.remove(plantToRemove);

                // 从UI中移除植物视图
                cell.getChildren().clear();

                battleStatusText.setText("已铲除植物");

                // 返回部分阳光（例如返还一半的阳光成本）
                int refund = plantToRemove.getType().getCost() / 2;
                sunBankService.addSun(refund);
                sunAmountText.setText("阳光: " + sunBankService.getSunAmount());
                // 铲除成功后自动退出铲子模式
                isShovelMode = false;
                shovelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            }
        } else {
            battleStatusText.setText("该位置没有植物！");
        }

    }
    /**
     * 显示游戏结束对话框
     */
    private void showGameOverDialog() {
        // 停止游戏
        stopBattle();

        // 创建一个半透明的遮罩层
        Pane overlay = new Pane();
        overlay.setPrefSize(1200, 700);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setMouseTransparent(false);

        // 创建游戏结束对话框
        VBox gameOverBox = new VBox(20);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setPrefSize(400, 300);
        gameOverBox.setStyle("-fx-background-color: #283618; -fx-border-color: #bc6c25; -fx-border-width: 3; -fx-padding: 20;");
        gameOverBox.setLayoutX((1200 - 400) / 2);
        gameOverBox.setLayoutY((700 - 300) / 2);

        // 游戏结束文本
        Text gameOverText = new Text("游戏失败！");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        gameOverText.setFill(Color.RED);

        // 创建按钮容器
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // 返回选关按钮
        Button backToLevelSelectButton = new Button("返回选关");
        backToLevelSelectButton.setPrefSize(120, 40);
        backToLevelSelectButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        backToLevelSelectButton.setOnAction(e -> {
            // 移除遮罩层和对话框
            ((Pane) getRoot()).getChildren().remove(overlay);
            // 跳转到选关界面
            Router.getInstance().showLevelSelectScene();
        });

        // 重新游戏按钮
        Button restartButton = new Button("重新游戏");
        restartButton.setPrefSize(120, 40);
        restartButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        restartButton.setOnAction(e -> {
            // 移除遮罩层和对话框
            ((Pane) getRoot()).getChildren().remove(overlay);
            // 重新开始当前关卡
            restartLevel();
        });

        buttonBox.getChildren().addAll(backToLevelSelectButton, restartButton);
        gameOverBox.getChildren().addAll(gameOverText, buttonBox);
        overlay.getChildren().add(gameOverBox);

        // 将遮罩层添加到场景根节点
        ((Pane) getRoot()).getChildren().add(overlay);
    }

    /**
     * 重新开始当前关卡
     */
    private void restartLevel() {
        // 清空游戏中的所有实体
        plants.clear();
        zombies.clear();
        projectiles.clear();
        activeSuns.clear();

        // 重置游戏状态
        battleStarted = false;
        gameOver = false;
        selectedPlantType = null;
        isShovelMode = false;

        // 重置UI状态
        battleStatusText.setText("战斗准备中...");
        startButton.setDisable(false);
        startButton.setText("开始战斗");
        shovelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        // 清空植物网格
        for (Pane cell : plantCells.values()) {
            cell.getChildren().clear();
        }

        // 清空阳光、僵尸和子弹层
        sunLayer.getChildren().clear();
        zombieLayer.getChildren().clear();
        projectileLayer.getChildren().clear();

        // 清空映射
        zombieViews.clear();
        zombieContainers.clear();
        projectileViews.clear();

        // 重置阳光数量
        sunBankService.setSunAmount(50); // 设置初始阳光数量
        sunAmountText.setText("阳光: " + sunBankService.getSunAmount());

        // 重新创建小推车
        carts.clear();
        cartViews.clear();
        for (int laneIndex = 0; laneIndex < 5; laneIndex++) {
            Position cartPosition = new Position(10, laneIndex * 82 + 5 + 40);
            Cart cart = CartFactory.getInstance().createDefaultCart(cartPosition, laneIndex);
            carts.add(cart);
            renderCart(cart);
        }
    }

}
