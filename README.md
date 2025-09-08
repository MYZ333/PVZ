game-core/src/main/java/com/pvz/game
├─ api                           // 对外唯一门面
│  ├─ GameLauncher.java          // 【接口】启动/停止游戏
│  ├─ GameEventBus.java          // 【接口】全局事件总线
│  ├─ GameStateObserver.java     // 【接口】状态监听回调
│  └─ Level.java                 // 【接口】关卡配置
├─ event                         // 事件DTO
│  ├─ GameEvent.java             // 事件基类（sealed）
│  ├─ GameTick.java              // 每帧逻辑事件
│  ├─ PlantPlaced.java           // 植物放置事件
│  ├─ ZombieDied.java            // 僵尸死亡事件
│  └─ SunCollected.java          // 阳光收集事件
├─ model                         // 纯数据
│  ├─ GameObject.java            // 实体标记接口
│  ├─ Position.java              // 不可变坐标
│  ├─ Health.java                // 血量值对象
│  ├─ plant
│  │  ├─ Plant.java              // 植物实体
│  │  ├─ PlantType.java          // 植物类型枚举
│  │  └─ PlantFactory.java       // 植物工厂（类）
│  ├─ zombie
│  │  ├─ Zombie.java             // 僵尸实体
│  │  ├─ ZombieType.java         // 僵尸类型枚举
│  │  └─ ZombieFactory.java      // 僵尸工厂（类）
│  ├─ projectile
│  │  ├─ Projectile.java         // 子弹实体
│  │  └─ ProjectileType.java     // 子弹类型枚举
│  ├─ sun
│  │  ├─ Sun.java                // 阳光实体
│  │  └─ SunBank.java            // 阳光账户（类）
│  └─ board
│     ├─ Board.java              // 战场棋盘
│     ├─ Lane.java               // 单条车道
│     └─ Row.java                // 横向行
├─ rule                          // 规则接口
│  ├─ Spawner.java               // 【接口】刷怪/刷阳光
│  ├─ Collider.java              // 【接口】碰撞检测
│  ├─ GameLoop.java              // 【接口】主循环
│  └─ SunBank.java               // 【接口】阳光交易
└─ internal                      // 实现（包私有）
   ├─ GameLauncherImpl.java      // 启动器实现
   ├─ GameLoopImpl.java          // 主循环实现
   ├─ SpawnerImpl.java           // 刷怪实现
   ├─ ColliderImpl.java          // 碰撞实现
   └─ SunBankImpl.java           // 阳光实现

ui-fx/src/main/java/com/pvz/ui
├─ main
│  ├─ App.java                   // JavaFX Application
│  ├─ AppRouter.java             // 场景路由（类）
│  └─ ResourcePool.java          // 资源缓存（类）
├─ scene
│  ├─ BattleScene.java           // 战场场景（类）
│  ├─ MenuScene.java             // 主菜单场景（类）
│  └─ LevelSelectScene.java      // 选关场景（类）
├─ component
│  ├─ BattleCanvas.java          // 战场画布（抽象类）
│  ├─ PlantCard.java             // 植物卡片控件（类）
│  └─ SunLabel.java              // 阳光标签控件（类）
├─ map
│  ├─ TiledMapLoader.java        // Tiled地图加载（类）
│  ├─ MapRenderer.java           // 【接口】地图渲染
│  ├─ MapViewport.java           // 【接口】摄像机
│  └─ internal
│     ├─ TilesetCache.java       // 图块缓存（类）
│     └─ LayerType.java          // 图层类型枚举
└─ internal
   ├─ ImagePool.java             // 图片池（类）
   └─ AudioPool.java             // 音频池（类）

desktop-launcher/src/main/java/com/pvz/desktop
└─ PvzMain.java                  // 启动器main类（含module-info）
