package com.game.pvz.ui.map.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * 图块缓存（类）
 */
public class TilesetCache {
    
    private static final TilesetCache INSTANCE = new TilesetCache();
    
    private final Map<String, Object> tilesetCache = new HashMap<>();
    
    private TilesetCache() {
        // 私有构造函数，防止外部实例化
    }
    
    public static TilesetCache getInstance() {
        return INSTANCE;
    }
    
    public void addTileset(String key, Object tileset) {
        tilesetCache.put(key, tileset);
    }
    
    public Object getTileset(String key) {
        return tilesetCache.get(key);
    }
    
    public boolean containsTileset(String key) {
        return tilesetCache.containsKey(key);
    }
    
    public void removeTileset(String key) {
        tilesetCache.remove(key);
    }
    
    public void clear() {
        tilesetCache.clear();
    }
}