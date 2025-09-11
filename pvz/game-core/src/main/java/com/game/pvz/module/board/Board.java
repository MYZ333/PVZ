package com.game.pvz.module.board;

import java.util.ArrayList;
import java.util.List;

/**
 * 整个棋盘（类）
 */
public class Board {
    private final int width;
    private final int height;
    private final List<Lane> lanes;
    private final List<Row> rows;
    
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.lanes = new ArrayList<>();
        this.rows = new ArrayList<>();
        
        // 初始化车道和行
        for (int i = 0; i < height; i++) {
            lanes.add(new Lane(i));
        }
        
        for (int i = 0; i < width; i++) {
            rows.add(new Row(i));
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public List<Lane> getLanes() {
        return lanes;
    }
    
    public List<Row> getRows() {
        return rows;
    }
    
    public Lane getLane(int index) {
        if (index >= 0 && index < lanes.size()) {
            return lanes.get(index);
        }
        return null;
    }
    
    public Row getRow(int index) {
        if (index >= 0 && index < rows.size()) {
            return rows.get(index);
        }
        return null;
    }
}