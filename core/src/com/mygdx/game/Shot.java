/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author tomnil001
 */
public class Shot extends Sprite {
    private int direction;
    private float speed;
    private boolean disabled = false;
    
    
    public Shot(Texture texture, int xpos, int ypos, int dir, float sp) {
        super(texture);
        super.setPosition(xpos, ypos);
        direction=dir;
        speed=sp;
    }

    public int getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
