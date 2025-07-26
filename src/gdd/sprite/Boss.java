package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Boss extends Enemy {

    private final Rectangle[] bossFrames = new Rectangle[]{
            new Rectangle(0, 6, 153, 138),
            new Rectangle(153, 7, 163, 136),
            new Rectangle(315, 7, 154, 137),
            new Rectangle(473, 6, 151, 138),
            new Rectangle(633, 11, 153, 132),
            new Rectangle(793, 10, 151, 133),
            new Rectangle(952, 6, 146, 138),
            new Rectangle(1104, 7, 156, 137),
            new Rectangle(1264, 6, 158, 138),
            new Rectangle(1424, 6, 153, 138)
    };

    private int currentFrameIndex = 0;
    private int animationCounter = 0;
    private final int ANIMATION_DELAY = 6;
    private final double SCALE = 1.3;

    private Rectangle currentFrame;

    private List<BabyBoss> babyBosses = new ArrayList<>();
    private int waveCooldown = 0;
    private int waveCount = 0;
    private final int MAX_WAVES = 5;
    private final int WAVE_INTERVAL = 300; // ~10s at 30fps (30 * 10 = 300)

    private Player player;

    public Boss(int x, int y, Player player) {
        super(x, y);
        this.type = "Boss";
        this.player = player;
        initBoss(x, y);
    }

    private void initBoss(int x, int y) {
        this.x = x;
        this.y = y;
        setImage(new ImageIcon(IMG_BOSS).getImage());
        currentFrame = bossFrames[0];
    }

    @Override
    public void act() {
        // Animate the boss
        animationCounter++;
        if (animationCounter >= ANIMATION_DELAY) {
            currentFrameIndex = (currentFrameIndex + 1) % bossFrames.length;
            currentFrame = bossFrames[currentFrameIndex];
            animationCounter = 0;
        }

        // Handle baby boss waves
        if (waveCount < MAX_WAVES) {
            waveCooldown++;
            if (waveCooldown >= WAVE_INTERVAL) {
                spawnBabyBosses();
                waveCooldown = 0;
                waveCount++;
                System.out.println("Wave " + waveCount + " spawned! (" + babyBosses.size() + " baby bosses)");
            }
        }

        // Update all baby bosses
        Iterator<BabyBoss> iterator = babyBosses.iterator();
        while (iterator.hasNext()) {
            BabyBoss bb = iterator.next();
            if (bb.isVisible()) {
                bb.chasePlayer(player);
            } else {
                iterator.remove(); // Remove dead baby bosses
            }
        }
    }

    public List<BabyBoss> getBabyBosses() {
        return babyBosses;
    }

    private void spawnBabyBosses() {
        // Clear previous wave (optional - you can remove this if you want accumulated waves)
        babyBosses.clear();

        int baseX = this.x + this.getWidth() / 2;
        int baseY = this.y + this.getHeight();

        int spacing = 50; // Increased spacing for better visibility

        // U-shaped formation with 7 baby bosses
        int[] offsetsX = {-3, -2, -1, 0, 1, 2, 3};
        int[] offsetsY = {50, 30, 15, 0, 15, 30, 50}; // U shape - outer ones lower

        for (int i = 0; i < 7; i++) {
            int bx = baseX + offsetsX[i] * spacing;
            int by = baseY + offsetsY[i];

            // Ensure baby bosses spawn within screen bounds
            bx = Math.max(25, Math.min(BOARD_WIDTH - 75, bx));
            by = Math.max(0, Math.min(BOARD_HEIGHT - 100, by));

            BabyBoss baby = new BabyBoss(bx, by, player);
            babyBosses.add(baby);
        }
    }

    @Override
    public Image getImage() {
        if (image == null || currentFrame == null) return null;

        BufferedImage bImage = toBufferedImage(image);
        Rectangle r = currentFrame;

        try {
            BufferedImage sub = bImage.getSubimage(r.x, r.y, r.width, r.height);
            int scaledW = (int) (r.width * SCALE);
            int scaledH = (int) (r.height * SCALE);
            return sub.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
        } catch (RasterFormatException e) {
            return bImage;
        }
    }

    @Override
    public int getWidth() {
        return currentFrame != null ? (int)(currentFrame.width * SCALE) : super.getWidth();
    }

    @Override
    public int getHeight() {
        return currentFrame != null ? (int)(currentFrame.height * SCALE) : super.getHeight();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    // Method to get current wave information (for debugging/UI)
    public int getCurrentWave() {
        return waveCount;
    }

    public int getMaxWaves() {
        return MAX_WAVES;
    }

    public boolean isWaveActive() {
        return !babyBosses.isEmpty();
    }

    // Method to manually trigger next wave (for testing)
    public void forceNextWave() {
        if (waveCount < MAX_WAVES) {
            spawnBabyBosses();
            waveCount++;
            waveCooldown = 0;
        }
    }
}