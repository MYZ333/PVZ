# 植物大战僵尸 (Plants vs. Zombies) 游戏项目

## 项目简介
这是一个使用Java 17、JavaFX和Maven构建的植物大战僵尸小游戏。项目采用模块化架构设计，将游戏逻辑与UI界面分离，便于维护和扩展。

## 项目结构

```
pvz/
├── pom.xml                          # 聚合父POM（管理版本、模块）
├── .gitignore                       # Git忽略规则
├── README.md                        # 项目说明文档
│
├── game-core/                       # Maven子模块：纯游戏逻辑，零JavaFX
│   ├── pom.xml                      # 当前模块依赖
│   └── src/main/java/com/game/pvz   # 核心代码
│
├── ui-fx/                           # Maven子模块：JavaFX界面
│   ├── pom.xml                      # 界面模块依赖
│   └── src/main/java/com/game/pvz   # UI代码
│
├── desktop-launcher/                # Maven子模块：启动器
│   ├── pom.xml                      # 启动器依赖
│   └── src/main/java/com/game/pvz   # 启动代码
│
└── assets/                          # 额外资源（非Maven）
    ├── tiled/                       # Tiled源文件
    ├── images/                      # 其它UI图
    ├── audio/                       # 音乐/音效
    └── css/                         # 全局样式表
```

## 技术栈
- **Java**: 17
- **JavaFX**: 17.0.2 (界面框架)
- **Maven**: 3.8.8 (构建工具)
- **libtiled**: 1.4.3 (地图支持)
- **SLF4J**: 1.7.36 (日志框架)

## 快速开始

### 前提条件
- JDK 17
- Maven 3.8.8 或更高版本
- IDEA 2021.3 或更高版本

### 构建项目
```bash
# 在项目根目录执行
mvn clean package
```

### 运行游戏
```bash
# 通过Maven运行
mvn javafx:run -pl desktop-launcher

# 或运行生成的JAR文件
java -jar desktop-launcher/target/desktop-launcher-1.0-SNAPSHOT.jar
```

## 模块说明

### game-core
纯游戏逻辑模块，不依赖JavaFX。包含游戏实体、核心服务、物理系统等。

### ui-fx
JavaFX界面模块，负责游戏的可视化展示和用户交互。

### desktop-launcher
游戏启动器模块，包含main方法，负责启动JavaFX应用。

## 开发指南
1. 首先确保安装了JDK 17和Maven 3.8.8
2. 使用IDEA打开项目（选择pvz目录）
3. 等待Maven下载依赖
4. 运行`desktop-launcher`模块中的`Launcher.java`来启动游戏

## 版权信息
本项目仅供学习和参考使用。