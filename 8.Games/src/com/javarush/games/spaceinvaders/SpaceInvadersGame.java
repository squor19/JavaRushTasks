package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

import static com.javarush.games.spaceinvaders.Direction.*;

public class SpaceInvadersGame extends Game {

    private int score;

    private final static int PLAYER_BULLETS_MAX = 1;

    private boolean isGameStopped;

    private int animationsCount;

    public final static int COMPLEXITY = 5;

    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;

    private List<Bullet> playerBullets;

    private PlayerShip playerShip;
    private void check() {
        playerShip.verifyHit(enemyBullets);
        enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }
        if (enemyFleet.getBottomBorder() >= playerShip.y) {
            playerShip.kill();
        }
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();
        }
        score += enemyFleet.verifyHit(playerBullets);
    }

    private void removeDeadBullets() {
        for (Bullet bullet : new ArrayList<>(enemyBullets)) {
            if (!bullet.isAlive || bullet.y >= HEIGHT - 1) {
                enemyBullets.remove(bullet);
            }
        }

        for (Bullet bullet : new ArrayList<>(playerBullets)) {
            if (!bullet.isAlive || bullet.y + bullet.height < 0) {
                playerBullets.remove(bullet);
            }
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color color, String value) {
        if (x > WIDTH - 1 || x < 0 || y > HEIGHT - 1 || y < 0) {
            return;
        }

        super.setCellValueEx(x, y, color, value);
    }

    private void createStars() {
        stars = new ArrayList<Star>();
        for (int i = 0; i < 8; i++) {
            stars.add(new Star(i*2, i*2));
        }
    }

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) {
            enemyBullets.add(bullet);
        }
        setScore(score);
        drawScene();
    }

    private void createGame() {
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        playerBullets = new ArrayList<>();
        drawScene();
        setTurnTimer(40);
        score = 0;
    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }

        for (Star star : stars) {
            star.draw(this);
        }
    }

    private void drawScene() {
        drawField();
        enemyFleet.draw(this);
        for (Bullet bullet : enemyBullets) {
            bullet.draw(this);
            playerShip.draw(this);
        }
        for (Bullet bullet : playerBullets) {
            bullet.draw(this);
        }
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        for (Bullet bullet : enemyBullets) {
            bullet.move();
        }
        playerShip.move();
        for (Bullet bullet : playerBullets) {
            bullet.move();
        }
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.WHEAT, "Congratulations!", Color.GREEN, 4);
        } else {
            showMessageDialog(Color.WHEAT, "You lost!", Color.RED, 4);
        }
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.SPACE && isGameStopped) {
            createGame();
        }

        switch(key) {
            case LEFT:
                playerShip.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                playerShip.setDirection(Direction.RIGHT);
                break;
            case SPACE:
                Bullet bullet = playerShip.fire();
                if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                    playerBullets.add(bullet);
                }
                break;
        }
    }


    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.LEFT && playerShip.getDirection() == LEFT || key == Key.RIGHT && playerShip.getDirection() == RIGHT) {
            playerShip.setDirection(Direction.UP);
        }
    }
}
