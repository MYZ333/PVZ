# PVZ
pvz-root/                          ← Maven 聚合父模块目录
├── pom.xml                        ← Maven 根 POM（packaging=pom）
├── game-core/                     ← Maven 子模块目录
│   ├── pom.xml                    ← Maven 子模块 POM
│   └── src
│       ├── main/
│       │   ├── java/              ← Java 源码根目录
│       │   │   └── com/pvz/game/
│       │   │       ├── api/       ← Java 包（接口）
│       │   │       ├── event/     ← Java 包（事件DTO）
│       │   │       ├── model/     ← Java 包（实体类）
│       │   │       ├── rule/      ← Java 包（规则接口）
│       │   │       └── internal/  ← Java 包（实现类，包私有）
│       │   └── resources/         ← 资源目录（非代码）
│       └── test/                  ← 单元测试目录
├── ui-fx/                         ← Maven 子模块目录
│   ├── pom.xml                    ← Maven 子模块 POM
│   └── src
│       ├── main/
│       │   ├── java/              ← Java 源码根目录
│       │   │   └── com/pvz/ui/
│       │   │       ├── main/      ← Java 包（App入口）
│       │   │       ├── scene/     ← Java 包（场景类）
│       │   │       ├── component/ ← Java 包（控件类）
│       │   │       ├── map/       ← Java 包（地图加载）
│       │   │       └── internal/  ← Java 包（内部工具）
│       │   └── resources/         ← 资源目录（fxml/css/图片）
│       └── test/
├── desktop-launcher/              ← Maven 子模块目录
│   ├── pom.xml                    ← Maven 子模块 POM
│   └── src
│       └── main/
│           ├── java/              ← Java 源码根目录
│           │   └── com/pvz/desktop/
│           └── resources/         ← 资源目录（module-info）
└── .idea/                         ← IDEA 自动生成的项目配置目录
    ├── misc.xml                   ← IDEA 项目配置（XML）
    ├── modules.xml                ← IDEA 模块列表（XML）
    └── *.iml                      ← IDEA 模块文件（XML）
