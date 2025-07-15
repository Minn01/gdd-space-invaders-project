package gdd.sprite;

import static gdd.Global.*;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 270;
    private static final int START_Y = 540;

    private int width;
    private int height;
    private int currentSpeed = 5;

    public boolean rightPressed = false;
    public boolean leftPressed = false;
    public boolean upPressed = false;
    public boolean downPressed = false;

    // This bound can be used to detect collisions
    private Rectangle bounds = new Rectangle(175,135,17,32);

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        var ii = new ImageIcon(IMG_PLAYER);

        // Scale the image to use the global scaling factor
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        width = ii.getIconWidth();
        height = ii.getIconHeight();

        setX(START_X);
        setY(START_Y);
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public int setSpeed(int speed) {
        if (speed < 1) {
            speed = 1; // Ensure speed is at least 1
        }
        this.currentSpeed = speed;
        return currentSpeed;
    }

    public void act() {
        x += dx;
        y += dy;

        // for left side border limit
        if (x <= 2) {
            x = 2;
        }

        // for right side border limit
        if (x >= BOARD_WIDTH - width * 3) {
            x = BOARD_WIDTH - width * 3;
        }

        // for upper side border limit
        if (y < 2) {
            y = 2;
        }

        // for lower side border limit
        if (y >= BOARD_HEIGHT - height * 6) {
            y = BOARD_HEIGHT - height * 6;
        }
        System.out.printf("x: %d, y: %d\n", x, y);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1 * currentSpeed;
            leftPressed = true;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = currentSpeed;
            rightPressed = true;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1 * currentSpeed;
            upPressed = true;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = currentSpeed;
            downPressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
            if (rightPressed) {
                dx = currentSpeed;
            } else {
                dx = 0;
            }
        }

        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            if (leftPressed) {
                dx = -1 * currentSpeed;
            } else {
                dx = 0;
            }
        }

        if (key == KeyEvent.VK_UP) {
            upPressed = false;
            if (downPressed) {
                dy = currentSpeed;
            } else {
                dy = 0;
            }
        }

        if (key == KeyEvent.VK_DOWN) {
            downPressed = false;
            if (upPressed) {
                dy = -1 * currentSpeed;
            } else {
                dy = 0;
            }
        }
    }
}
