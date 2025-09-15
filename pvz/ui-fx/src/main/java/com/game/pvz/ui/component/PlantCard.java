package com.game.pvz.ui.component;

import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
/**
 * 植物卡片按钮（类）
 */
public class PlantCard extends Button {
    
    private String plantType;
    private int cost;
    private boolean isReady;
    private Timeline cooldownTimeline;
<<<<<<< HEAD

=======
>>>>>>> f301b6087c14b4819f630b2622fbff8764c4f319
    public PlantCard(String plantType, int cost) {
        this.plantType = plantType;
        this.cost = cost;
        this.isReady = true;
        initialize();
    }
    
    private void initialize() {
<<<<<<< HEAD

       // setText(plantType); // 设置按钮文本为植物类型名称
        //setFont(Font.font("Arial", FontWeight.BOLD, 12)); // 设置字体样式
        //setPrefSize(80, 150);
        //setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");// 实现植物卡片的初始化逻辑
=======
        setText(plantType); // 设置按钮文本为植物类型名称
        setFont(Font.font("Arial", FontWeight.BOLD, 12)); // 设置字体样式
        setPrefSize(80, 150);
        setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");// 实现植物卡片的初始化逻辑
>>>>>>> f301b6087c14b4819f630b2622fbff8764c4f319
    }
    
    public String getPlantType() {
        return plantType;
    }
    
    public int getCost() {
        return cost;
    }
    
    public boolean isReady() {
        return isReady;
    }
    
    public void setReady(boolean ready) {
        isReady = ready;
        // 根据是否就绪更新按钮样式
        if (isReady) {
            setStyle("-fx-background-color: #bc6c25; -fx-text-fill: white;");
            setDisable(false);
        } else {
            setStyle("-fx-background-color: #333; -fx-text-fill: #999;");
            setDisable(true);
        }
    }

    public void startCooldown(int cooldownTimeMs) {
        setReady(false);

        // 取消之前的冷却动画（如果存在）
        if (cooldownTimeline != null) {
            cooldownTimeline.stop();
        }

        // 创建新的冷却时间线
        cooldownTimeline = new Timeline(
                new KeyFrame(Duration.millis(cooldownTimeMs), e -> {
                    setReady(true);
                })
        );
        cooldownTimeline.play();
    }
}