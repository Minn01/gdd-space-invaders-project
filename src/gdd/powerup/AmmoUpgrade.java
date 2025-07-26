package gdd.powerup;

import gdd.sprite.Player;

import javax.swing.*;

import static gdd.Global.IMG_POWERUP_AMMO;

public class AmmoUpgrade extends PowerUp {
    public AmmoUpgrade(int x, int y) {
        super(x, y);
        ImageIcon ii = new ImageIcon(IMG_POWERUP_AMMO);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() ,
                ii.getIconHeight() ,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void upgrade(Player player) {
        // Upgrade the player with ammo boost
        if (player.getBulletStage() <= 4) {
            player.setBulletStage(player.getBulletStage() + 1);
        }
        this.die(); // Remove the power-up after use
    }

    @Override
    public void act() {
        this.y += 2;
    }
}
