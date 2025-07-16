package gdd.sprite;

import static gdd.Global.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import javax.swing.ImageIcon;

public class Explosion extends Sprite {
    private BufferedImage spriteSheet;



    private int animationCounter = 0;
    private int currentFrameIndex = 0;
    private final int ANIMATION_DELAY = 2;
    private Rectangle currentFrame;

    //private static final String ACT_FLYING = "FLYING";
    private static final String ACT_EXPLOSION = "EXPLOSION";
    private String action = ACT_EXPLOSION;

    private final Rectangle[] explosionFrames = new Rectangle[] {
        new Rectangle(388, 173, 31, 24),
        new Rectangle(328, 173, 31, 24),
        new Rectangle(269, 172, 30, 25),
        new Rectangle(207, 173, 32, 24),
        new Rectangle(150, 171, 29, 26),
        new Rectangle(90, 171, 29, 26),
        new Rectangle(31, 169, 28, 29)
};


    public Explosion(int x, int y) {

        initExplosion(x, y);
    }

    private void initExplosion(int x, int y) {

        this.x = x;
        this.y = y;
        ImageIcon ii = new ImageIcon(IMG_ALIEN_UFO);
        setImage(ii.getImage());
        currentFrame = explosionFrames[0];
        //act();

        // var ii = new ImageIcon(IMG_EXPLOSION);

        // // Scale the image to use the global scaling factor
        // var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
        //         ii.getIconHeight() * SCALE_FACTOR,
        //         java.awt.Image.SCALE_SMOOTH);
        // setImage(scaledImage);
    }

    @Override
    public Image getImage() {
        if (image == null || currentFrame == null) {
            
            return image;
        }
        BufferedImage bImage = toBufferedImage(image);
        int frameX = currentFrame.x;
        int frameY = currentFrame.y;
        int frameWidth = currentFrame.width;
        int frameHeight = currentFrame.height;

        // Clamp frame dimensions to image bounds to avoid exceptions
        if (frameX + frameWidth > bImage.getWidth()) {
            frameWidth = bImage.getWidth() - frameX;
        }
        if (frameY + frameHeight > bImage.getHeight()) {
            frameHeight = bImage.getHeight() - frameY;
        }

        try {
            return bImage.getSubimage(frameX, frameY, frameWidth, frameHeight);
        } catch (RasterFormatException e) {
            // Fallback if something still goes wrong
            return bImage;
        }
    }



    public void act(int direction) {

        // this.x += direction;
    }


    @Override
    public void act() {
        animationCounter++;
        if (animationCounter % ANIMATION_DELAY == 0) {
            currentFrameIndex++;
            if (currentFrameIndex >= explosionFrames.length) {
                setVisible(false);
               
                currentFrame = null; // Avoid drawing invalid frames
            } else {
                currentFrame = explosionFrames[currentFrameIndex];
            }
        }

    }
}
