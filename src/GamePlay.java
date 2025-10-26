import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePlay {
    private GameState gameState;
    private Player player;
    private Boss boss;
    private ArrayList<Projectile> projectiles;
    private UI gameUI;
    
    // Game timer (60 seconds countdown)
    private int gameTimer;
    private int maxGameTime;
    
    // Game initialization flag
    private boolean initialized;
    
    public GamePlay(GameState gameState) {
        this.gameState = gameState;
        this.projectiles = new ArrayList<>();
        this.gameUI = new UI();
        this.maxGameTime = 3600; // 60 seconds at 60 FPS
        this.initialized = false;
    }
    
    private void initialize() {
        // Reset game timer
        gameTimer = maxGameTime;
        
        // Create player at starting position
        player = new Player(100, HunterArena.SCREEN_HEIGHT / 2 - 50);
        
        // Create boss based on selection
        if (GameData.selectedBoss == 0) {
            boss = new Teostra(HunterArena.SCREEN_WIDTH - 200, HunterArena.SCREEN_HEIGHT / 2 - 50);
        } else {
            boss = new Velkhana(HunterArena.SCREEN_WIDTH - 200, HunterArena.SCREEN_HEIGHT / 2 - 50);
        }
        
        // Clear projectiles
        projectiles.clear();
        
        initialized = true;
    }
    
    public void update() {
        // Initialize if not done yet
        if (!initialized) {
            initialize();
        }
        
        // Update game timer
        gameTimer--;
        
        // Check win/lose conditions
        if (boss.isDead()) {
            // Player wins
            GameData.gameWon = true;
            GameData.gameTime = (maxGameTime - gameTimer) / 60; // Convert to seconds
            gameState.setState(GameState.State.GAME_OVER);
            initialized = false; // Reset for next game
            return;
        }
        
        if (player.isDead() || gameTimer <= 0) {
            // Player loses
            GameData.gameWon = false;
            gameState.setState(GameState.State.GAME_OVER);
            initialized = false; // Reset for next game
            return;
        }
        
        // Update player
        player.update();
        
        // Update boss
        boss.update();
        
        // Handle player attacks
        if (GameData.selectedWeapon == 1) { // Bow
            createArrow();
        }
        // Sword attacks are handled in checkCollisions() method via getAttackBounds()
        
        // Handle boss attacks
        handleBossAttacks();
        
        // Update projectiles
        updateProjectiles();
        
        // Check collisions
        checkCollisions();
    }
    
    private void createArrow() {
        if (player.isAttacking() && !player.hasShot()) {
            // ยิงธนูเพียงครั้งเดียวต่อการโจมตี
            float arrowX = player.getX() + (player.isFacingRight() ? 50 : -10);
            float arrowY = player.getY() + 25;
            float arrowSpeed = 8.0f;
            float arrowDx = player.isFacingRight() ? arrowSpeed : -arrowSpeed;
            
            
            String arrowSprite = player.isFacingRight() ? "assets/sprites/ArrowRight.png" : "assets/sprites/ArrowLeft.png";
            Projectile arrow = new Projectile(arrowX, arrowY, arrowDx, 0, arrowSprite, 20, true);
            projectiles.add(arrow);
            
            // เซ็ตว่าได้ยิงแล้วในการโจมตีนี้
            player.setHasShot(true);
        }
    }

    private void handleBossAttacks() {
        if (boss.isAttacking() && !boss.hasShot()) {
            // ยิงกระสุนเพียงครั้งเดียวต่อการโจมตี
            float projectileX = boss.getX();
            float projectileY = boss.getY() + 25;
            float projectileSpeed = 5f;
            float projectileDx = -projectileSpeed; // Boss shoots towards player
            
            String projectileSprite;
            if (GameData.selectedBoss == 0) { // Teostra
                projectileSprite = "assets/sprites/Fireball.png";
            } else { // Velkhana
                projectileSprite = "assets/sprites/Iceball.png";
            }
            //Damage ของ บอส
            Projectile bossProjectile = new Projectile(projectileX, projectileY, projectileDx, 0, projectileSprite, 8, false);
            projectiles.add(bossProjectile);
            
            // เซ็ตว่าได้ยิงแล้วในการโจมตีนี้
            boss.setHasShot(true);
        }
    }
    
    private void updateProjectiles() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update();
            
            // Remove projectiles that are off-screen
            if (projectile.getX() < -50 || projectile.getX() > HunterArena.SCREEN_WIDTH + 50) {
                iterator.remove();
            }
        }
    }
    
    private void checkCollisions() {
        // Check player melee attacks against boss (Sword only)
        if (player.isAttacking() && GameData.selectedWeapon == 0) { // Sword
            Rectangle attackBounds = player.getAttackBounds();
            if (attackBounds != null && attackBounds.intersects(boss.getBounds())) {
                // ป้องกันการโจมตีซ้ำในการโจมตีเดียวกัน
                if (!player.hasShot()) {
                    boss.takeDamage(25); // เพิ่มความเสียหายของดาบ
                    player.setHasShot(true); // เซ็ตว่าได้โจมตีแล้วในการโจมตีนี้
                }
            }
        }
        
        // Check projectile collisions
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            
            if (projectile.isPlayerProjectile()) {
                // Player projectile hitting boss
                if (projectile.getBounds().intersects(boss.getBounds())) {
                    boss.takeDamage(projectile.getDamage());
                    iterator.remove();
                }
            } else {
                // Boss projectile hitting player
                if (projectile.getBounds().intersects(player.getBounds())) {
                    player.takeDamage(projectile.getDamage());
                    
                    // Apply status effects based on boss type
                    if (GameData.selectedBoss == 0) { // Teostra
                        player.applyBurnEffect();
                    } else { // Velkhana
                        player.applySlowEffect();
                    }
                    
                    iterator.remove();
                }
            }
        }
        
        // Check boss melee attacks against player
        if (boss.isAttacking()) {
            Rectangle bossAttackBounds = boss.getAttackBounds();
            if (bossAttackBounds != null && bossAttackBounds.intersects(player.getBounds())) {
                if (!boss.hasShot()) {
                    player.takeDamage(80);
                    boss.setHasShot(true);
                }
                
                // Apply status effects
                if (GameData.selectedBoss == 0) { // Teostra
                    player.applyBurnEffect();
                } else { // Velkhana
                    player.applySlowEffect();
                }
            }
        }
    }
    
    public void render(Graphics2D g2d) {
        if (!initialized) return;
        
        // Render player
        player.render(g2d);
        
        // Render boss
        boss.render(g2d);
        
        // Render projectiles
        for (Projectile projectile : projectiles) {
            projectile.render(g2d);
        }
        
        // Render UI
        gameUI.render(g2d, player, boss, gameTimer, maxGameTime);
    }
    
    public void keyPressed(int keyCode) {
        if (initialized && player != null) {
            player.keyPressed(keyCode);
        }
        
        // ESC to pause/return to menu (optional)
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gameState.setState(GameState.State.TITLE_SCREEN);
            initialized = false;
        }
    }
    
    public void keyReleased(int keyCode) {
        if (initialized && player != null) {
            player.keyReleased(keyCode);
        }
    }
}