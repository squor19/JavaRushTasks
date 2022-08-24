package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class Ship extends GameObject {

    private boolean loopAnimation = false;
    private List<int[][]> frames;
    private int frameIndex;

    public boolean isAlive = true;

    public void kill() {
        isAlive = false;
    }

    public Ship(double x, double y) {
        super(x, y);
    }


    public boolean isVisible() {
        if (!isAlive && frameIndex >= frames.size()) {
            return false;
        } else {
            return true;
        }
    }

    public void setStaticView(int[][] viewFrame) {
        setMatrix(viewFrame);
        frames = new ArrayList<int[][]>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    public Bullet fire() {
        return null;
    }

    public void nextFrame() {
        frameIndex++;
        
        if (frameIndex >= frames.size()) {
            if (loopAnimation) {
                frameIndex = 0;
            } else {
                return;
            }
        }
        
        matrix = frames.get(frameIndex);

    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    public void setAnimatedView(boolean isLoopAnimation, int[][]... viewFrames) {
        loopAnimation = isLoopAnimation;
    }
}
