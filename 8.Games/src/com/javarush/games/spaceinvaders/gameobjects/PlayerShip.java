package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.security.Key;
import java.util.List;

public class PlayerShip extends Ship {

    private Direction direction = Direction.UP;

    public void setDirection(Direction newDirection) {
        direction = newDirection != Direction.DOWN ? newDirection : direction;
    }

    @Override
    public boolean isCollision(GameObject gameObject) {
        return super.isCollision(gameObject);
    }

    public void verifyHit(List<Bullet> bullets) {
        if (bullets.isEmpty()) {
            return;
        }

        for (Bullet bullet : bullets) {
            if (isAlive && bullet.isAlive) {
                if (isCollision(bullet)) {
                    kill();
                    bullet.kill();
                }
            }
        }

    }

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
        setStaticView(ShapeMatrix.PLAYER);
    }

    @Override
    public void kill() {
        if (isAlive) {
            isAlive = false;
            super.setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST, ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
        }
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }

    public void move() {
        if (!isAlive) {
            return;
        }

        switch(direction) {
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }

        if (x < 0) {
            x = 0;
        }

        if (x + width > SpaceInvadersGame.WIDTH) {
            x = SpaceInvadersGame.WIDTH - width;
        }
    }


    public Direction getDirection() {
        return direction;
    }

    @Override
    public Bullet fire() {
        if (!isAlive) {
            return null;
        }

        return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
    }
}
