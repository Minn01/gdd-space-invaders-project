package gdd.powerup;

import gdd.sprite.Player;

import javax.swing.*;
import java.awt.*;

import static gdd.Global.IMG_POWERUP_SHIELD;
import java.awt.geom.Ellipse2D;


public class Shield extends PowerUp {
    private Timer shieldTimer = null;
    private static int shieldRadius = 40;

    public Shield(int x, int y) {
        super(x, y);

        ImageIcon ii = new ImageIcon(IMG_POWERUP_SHIELD);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() ,
                ii.getIconHeight() ,
                java.awt.Image.SCALE_SMOOTH
        );
        setImage(scaledImage);
    }

    @Override
    public void upgrade(Player player) {
        player.setShieldActive(true);
        shieldTimer = new Timer(1000, e -> {
            player.setShieldActive(false);
            shieldTimer = null;
        });
        shieldTimer.setRepeats(false);
        shieldTimer.start();
        this.die();
    }

    @Override
    public void act() {
        this.y += 2;
    }

    public static void drawActiveShield(Player player, Graphics2D g2d) {
        if (player.isShieldActive()) {
            // Save the original composite
            Composite originalComposite = g2d.getComposite();

            // Set 50% transparency
            float alpha = 0.25f;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.CYAN);
            g2d.fillOval(
                    player.getX() + player.getWidth()/2 - shieldRadius,
                    player.getY() + player.getHeight()/2 - shieldRadius,
                    shieldRadius * 2,
                    shieldRadius * 2
            );

            // Restore the original composite
            g2d.setComposite(originalComposite);
        }
    }

    public void disposeShieldTimer() {
        shieldTimer.stop();
        shieldTimer = null;
    }
}
