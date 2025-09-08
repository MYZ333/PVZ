pvz/                                 ← IDEA 工程根 & Git 仓库
├── pom.xml                          ← Maven 聚合父 POM
├── .gitignore
├── README.md
│
├── game-core/                       ← Maven 子模块
│   ├── pom.xml
│   └── src
│       ├── main/
│       │   ├── java/
│       │   │   └── com/pvz/game/
│       │   │       ├── api/
│       │   │       │  ├─ GameLauncher.java          【接口】启动停止
│       │   │       │  ├─ GameEventBus.java          【接口】事件总线
│       │   │       │  ├─ GameStateObserver.java     【接口】状态监听
│       │   │       │  └─ Level.java                 【接口】关卡配置
│       │   │       ├── event/
│       │   │       │  ├─ GameEvent.java             // 事件基类
│       │   │       │  ├─ GameTick.java              // 每帧事件
│       │   │       │  ├─ PlantPlaced.java           // 植物放置
│       │   │       │  ├─ ZombieDied.java            // 僵尸死亡
│       │   │       │  └─ SunCollected.java          // 阳光收集
│       │   │       ├── model/
│       │   │       │  ├─ GameObject.java            // 实体标记
│       │   │       │  ├─ Position.java              // 坐标值对象
│       │   │       │  ├─ Health.java                // 血量值对象
│       │   │       │  ├── plant/
│       │   │       │  │  ├─ Plant.java              // 植物实体
│       │   │       │  │  ├─ PlantType.java          // 植物类型枚举
│       │   │       │  │  └─ PlantFactory.java       // 植物工厂（类）
│       │   │       │  ├── zombie/
│       │   │       │  │  ├─ Zombie.java             // 僵尸实体
│       │   │       │  │  ├─ ZombieType.java         // 僵尸类型枚举
│       │   │       │  │  └─ ZombieFactory.java      // 僵尸工厂（类）
│       │   │       │  ├── projectile/
│       │   │       │  │  ├─ Projectile.java         // 子弹实体
│       │   │       │  │  └─ ProjectileType.java     // 子弹类型枚举
│       │   │       │  ├── sun/
│       │   │       │  │  ├─ Sun.java                // 阳光实体
│       │   │       │  │  └─ SunBank.java            // 阳光账户（类）
│       │   │       │  └── board/
│       │   │       │     ├─ Board.java              // 棋盘
│       │   │       │     ├─ Lane.java               // 车道
│       │   │       │     └─ Row.java                // 横行
│       │   │       ├── rule/
│       │   │       │  ├─ Spawner.java               【接口】刷怪
│       │   │       │  ├─ Collider.java              【接口】碰撞
│       │   │       │  ├─ GameLoop.java              【接口】主循环
│       │   │       │  └─ SunBank.java               【接口】阳光交易
│       │   │       └── internal/
│       │   │          ├─ GameLauncherImpl.java      // 启动实现
│       │   │          ├─ GameLoopImpl.java          // 循环实现
│       │   │          ├─ SpawnerImpl.java           // 刷怪实现
│       │   │          ├─ ColliderImpl.java          // 碰撞实现
│       │   │          └─ SunBankImpl.java           // 阳光实现
│       │   └── resources/
│       └── test/
│
├── ui-fx/                           ← Maven 子模块
│   ├── pom.xml
│   └── src
│       ├── main/
│       │   ├── java/
│       │   │   └── com/pvz/ui/
│       │   │       ├── main/
│       │   │       │  ├─ App.java                   // JavaFX入口
│       │   │       │  ├─ AppRouter.java             // 场景路由（类）
│       │   │       │  └─ ResourcePool.java          // 资源缓存（类）
│       │   │       ├── scene/
│       │   │       │  ├─ BattleScene.java           // 战场场景（类）
│       │   │       │  ├─ MenuScene.java             // 主菜单场景（类）
│       │   │       │  └─ LevelSelectScene.java      // 选关场景（类）
│       │   │       ├── component/
│       │   │       │  ├─ BattleCanvas.java          // 战场画布（抽象类）
│       │   │       │  ├─ PlantCard.java             // 植物卡片（类）
│       │   │       │  └─ SunLabel.java              // 阳光标签（类）
│       │   │       ├── map/
│       │   │       │  ├─ TiledMapLoader.java        // Tiled加载（类）
│       │   │       │  ├─ MapRenderer.java           【接口】地图渲染
│       │   │       │  ├─ MapViewport.java           【接口】摄像机
│       │   │       │  └── internal/
│       │   │       │     ├─ TilesetCache.java       // 图块缓存（类）
│       │   │       │     └─ LayerType.java          // 图层枚举
│       │   │       └── internal/
│       │   │          ├─ ImagePool.java             // 图片池（类）
│       │   │          └─ AudioPool.java             // 音频池（类）
│       │   └── resources/
│       │       ├── tiled/
│       │       │  ├─ level1.tmx
│       │       │  ├─ level1.tsx
│       │       │  └─ terrain.png
│       │       ├── images/
│       │       │  ├─ plants/
│       │       │  └─ zombies/
│       │       ├── fxml/
│       │       │  ├─ battle.fxml
│       │       │  └─ menu.fxml
│       │       └── css/
│       │          └─ style.css
│       └── test/
│
├── desktop-launcher/                ← Maven 子模块
│   ├── pom.xml
│   └── src
│       └── main/
│           ├── java/
│           │   └── com/pvz/desktop/
│           │       └─ PvzMain.java  // 启动main
│           └── resources/
│               └── META-INF/
│                   └── (可选)
│
└── assets/                          ← 额外资源目录（非Maven）
    ├── tiled/                       // 地图源文件
    ├── images/                      // 其它UI图
    ├── audio/                       // 音乐/音效
    └── css/                         // 全局样式
