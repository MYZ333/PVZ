package com.game.pvz.core.service;

import java.awt.event.KeyEvent;

public interface InputHandler {
    void onKeyPressed(KeyEvent keyEvent);
    void onKeyReleased(KeyEvent keyEvent);
//    void onMousePressed(int button);
//    void onMouseReleased(int button);
//    void onMouseMoved(double x, double y);
}
