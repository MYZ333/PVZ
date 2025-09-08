pvz/                                    ← IDEA 工程根
├── pom.xml                             ← 聚合父 POM
├── .gitignore
├── README.md
│
├── game-core/                          ← Maven 子模块（老师说的“Core+物理特性”）
│   ├── pom.xml
│   └── src/main/java/com/game/pvz
│       ├── core/                      ← 老师要求：Core(核心)
│       │   ├── event/                 ← 事件DTO
│       │   │   ├─ GameEvent.java
│       │   │   ├─ GameTick.java
│       │   │   ├─ PlantPlaced.java
│       │   │   └─ ZombieDied.java
│       │   └── service/               ← 核心服务（原 rule + internal）
│       │       ├─ GameLoopService.java 【接口】主循环
│       │       ├─ SpawnService.java    【接口】刷怪
│       │       ├─ CollideService.java  【接口】碰撞
│       │       └─ impl/               ← 实现类（包私有）
│       │           ├─ GameLoopServiceImpl.java
│       │           ├─ SpawnServiceImpl.java
│       │           ├─ CollideServiceImpl.java
│       │           └─ SunBankServiceImpl.java
│       ├── module/                    ← 老师要求：module(模块) → 实体
│       │   ├── entity/                ← 所有实体
│       │   │  ├─ GameObject.java
│       │   │  ├─ Position.java
│       │   │  ├─ Health.java
│       │   │  ├── plant/
│       │   │  │  ├─ Plant.java
│       │   │  │  ├─ PlantType.java
│       │   │  │  └─ PlantFactory.java
│       │   │  ├── zombie/
│       │   │  │  ├─ Zombie.java
│       │   │  │  ├─ ZombieType.java
│       │   │  │  └─ ZombieFactory.java
│       │   │  └── projectile/
│       │   │     ├─ Projectile.java
│       │   │     └─ ProjectileType.java
│       │   └── board/               ← 棋盘模块
│       │       ├─ Board.java
│       │       ├─ Lane.java
│       │       └─ Row.java
│       └── physics/                 ← 老师要求：物理特性
│           ├── PhysicsBody.java     // 刚体数据
│           ├── Bounds.java          // 包围盒
│           └── PhysicsUtils.java    // 工具计算
│
├── ui-fx/                            ← Maven 子模块（老师说的“UI”）
│   ├── pom.xml
│   └── src/main/java/com/game/pvz
│       └── ui/                      ← 统一包名：com.game.pvz.ui.*
│           ├── app/
│           │  ├─ PvzApp.java        // JavaFX Application
│           │  ├─ Router.java        // 场景路由
│           │  └─ ResourcePool.java  // 资源池
│           ├── scene/
│           │  ├─ BattleScene.java
│           │  ├─ MenuScene.java
│           │  └─ LevelSelectScene.java
│           ├── component/
│           │  ├─ BattleCanvas.java
│           │  ├─ PlantCard.java
│           │  └─ SunLabel.java
│           └── map/
│               ├─ TiledMapLoader.java
│               ├─ MapRenderer.java   【接口】
│               ├─ MapViewport.java   【接口】
│               └── internal/
│                   ├─ TilesetCache.java
│                   └─ LayerType.java
│
├── desktop-launcher/                 ← 启动器（最小可运行）
│   ├── pom.xml
│   └── src/main/java/com/game/pvz
│       └── launcher/
│           └─ Launcher.java          // main 方法
│
└── assets/                           ← 额外资源
    ├── tiled/
    ├── images/
    ├── audio/
    └── css/
