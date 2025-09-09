pvz/                                 ← IDEA 工程根 & Git 仓库
├── pom.xml                          ← 聚合父 POM（管理版本、模块）
├── .gitignore                       ← Git 忽略规则（target、*.iml 等）
├── README.md                        ← 项目说明文档
│
├── game-core/                       ← Maven 子模块：纯游戏逻辑，零 JavaFX
│   ├── pom.xml                      ← 当前模块依赖（仅 slf4j + junit）
│   └── src/main/java/com/game/pvz
│       ├── core/                    ← Core(核心)
│       │   ├── event/               ← 事件DTO（只读记录）
│       │   │   ├─ GameEvent.java   // 所有事件的父类
│       │   │   │─ EventBus         //事件总线
│       │   │   │─ GameEventListener//监听器
│       │   │   ├─ GameTick.java     // 每帧逻辑触发事件（记录类）
│       │   │   ├─ PlantPlaced.java  // 植物放置成功事件（记录类）
│       │   │   └─ ZombieDied.java   // 僵尸死亡事件（记录类）
│       │   └── service/             ← 核心服务接口 & 实现
│       │       ├─ InputHandle         // 【接口】输入
│       │       ├─ GameLoopService.java // 【接口】游戏主循环驱动
│       │       ├─ SpawnService.java    // 【接口】刷怪/刷阳光
│       │       ├─ CollideService.java  // 【接口】碰撞检测
│       │       └─ impl/               ← 服务实现（包私有）
│       │           ├─ GameLoopServiceImpl.java // 主循环实现
│       │           ├─ SpawnServiceImpl.java    // 刷怪实现
│       │           ├─ CollideServiceImpl.java  // 碰撞实现
│       │           └─ SunBankServiceImpl.java  // 阳光账户实现
│       ├── module/                  ← 老师要求：module(模块) → 实体
│       │   ├── entity/              ← 所有游戏实体
│       │   │  ├─ GameObject.java    // 实体标记接口（空）
│       │   │  ├─ Position.java      // 不可变坐标（记录类）
│       │   │  ├─ Health.java        // 血量值对象（记录类）
│       │   │  ├── plant/
│       │   │  │  ├─ Plant.java      // 植物实体（类）
│       │   │  │  ├─ PlantType.java  // 植物类型枚举
│       │   │  │  └─ PlantFactory.java // 植物工厂（类）
│       │   │  ├── zombie/
│       │   │  │  ├─ Zombie.java     // 僵尸实体（类）
│       │   │  │  ├─ ZombieType.java // 僵尸类型枚举
│       │   │  │  └─ ZombieFactory.java // 僵尸工厂（类）
│       │   │  └── projectile/
│       │   │     ├─ Projectile.java // 子弹实体（类）
│       │   │     └─ ProjectileType.java // 子弹类型枚举
│       │   └── board/               ← 棋盘模块
│       │       ├─ Board.java        // 整个棋盘（类）
│       │       ├─ Lane.java         // 单车道（类）
│       │       └─ Row.java          // 横行（类）
│       └── physics/                 ← 物理特性
│           ├── PhysicsBody.java     // 刚体数据（记录类）
│           ├── Bounds.java          // 包围盒（记录类）
│           └── PhysicsUtils.java    // 物理工具（类）
│
├── ui-fx/                           ← Maven 子模块：JavaFX 界面
│   ├── pom.xml                      ← 引入 javafx-controls + libtiled
│   └── src/main/java/com/game/pvz
│       └── ui/                      ← 统一包：com.game.pvz.ui.*
│           ├── app/
│           │  ├─ PvzApp.java        // JavaFX Application 入口
│           │  ├─ Router.java        // 场景路由（类）
│           │  └─ ResourcePool.java  // 图片/音频缓存（类）
│           ├── scene/
│           │  ├─ BattleScene.java   // 战场场景（类）
│           │  ├─ MenuScene.java     // 主菜单场景（类）
│           │  └─ LevelSelectScene.java // 选关场景（类）
│           ├── component/
│           │  ├─ BattleCanvas.java  // 战场画布（抽象类，继承Canvas）
│           │  ├─ PlantCard.java     // 植物卡片按钮（类）
│           │  └─ SunLabel.java      // 阳光数值标签（类）
│           └── map/
│               ├─ TiledMapLoader.java // Tiled 地图加载（类）
│               ├─ MapRenderer.java   // 【接口】地图渲染器
│               ├─ MapViewport.java   // 【接口】摄像机
│               └── internal/
│                   ├─ TilesetCache.java // 图块缓存（类）
│                   └─ LayerType.java    // 图层类型枚举
│
├── desktop-launcher/                ← Maven 子模块：启动器
│   ├── pom.xml                      ← 仅依赖 javafx-maven-plugin
│   └── src/main/java/com/game/pvz
│       └── launcher/
│           └─ Launcher.java         // main 方法，启动 JavaFX
│
└── assets/                          ← 额外资源（非 Maven）
    ├── tiled/                       // Tiled 源文件（tmx/tsx/png）
    ├── images/                      // 其它 UI 图
    ├── audio/                       // 音乐/音效
    └── css/                         // 全局样式表
