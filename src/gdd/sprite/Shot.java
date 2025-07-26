package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Shot extends Sprite {
    private static final int H_SPACE = 20;
    private static final int V_SPACE = 1;
    private int dx = 0;  // horizontal velocity
    private int dy = -2; // vertical velocity (negative = up)

    public Shot() {
    }

    public Shot(int x, int y) {
        initShot(x, y);
    }

    public Shot(int x, int y, int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        initShot(x, y);
    }

    @Override
    public void act() {
        // Move bullet based on its velocity
        setX(getX() + dx);
        setY(getY() + dy);
    }

    private void initShot(int x, int y) {
        var ii = new ImageIcon(IMG_SHOT);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }

    // Getters for velocity (if needed elsewhere)
    public int getDx() { return dx; }
    public int getDy() { return dy; }
}