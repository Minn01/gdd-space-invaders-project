package gdd.powerup;

import gdd.sprite.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static gdd.Global.IMG_POWERUP_HEART;

public class Life extends PowerUp {
    private int animationCounter = 0;
    private int clipNo = 0;
    private final int ANIMATION_DELAY = 8;
    private Rectangle currentFrame;
    private final Rectangle[] heartFrames = new Rectangle[] {
            new Rectangle(82, 80, 40, 39),  // heart1 (bottom right)
            new Rectangle(41, 80, 40, 39),  // heart2 (bottom middle)
            new Rectangle(82, 40, 40, 39),  // heart3 (middle right)
            new Rectangle(82, 0, 40, 39),   // heart4 (top right)
            new Rectangle(41, 0, 40, 39),   // heart5 (top middle)
            new Rectangle(0, 80, 40, 39),   // heart6 (bottom left)
            new Rectangle(41, 40, 40, 39),  // heart7 (middle middle)
            new Rectangle(0, 0, 40, 39),    // heart8 (top left)
            new Rectangle(0, 40, 40, 39)    // heart9 (middle left)
    };
    public Life(int x, int y) {
        super(x, y);
        ImageIcon ii = new ImageIcon(IMG_POWERUP_HEART);
        setImage(ii.getImage());
        currentFrame = heartFrames[clipNo];
    }

    public Image getImage() {
        Rectangle bound = heartFrames[clipNo];
        // TODO this can be cached.
        BufferedImage bImage = toBufferedImage(image);
        return bImage.getSubimage(bound.x, bound.y, bound.width, bound.height);
    }

    @Override
    public void upgrade(Player player) {
        player.setLives(player.getLives()+1);
        this.die();
    }

    @Override
    public void act() {
        this.y += 2;
        animationCounter++;
        if (animationCounter % ANIMATION_DELAY == 0) {
            clipNo = (clipNo + 1) % heartFrames.length;
            currentFrame = heartFrames[clipNo];
        }

    }
}
