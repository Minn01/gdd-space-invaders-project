package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;

public class Shot extends Sprite {

    private static final int V_OFFSET = 5;
    private static final double SCALE = 3.0;

    public Shot() {}

    public Shot(int shipX, int shipY, int shipWidth) {
        super(); // Call Sprite constructor
        initShot(shipX, shipY, shipWidth);
    }

    private void initShot(int shipX, int shipY, int shipWidth) {
        var ii = new ImageIcon(IMG_SHOT);
        var baseImage = ii.getImage();

        int scaledWidth = (int)(baseImage.getWidth(null) * SCALE);
        int scaledHeight = (int)(baseImage.getHeight(null) * SCALE);

        Image scaledShot = baseImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        setImage(scaledShot);

        // Position shot at center of ship
        int shotX = shipX + (shipWidth - scaledWidth) / 2;
        int shotY = shipY - V_OFFSET;

        setX(shotX);
        setY(shotY);
        setVisible(true); // Make sure shot is visible

        System.out.println("Shot created at (" + shotX + ", " + shotY + ") with size " + scaledWidth + "x" + scaledHeight);
    }

    @Override
    public void act() {
        // Move the shot upward
        y -= 10; // adjust shot speed if needed
        if (y < 0) visible = false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}