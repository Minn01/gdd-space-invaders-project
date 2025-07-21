package gdd.scene;

import gdd.*;

import static gdd.Global.*;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.sprite.AlienUFO;
import gdd.sprite.Enemy;
import gdd.sprite.Explosion;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene1 extends JPanel {

    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;
    // private Shot shot;

    private int lives = 3;

    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;

    final int BLOCKS_TO_DRAW = BOARD_HEIGHT / BLOCKHEIGHT;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();

    private Timer timer;
    private final Game game;

    int score = 0;

    private int currentRow = -1;
    // TODO load this map from a file
    private int mapOffset = 0;
    private final int[][] MAP = {
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }
    };

    private String fadeMessage = "";
    private int fadeAlpha = 0;
    private int fadeTimer = 0;
    private boolean isFading = false;

    // Stage tracking
    private int currentStage = 1;
    private boolean stage2MessageShown = false;
    private boolean stage3MessageShown = false;

    private HashMap<Integer, SpawnDetails> spawnMap = new HashMap<>();
    private AudioPlayer audioPlayer;
    private int lastRowToShow;
    private int firstRowToShow;

    public Scene1(Game game) {
        this.game = game;
        // initBoard();
        // gameInit();
        loadSpawnDetails();
    }

    private void initAudio() {
        try {
            String filePath = "src/audio/scene1.wav";
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void loadSpawnDetails() {
//        // TODO load this from a file
//        spawnMap.put(50, new SpawnDetails("PowerUp-SpeedUp", 100, 0));
//        spawnMap.put(200, new SpawnDetails("AlienUFO", 200, 0));
//        spawnMap.put(300, new SpawnDetails("AlienUFO", 300, 0));
//
//        spawnMap.put(400, new SpawnDetails("AlienUFO", 400, 0));
//        spawnMap.put(401, new SpawnDetails("AlienUFO", 450, 0));
//        spawnMap.put(402, new SpawnDetails("AlienUFO", 500, 0));
//        spawnMap.put(403, new SpawnDetails("AlienUFO", 550, 0));
//
//        spawnMap.put(500, new SpawnDetails("AlienUFO", 100, 0));
//        spawnMap.put(501, new SpawnDetails("AlienUFO", 150, 0));
//        spawnMap.put(502, new SpawnDetails("AlienUFO", 200, 0));
//        spawnMap.put(503, new SpawnDetails("AlienUFO", 350, 0));
        try {
            // Load from external file
            spawnMap = SpawnDetailsLoader.loadFromCSV("src/gdd/spawns.csv");
            System.out.println("Loaded " + spawnMap.size() + " spawn entries from CSV");
        } catch (IOException e) {
            System.err.println("Error loading spawn data from CSV: " + e.getMessage());
        }
    }

    // function to spawn anything from spawn map
    private void spawnEntity() {
        // Check enemy spawn
        SpawnDetails sd = spawnMap.get(frame);
        if (sd != null) {
            for (int i = 0; i < sd.getCount(); i++) {
                int xPosition = sd.getX() + (sd.getSpacing() * i);

                // Create a new enemy based on the spawn details
                switch (sd.getType()) {
                    case ALIEN_UFO: // rename this type to "AlienUFO" in your spawnMap too for clarity
                        Enemy ufo = new AlienUFO(xPosition, sd.getY());
                        enemies.add(ufo);
                        System.out.println("Entity Spawned at frame: " + frame);
                        break;
                    // Add more cases for different enemy types if needed
                    case ALIEN:
                        // Enemy enemy2 = new Alien2(sd.x, sd.y);
                        // enemies.add(enemy2);
                        break;
                    case SPEED_BOOST:
                        // Handle speed up item spawn
                        PowerUp speedUp = new SpeedUp(xPosition, sd.getY());
                        powerups.add(speedUp);
                        System.out.println("Entity Spawned at frame: " + frame);
                        break;
                    default:
                        System.out.println("Unknown enemy type: " + sd.getType());
                        break;
                }
            }
        }
    }

    private void initBoard() {

    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.black);

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();

        gameInit();
        initAudio();
    }

    public void stop() {
        timer.stop();
        try {
            if (audioPlayer != null) {
                audioPlayer.stop();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void gameInit() {

        enemies = new ArrayList<>();
        powerups = new ArrayList<>();
        explosions = new ArrayList<>();
        shots = new ArrayList<>();

        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 6; j++) {
        // var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
        // ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
        // enemies.add(enemy);
        // }
        // }
        player = new Player();
        // shot = new Shot();
    }

    private void drawMap(Graphics g) {
        // Draw scrolling starfield background

        // Calculate smooth scrolling offset (1 pixel per frame)
        int scrollOffset = (frame) % BLOCKHEIGHT;

        // Calculate which rows to draw based on screen position
        int baseRow = (frame) / BLOCKHEIGHT;
        int rowsNeeded = (BOARD_HEIGHT / BLOCKHEIGHT) + 2; // +2 for smooth scrolling

        // Loop through rows that should be visible on screen
        for (int screenRow = 0; screenRow < rowsNeeded; screenRow++) {
            // Calculate which MAP row to use (with wrapping)
            int mapRow = (baseRow + screenRow) % MAP.length;

            // Calculate Y position for this row
            // int y = (screenRow * BLOCKHEIGHT) - scrollOffset;
            int y = BOARD_HEIGHT - ((screenRow * BLOCKHEIGHT) - scrollOffset);

            // Skip if row is completely off-screen
            if (y > BOARD_HEIGHT || y < -BLOCKHEIGHT) {
                continue;
            }

            // Draw each column in this row
            for (int col = 0; col < MAP[mapRow].length; col++) {
                if (MAP[mapRow][col] == 1) {
                    // Calculate X position
                    int x = col * BLOCKWIDTH;

                    // Draw a cluster of stars
                    drawStarCluster(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
                }
            }
        }

    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 24)); // Use monospaced for equal character width

        // Format score with leading zeros (8 digits)
        String formattedScore = String.format("%08d", score);

        // Draw score at top-left
        g.drawString(formattedScore, 20, 40);
    }

    private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
        // Set star color to white
        g.setColor(Color.WHITE);

        // Draw multiple stars in a cluster pattern
        // Main star (larger)
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g.fillOval(centerX - 2, centerY - 2, 4, 4);

        // Smaller surrounding stars
        g.fillOval(centerX - 15, centerY - 10, 2, 2);
        g.fillOval(centerX + 12, centerY - 8, 2, 2);
        g.fillOval(centerX - 8, centerY + 12, 2, 2);
        g.fillOval(centerX + 10, centerY + 15, 2, 2);

        // Tiny stars for more detail
        g.fillOval(centerX - 20, centerY + 5, 1, 1);
        g.fillOval(centerX + 18, centerY - 15, 1, 1);
        g.fillOval(centerX - 5, centerY - 18, 1, 1);
        g.fillOval(centerX + 8, centerY + 20, 1, 1);
    }

    private void drawAliens(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPowreUps(Graphics g) {

        for (PowerUp p : powerups) {

            if (p.isVisible()) {

                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }

            if (p.isDying()) {

                p.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        for (Shot shot : shots) {

            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void drawBombing(Graphics g) {

        // for (Enemy e : enemies) {
        // Enemy.Bomb b = e.getBomb();
        // if (!b.isDestroyed()) {
        // g.drawImage(b.getImage(), b.getX(), b.getY(), this);
        // }
        // }
    }

    private void drawExplosions(Graphics g) {

       // List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                // explosion.act();
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
                // explosion.visibleCountDown();
                // if (!explosion.isVisible()) {
                //     toRemove.add(explosion);
                // }
            }
        }

        //explosions.removeAll(toRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.white);
        g.drawString("FRAME: " + frame, 10, 10);

        g.setColor(Color.green);

        if (inGame) {

            if (!isFading) {
                drawMap(g); // Draw background stars first
            }
            drawAliens(g);
            drawPowreUps(g);
            drawExplosions(g);
            drawPlayer(g);
            drawShot(g);
            drawScore(g);
            drawFadeMessage(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    private void update() {
        if (!isFading) {
            spawnEntity();
        }

        if (frame % 60 == 0) {
            score++;
        }

//        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
//            inGame = false;
//            timer.stop();
//            message = "Game won!";
//        }

        // for stage transitions
        checkStageTransitions();
        updateFadeMessage();

        // player
        player.act();

        // Power-ups
        for (PowerUp powerup : powerups) {
            if (powerup.isVisible()) {
                powerup.act();
                if (powerup.collidesWith(player)) {
                    powerup.upgrade(player);
                }
            }
        }

        // Enemies
        // using iterator so that you can remove elements within a list whilst looping over them :)
        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();

            if (enemy.isVisible()) {
                int y = enemy.getY();

                if (y >= BOARD_HEIGHT) {
                    iterator.remove();
                } else {
                    enemy.act(direction); // only act if not removed
                }
            }
        }
        // explosion

        List<Explosion> explosionsToRemove = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.act(); // Advance animation frame
            if (!explosion.isVisible()) {
                explosionsToRemove.add(explosion);
            }
        }
        explosions.removeAll(explosionsToRemove);

        // shot
        List<Shot> shotsToRemove = new ArrayList<>();
        for (Shot shot : shots) {

            if (shot.isVisible()) {
                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Enemy enemy : enemies) {
                    // Collision detection: shot and enemy
                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    int enemyWidth = enemy.getWidth();
                    int enemyHeight = enemy.getHeight();

                    if (enemy.isVisible() && shot.isVisible()
                            && shotX >= (enemyX)
                            && shotX <= (enemyX + enemyWidth)
                            && shotY >= (enemyY)
                            && shotY <= (enemyY + enemyHeight)) {

                        // var ii = new ImageIcon(IMG_EXPLOSION);
                        // enemy.setImage(ii.getImage());

                        score += 5;
                        explosions.add(new Explosion(enemyX, enemyY));

                        deaths++;
                        enemy.setDying(true);
                        shot.die();
                        shotsToRemove.add(shot);
                    }
                }

                int y = shot.getY();
                // y -= 4;
                y -= 20;

                if (y < 0) {
                    shot.die();
                    shotsToRemove.add(shot);
                } else {
                    shot.setY(y);
                }
            }
        }
        shots.removeAll(shotsToRemove);

        // enemies
        // for (Enemy enemy : enemies) {
        // int x = enemy.getX();
        // if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
        // direction = -1;
        // for (Enemy e2 : enemies) {
        // e2.setY(e2.getY() + GO_DOWN);
        // }
        // }
        // if (x <= BORDER_LEFT && direction != 1) {
        // direction = 1;
        // for (Enemy e : enemies) {
        // e.setY(e.getY() + GO_DOWN);
        // }
        // }
        // }
        // for (Enemy enemy : enemies) {
        // if (enemy.isVisible()) {
        // int y = enemy.getY();
        // if (y > GROUND - ALIEN_HEIGHT) {
        // inGame = false;
        // message = "Invasion!";
        // }
        // enemy.act(direction);
        // }
        // }
        // bombs - collision detection
        // Bomb is with enemy, so it loops over enemies
        /*
         * for (Enemy enemy : enemies) {
         * 
         * int chance = randomizer.nextInt(15);
         * Enemy.Bomb bomb = enemy.getBomb();
         * 
         * if (chance == CHANCE && enemy.isVisible() && bomb.isDestroyed()) {
         * 
         * bomb.setDestroyed(false);
         * bomb.setX(enemy.getX());
         * bomb.setY(enemy.getY());
         * }
         * 
         * int bombX = bomb.getX();
         * int bombY = bomb.getY();
         * int playerX = player.getX();
         * int playerY = player.getY();
         * 
         * if (player.isVisible() && !bomb.isDestroyed()
         * && bombX >= (playerX)
         * && bombX <= (playerX + PLAYER_WIDTH)
         * && bombY >= (playerY)
         * && bombY <= (playerY + PLAYER_HEIGHT)) {
         * 
         * var ii = new ImageIcon(IMG_EXPLOSION);
         * player.setImage(ii.getImage());
         * player.setDying(true);
         * bomb.setDestroyed(true);
         * }
         * 
         * if (!bomb.isDestroyed()) {
         * bomb.setY(bomb.getY() + 1);
         * if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
         * bomb.setDestroyed(true);
         * }
         * }
         * }
         */
    }

    // Method to trigger a fade message
    private void showFadeMessage(String message) {
        fadeMessage = message;
        fadeAlpha = 0;
        fadeTimer = 0;
        isFading = true;

        // Initialize action lines
        showActionLines = true;
        actionLineTimer = 0;
        actionLines.clear();
    }

    private void checkStageTransitions() {
        // Stage 2 transition
        if (frame >= STAGE_1_END && !stage2MessageShown) {
            enemies.clear();
            currentStage = 2;
            showFadeMessage("Your enemies grow stronger...");
            stage2MessageShown = true;
        }

        // Stage 3 transition
        if (frame >= STAGE_2_END && !stage3MessageShown) {
            enemies.clear();
            currentStage = 3;
            showFadeMessage("The final assault begins...");
            stage3MessageShown = true;
        }
    }

    // Add this to your update() method to handle fading
    private void updateFadeMessage() {
        if (isFading) {
            fadeTimer++;

            if (fadeTimer <= FADE_IN_FRAMES) {
                // Fade in
                fadeAlpha = (int) (255 * ((double) fadeTimer / FADE_IN_FRAMES));
            } else if (fadeTimer <= FADE_IN_FRAMES + FADE_HOLD_FRAMES) {
                // Hold at full opacity
                fadeAlpha = 255;
            } else if (fadeTimer <= FADE_IN_FRAMES + FADE_HOLD_FRAMES + FADE_OUT_FRAMES) {
                // Fade out
                int fadeOutProgress = fadeTimer - FADE_IN_FRAMES - FADE_HOLD_FRAMES;
                fadeAlpha = 255 - (int) (255 * ((double) fadeOutProgress / FADE_OUT_FRAMES));
            } else {
                // Fade complete
                isFading = false;
                fadeAlpha = 0;
                fadeMessage = "";
                showActionLines = false; // Stop action lines when fade is complete
            }
        }

        // Handle action lines
        if (showActionLines) {
            actionLineTimer++;

            // Start showing action lines after a delay
            if (actionLineTimer >= ACTION_LINE_DELAY) {
                // Generate new action lines periodically
                if (actionLineTimer % 3 == 0) { // Every 3 frames
                    for (int i = 0; i < 5; i++) {
                        int x = randomizer.nextInt(BOARD_WIDTH);
                        int y = -randomizer.nextInt(100); // Start above screen
                        int length = 20 + randomizer.nextInt(40);
                        int speed = 8 + randomizer.nextInt(12);
                        actionLines.add(new ActionLine(x, y, length, speed));
                    }
                }
            }

            // Update existing action lines
            List<ActionLine> linesToRemove = new ArrayList<>();
            for (ActionLine line : actionLines) {
                line.update();
                if (!line.isVisible()) {
                    linesToRemove.add(line);
                }
            }
            actionLines.removeAll(linesToRemove);

            // Stop action lines after duration
            if (actionLineTimer >= ACTION_LINE_DURATION) {
                showActionLines = false;
                actionLines.clear();
            }
        }
    }

    private void drawFadeMessage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw action lines first (behind the message)
        if (showActionLines && actionLineTimer >= ACTION_LINE_DELAY) {
            for (ActionLine line : actionLines) {
                line.draw(g2d);
            }
        }

        if (isFading && fadeAlpha > 0) {
            // Set up font and get metrics
            Font messageFont = new Font("Arial", Font.BOLD, 36);
            FontMetrics fm = g2d.getFontMetrics(messageFont);

            // Calculate position to center the text
            int textWidth = fm.stringWidth(fadeMessage);
            int textHeight = fm.getHeight();
            int x = (BOARD_WIDTH - textWidth) / 2;
            int y = (BOARD_HEIGHT - textHeight) / 2;

            // Create semi-transparent background
            g2d.setColor(new Color(0, 0, 0, Math.min(fadeAlpha, 150))); // Dark background
            g2d.fillRoundRect(x - 20, y - textHeight + 10, textWidth + 40, textHeight + 20, 10, 10);

            // Draw the text with fade effect
            g2d.setColor(new Color(255, 255, 255, fadeAlpha)); // White text
            g2d.setFont(messageFont);
            g2d.drawString(fadeMessage, x, y);

            // Optional: Add a subtle glow effect
            g2d.setColor(new Color(255, 255, 0, fadeAlpha / 3)); // Yellow glow
            g2d.drawString(fadeMessage, x - 1, y - 1);
            g2d.drawString(fadeMessage, x + 1, y + 1);
        }
    }

    // Add these fields to your Scene1 class
    private boolean showActionLines = false;
    private int actionLineTimer = 0;
    private final int ACTION_LINE_DURATION = 180; // 3 seconds at 60 FPS
    private final int ACTION_LINE_DELAY = 30; // Start action lines after 0.5 seconds of fade message
    private List<ActionLine> actionLines = new ArrayList<>();

    // ActionLine helper class - add this inside Scene1 or as a separate class
    private class ActionLine {
        private int x, y;
        private int length;
        private int speed;
        private Color color;
        private int alpha;

        public ActionLine(int x, int y, int length, int speed) {
            this.x = x;
            this.y = y;
            this.length = length;
            this.speed = speed;
            this.alpha = 255;
            // Vary the color slightly for more dynamic effect
            int variation = randomizer.nextInt(50);
            this.color = new Color(200 + variation, 200 + variation, 255);
        }

        public void update() {
            y += speed;
            // Fade out as they move
            alpha = Math.max(0, alpha - 3);
        }

        public void draw(Graphics2D g2d) {
            if (alpha > 0) {
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, y, x, y + length);
            }
        }

        public boolean isVisible() {
            return y < BOARD_HEIGHT + length && alpha > 0;
        }
    }

    private void doGameCycle() {
        if (!isFading) {
            frame++;
//            frame += 10;
        }
        update();
        repaint();
    }

    private void saveScore(int score) {
        String json = "{ \"highScore\": " + score + " }";
        try (FileWriter file = new FileWriter("score.json")) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int loadScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("score.json"))) {
            String line = reader.readLine();
            if (line != null) {
                // very simple manual parsing
                line = line.replaceAll("[^0-9]", "");  // removes non-digits
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // default if file not found or error
    }


    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
//            System.out.println("Scene2.keyPressed: " + e.getKeyCode());

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame) {
                System.out.println("Shots: " + shots.size());
                if (shots.size() < 4) {
                    // Create a new shot and add it to the list
                    Shot shot = new Shot(x, y);
                    shots.add(shot);
                }
            }

        }
    }
}
