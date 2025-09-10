package com.game.pvz.ui.map;

/**
 * Tiled 地图加载（类）
 */
public class TiledMapLoader {
    
    public static final String DEFAULT_MAP_PATH = "/tiled/levels/level1.tmx";
    
    public static Object loadMap(String path) {
        // 实现加载Tiled地图的逻辑
        return null;
    }
    
    public static Object loadDefaultMap() {
        return loadMap(DEFAULT_MAP_PATH);
    }
}