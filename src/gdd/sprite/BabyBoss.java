// BabyBoss.java
package gdd.sprite;

import gdd.sprite.Enemy;
import static gdd.Global.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.swing.ImageIcon;

public class BabyBoss extends Enemy {
    private final Rectangle[] frames = {
            new Rectangle(0, 6, 153, 138) // You can later customize multiple frames if animated
    };

    private Rectangle currentFrame = frames[0];
    private double angleToPlayer;
    private double speed = 2.5; // Slightly increased speed for better gameplay
    private Player targetPlayer;

    public BabyBoss(int x, int y, Player player) {
        super(x, y);
        this.type = "BabyBoss";
        this.targetPlayer = player;
        this.setImage(new ImageIcon(IMG_BOSS).getImage());

        // Calculate initial movement angle
        updateAngleToPlayer();
    }

    private void updateAngleToPlayer() {
        if (targetPlayer != null && targetPlayer.isVisible()) {
            int px = targetPlayer.getX() + targetPlayer.getWidth() / 2;
            int py = targetPlayer.getY() + targetPlayer.getHeight() / 2;
            int myX = this.x + getWidth() / 2;
            int myY = this.y + getHeight() / 2;

            double dx = px - myX;
            double dy = py - myY;
            angleToPlayer = Math.atan2(dy, dx);
        }
    }

    public void chasePlayer(Player player) {
        if (player != null && player.isVisible()) {
            this.targetPlayer = player;
            updateAngleToPlayer();
            act();
        }
    }

    @Override
    public void act() {
        // Update angle to player every few frames for better tracking
        if (Math.random() < 0.1) { // 10% chance per frame to recalculate
            updateAngleToPlayer();
        }

        // Move towards player
        this.x += (int)(Math.cos(angleToPlayer) * speed);
        this.y += (int)(Math.sin(angleToPlayer) * speed);

        // Keep baby boss on screen bounds
        if (this.x < 0) this.x = 0;
        if (this.x > BOARD_WIDTH - getWidth()) this.x = BOARD_WIDTH - getWidth();
        if (this.y < 0) this.y = 0;
        if (this.y > BOARD_HEIGHT - getHeight()) this.y = BOARD_HEIGHT - getHeight();
    }

    @Override
    public Image getImage() {
        if (image == null) return null;

        BufferedImage bImage = toBufferedImage(image);
        Rectangle r = currentFrame;
        BufferedImage sub;
        try {
            sub = bImage.getSubimage(r.x, r.y, r.width, r.height);
        } catch (RasterFormatException e) {
            return bImage;
        }
        return sub.getScaledInstance((int)(r.width * 0.4), (int)(r.height * 0.4), Image.SCALE_SMOOTH);
    }

    @Override
    public int getWidth() {
        return (int)(currentFrame.width * 0.4);
    }

    @Override
    public int getHeight() {
        return (int)(currentFrame.height * 0.4);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}