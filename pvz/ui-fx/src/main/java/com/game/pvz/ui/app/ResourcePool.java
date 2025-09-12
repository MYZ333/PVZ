package com.game.pvz.ui.app;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片/音频缓存（类）
 * 负责加载和缓存游戏中的资源
 */
public class ResourcePool {
    
    private static final ResourcePool INSTANCE = new ResourcePool();
    
    private final Map<String, Image> imageCache = new HashMap<>();
    private final Map<String, Media> audioCache = new HashMap<>();
    
    private ResourcePool() {
        // 私有构造函数，防止外部实例化
    }
    
    public static ResourcePool getInstance() {
        return INSTANCE;
    }
    
    /**
     * 加载并缓存图片资源
     */
    public Image getImage(String path) {
        return imageCache.computeIfAbsent(path, p -> {
            try {
                return new Image(getClass().getResourceAsStream(p));
            } catch (Exception e) {
                System.err.println("Failed to load image: " + p);
                e.printStackTrace();
                return null;
            }
        });
    }
    
    /**
     * 加载并缓存音频资源
     */
    public Media getAudio(String path) {
        return audioCache.computeIfAbsent(path, p -> {
            try {
                return new Media(getClass().getResource(p).toExternalForm());
            } catch (Exception e) {
                System.err.println("Failed to load audio: " + p);
                e.printStackTrace();
                return null;
            }
        });
    }
    
    /**
     * 清除所有缓存的资源
     */
    public void clear() {
        imageCache.clear();
        audioCache.clear();
    }
    
    /**
     * 预加载所有游戏资源
     */
    public void preloadAllResources() {
        // 实现预加载资源的逻辑
    }
}