package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;

    private List<EnemyShip> ships;

    private Direction direction = Direction.RIGHT;


    public EnemyFleet() {
        createShips();
    }

    private void createShips() {
        ships = new ArrayList<>();
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
        Boss boss = new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5);
        ships.add(boss);
    }

    public double getBottomBorder() {
        double bottom = 0;
        for (GameObject ship : ships) {
            if (ship.y + ship.height > bottom) {
                bottom = ship.y + ship.height;
            }
        }
        return bottom;
    }

    public int getShipsCount() {
        return ships.size();
    }

    public void draw(Game game) {
        for (EnemyShip ship : ships) {
            ship.draw(game);
        }
    }

    private double getLeftBorder() {
        double leftBorder = Double.MAX_VALUE;
        for (EnemyShip ship : ships) {
            leftBorder = Math.min(ship.x, leftBorder);
        }
        return leftBorder;
    }

    private double getRightBorder() {
        double rightBorder = Double.MIN_VALUE;
        for (EnemyShip ship : ships) {
            rightBorder = Math.max(ship.x + ship.width, rightBorder);
        }
        return rightBorder;
    }

    private double getSpeed() {
        double speed = - 1;
        for (EnemyShip ship : ships) {
            speed = Math.min(2.0, 3.0 / ships.size());
        }
        return speed;
    }

    public void move() {
        if (ships.isEmpty()) {
            return;
        }

        if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            for (EnemyShip ship : ships) {
                ship.move(Direction.DOWN, getSpeed());
            }
            return;
        } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            for (EnemyShip ship : ships) {
                ship.move(Direction.DOWN, getSpeed());
            }
            return;
        }
        for (EnemyShip ship : ships) {
            ship.move(direction, getSpeed());
        }
    }

    public Bullet fire(Game game) {
        if (ships.isEmpty()) {
            return null;
        }

        int rand = game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY);

        if (rand > 0) {
            return null;
        } else {
            return ships.get(game.getRandomNumber(ships.size())).fire();
        }
    }

    public void deleteHiddenShips() {
        for (EnemyShip ship : new ArrayList<>(ships)) {
            if (!ship.isVisible()) {
                ships.remove(ship);
            }
        }
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.isEmpty()) {
            return 0;
        }

        int result = 0;
        for (Bullet bullet : bullets) {
            for (EnemyShip ship : ships) {
                if (ship.isAlive && bullet.isAlive && ship.isCollision(bullet)) {
                    ship.kill();
                    bullet.kill();
                    result += ship.score;
                }
            }
        }

        return result;
    }
}
