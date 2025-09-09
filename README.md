#仓库名

pvz

####项目介绍
《植物大战僵尸》（PvZ）是一个基于经典塔防游戏设计的仿真项目，旨在通过JavaFX实现游戏的完整玩法。项目主要分为两个大模块：核心逻辑与UI界面。核心逻辑包括游戏的实体对象、事件机制、碰撞检测、主循环等，UI部分则负责呈现用户交互界面、场景切换、动画展示等。

该项目的目标是实现一个简化版的植物大战僵尸游戏，支持玩家在不同场景中放置植物、击败僵尸等基本操作，同时关注代码的模块化、可维护性和可扩展性。游戏的核心部分采用纯Java实现，不涉及图形框架或引擎，而UI界面则使用JavaFX进行渲染和交互。

工具：

JDK17 

maven3.8.8

#######软件架构说明

本项目采用了模块化设计思想，整体分为三个主要模块：
game-core：核心游戏逻辑模块，负责所有与游戏逻辑相关的部分，例如实体对象、事件管理、碰撞检测等。
ui-fx：用户界面模块，基于JavaFX开发，负责游戏界面的渲染和用户交互。
desktop-launcher：启动器模块，负责启动游戏程序和初始化JavaFX环境。
assets：存放游戏所需的静态资源（图片、音频、地图等）。

######接口定义
1. GameEventListener 接口
作用：用于监听和处理游戏中的各种事件（如植物放置、僵尸死亡等）。

2. GameLoopService 接口
作用：管理游戏主循环，控制游戏状态的更新频率和逻辑流。

3. SpawnService 接口
作用：负责生成僵尸、植物以及其他游戏元素。

4. CollideService 接口
作用：处理游戏实体（植物、僵尸等）之间的碰撞检测。

5. TiledMapLoader 接口
作用：加载Tiled地图，解析并生成游戏的战斗区域。


####Game上下文
GameContext 是整个游戏的核心上下文，它负责管理整个游戏的状态、游戏实体、事件系统等。该类是game-core模块中的一部分，封装了游戏运行的核心逻辑，并提供接口供其他模块进行交互。

核心职责：
GameState：维护游戏当前的状态（如开始、进行中、暂停等）。
GameEntities：管理游戏中所有的实体（植物、僵尸、子弹等）。
EventBus：事件总线，用于事件的发布和监听。
GameLoopService：游戏主循环，控制游戏进程的更新。
InputHandle：处理玩家的输入（鼠标点击、键盘操作等）。

Game模块
1. 事件模块（Event）

该模块包括：
GameEvent：所有事件的父类，其他具体事件继承自此类。
EventBus：事件总线，用于发布和分发事件。
GameEventListener：事件监听器，监听并处理不同类型的游戏事件。

2. 实体模块（Entity）

该模块包括：
GameObject：所有游戏实体的标记接口。
Plant、Zombie、Projectile：具体的游戏实体（植物、僵尸、子弹）。
Position：实体位置，用于描述游戏中实体的坐标。
Health：实体的生命值。

3. 核心服务模块（Service）

该模块包括：

InputHandle：输入处理接口，负责管理玩家输入。
GameLoopService：游戏主循环驱动，更新游戏状态。
SpawnService：刷怪和刷阳光服务，生成僵尸、植物和阳光。
CollideService：碰撞检测服务，判断植物与僵尸之间的交互。

4. 物理模块（Physics）

该模块包括：

PhysicsBody：游戏实体的物理属性，如速度、加速度等。
Bounds：用于实体碰撞检测的包围盒。
PhysicsUtils：物理计算工具类。

5. UI模块

通过JavaFX呈现游戏界面，处理不同场景的展示与交互，如战场场景、主菜单、选关场景等。
BattleCanvas：战场画布，用于渲染植物、僵尸、子弹等。
SunLabel：阳光数值标签，用于展示当前阳光数量。
PlantCard：植物卡片按钮，用于选择不同类型的植物。

#pvz/                                 ← IDEA 工程根 & Git 仓库
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
│       │   └── board/               ← 棋盘模块(eg:草坪)
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

####贡献者 <br>
孟羿臻 张博 符锦城  张景夫  董思奇
