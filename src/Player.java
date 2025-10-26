import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    private float x, y;
    private float speed;
    private int hp;
    private int maxHp;
    private int stamina;
    private int maxStamina;
    private boolean[] keys;
    
    // Dash system
    private boolean isDashing;
    private float dashDistance;
    private float dashSpeed;
    private float dashDx, dashDy;
    private int dashCooldown;
    
    // Attack system
    private boolean isAttacking;
    private int attackCooldown;
    private boolean hasShot; // เพิ่มตัวแปรเพื่อควบคุมการยิงธนูทีละลูก
    private boolean attackKeyPressed; // ตัวแปรเพื่อติดตามการกดปุ่มโจมตี
    private long lastAttackTime; // เวลาของการโจมตีครั้งล่าสุด
    private static final long ATTACK_DELAY = 500; // 0.5 วินาที = 500 มิลลิวินาที
    
    // Status effects
    private boolean isBurning;
    private int burnDuration;
    private int burnTimer;
    private boolean isSlowed;
    private int slowDuration;
    private int slowTimer;
    
    // Sprites
    private Sprite currentSprite;
    private Sprite runLeftSprite;
    private Sprite runRightSprite;
    private Sprite attackLeftSprite;
    private Sprite attackRightSprite;
    private boolean facingRight;
    
    // Animation
    private int animationTimer;
    private boolean animationFrame;
    
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.speed = 5.0f;
        this.maxHp = 100;
        this.hp = maxHp;
        this.maxStamina = 100;
        this.stamina = maxStamina;
        this.keys = new boolean[256];
        
        // Dash settings
        this.dashDistance = 100;
        this.dashSpeed = 15.0f;
        this.dashCooldown = 1;
        
        // Attack system
        this.attackCooldown = 0;
        this.hasShot = false; // เริ่มต้นยังไม่ได้ยิง
        this.attackKeyPressed = false; // เริ่มต้นยังไม่ได้กดปุ่มโจมตี
        this.lastAttackTime = 0; // เริ่มต้นเวลาการโจมตี
        
        // Status effects
        this.burnTimer = 0;
        this.slowTimer = 0;
        
        this.facingRight = true;
        this.animationTimer = 0;
        
        loadSprites();
    }
    
    private void loadSprites() {
        // Load sprites based on selected weapon
        if (GameData.selectedWeapon == 0) { // Sword
            runLeftSprite = new Sprite("assets/sprites/Warrior_RunLeft.png");
            runRightSprite = new Sprite("assets/sprites/Warrior_RunRight.png");
            attackLeftSprite = new Sprite("assets/sprites/Warrior_AttackLeft.png");
            attackRightSprite = new Sprite("assets/sprites/Warrior_AttackRight.png");
        } else { // Bow
            runLeftSprite = new Sprite("assets/sprites/Archer_RunLeft.png");
            runRightSprite = new Sprite("assets/sprites/Archer_RunRight.png");
            attackLeftSprite = new Sprite("assets/sprites/Archer_RunLeft.png");
            attackRightSprite = new Sprite("assets/sprites/Archer_RunRight.png");
        }
        
        currentSprite = runRightSprite;
    }
    
    public void update() {
        // Update timers
        if (dashCooldown > 0) dashCooldown--;
        if (attackCooldown > 0) attackCooldown--;
        
        // Stamina regeneration
        if (stamina < maxStamina && !isDashing && !isAttacking) {
            stamina += 2; // 2 stamina per frame (at 60 FPS = 2 per second)
            if (stamina > maxStamina) stamina = maxStamina;
        }
        
        // Handle status effects
        updateStatusEffects();
        
        // Handle dash
        if (isDashing) {
            updateDash();
        } else {
            // Normal movement
            updateMovement();
        }
        
        // Handle attack
        if (isAttacking && attackCooldown <= 0) {
            isAttacking = false;
        }
        
        // Update animation
        updateAnimation();
        
        // Keep player within screen bounds
        constrainToScreen();
    }
    
    private void updateMovement() {
        float currentSpeed = speed;
        if (isSlowed) {
            currentSpeed *= 0.5f; // 50% speed reduction
        }
        
        float dx = 0, dy = 0;
        
        if (keys[KeyEvent.VK_W]) dy -= currentSpeed;
        if (keys[KeyEvent.VK_S]) dy += currentSpeed;
        if (keys[KeyEvent.VK_A]) {
            dx -= currentSpeed;
            facingRight = false;
        }
        if (keys[KeyEvent.VK_D]) {
            dx += currentSpeed;
            facingRight = true;
        }
        
        // Normalize diagonal movement
        if (dx != 0 && dy != 0) {
            dx *= 0.707f; // 1/sqrt(2)
            dy *= 0.707f;
        }
        
        x += dx;
        y += dy;
    }
    
    private void updateDash() {
        x += dashDx;
        y += dashDy;
        
        dashDistance -= Math.abs(dashDx) + Math.abs(dashDy);
        
        if (dashDistance <= 0) {
            isDashing = false;
            dashCooldown = 120; // 2 second cooldown at 60 FPS
        }
    }
    
    private void updateStatusEffects() {
        // Burn effect
        if (isBurning) {
            burnTimer++;
            if (burnTimer >= 60) { // 1 second at 60 FPS
                hp -= 1;
                burnTimer = 0;
                burnDuration--;
                if (burnDuration <= 0) {
                    isBurning = false;
                }
            }
        }
        
        // Slow effect
        if (isSlowed) {
            slowTimer++;
            if (slowTimer >= 180) { // 3 seconds at 60 FPS
                isSlowed = false;
                slowTimer = 0;
            }
        }
    }
    
    private void updateAnimation() {
        animationTimer++;
        if (animationTimer >= 30) { // Change frame every 0.5 seconds
            animationFrame = !animationFrame;
            animationTimer = 0;
        }
        
        // Set current sprite based on state
        if (isAttacking) {
            currentSprite = facingRight ? attackRightSprite : attackLeftSprite;
        } else {
            currentSprite = facingRight ? runRightSprite : runLeftSprite;
        }
    }
    
    private void constrainToScreen() {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > HunterArena.SCREEN_WIDTH - currentSprite.getWidth()) {
            x = HunterArena.SCREEN_WIDTH - currentSprite.getWidth();
        }
        if (y > HunterArena.SCREEN_HEIGHT - currentSprite.getHeight()) {
            y = HunterArena.SCREEN_HEIGHT - currentSprite.getHeight();
        }
    }
    
    public void dash() {
        if (!isDashing && dashCooldown <= 0 && stamina >= 10) {
            isDashing = true;
            stamina -= 10;
            dashDistance = 100;
            
            // Calculate dash direction based on current movement
            float dx = 0, dy = 0;
            if (keys[KeyEvent.VK_W]) dy -= 1;
            if (keys[KeyEvent.VK_S]) dy += 1;
            if (keys[KeyEvent.VK_A]) dx -= 1;
            if (keys[KeyEvent.VK_D]) dx += 1;
            
            // If no movement keys pressed, dash in facing direction
            if (dx == 0 && dy == 0) {
                dx = facingRight ? 1 : -1;
            }
            
            // Normalize and apply dash speed
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            if (length > 0) {
                dashDx = (dx / length) * dashSpeed;
                dashDy = (dy / length) * dashSpeed;
            }
        }
    }
    
    public void attack() {
        long currentTime = System.currentTimeMillis();
        
        // ตรวจสอบว่าผ่าน delay แล้วหรือยัง และไม่อยู่ในสถานะโจมตี
        if (!isAttacking && attackCooldown <= 0 && stamina >= 5 && 
            (currentTime - lastAttackTime) >= ATTACK_DELAY) {
            isAttacking = true;
            hasShot = false; // รีเซ็ตการยิงเมื่อเริ่มโจมตีใหม่
            stamina -= 10;
            attackCooldown = 30; // 0.5 second cooldown
            lastAttackTime = currentTime; // บันทึกเวลาการโจมตี
        }
    }
    
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }
    
    public void applyBurnEffect() {
        isBurning = true;
        burnDuration = 5; // 5 seconds
        burnTimer = 0;
    }
    
    public void applySlowEffect() {
        isSlowed = true;
        slowTimer = 0;
    }
    
    public void render(Graphics2D g2d) {
        currentSprite.setPosition((int)x, (int)y);
        currentSprite.render(g2d);
        
        // Draw status effect indicators
        if (isBurning) {
            g2d.setColor(Color.RED);
            g2d.fillOval((int)x + currentSprite.getWidth() - 10, (int)y - 10, 10, 10);
        }
        
        if (isSlowed) {
            g2d.setColor(Color.CYAN);
            g2d.fillOval((int)x + currentSprite.getWidth() - 20, (int)y - 10, 10, 10);
        }
    }
    
    public Rectangle getAttackBounds() {
        if (!isAttacking) return null;
        
        if (GameData.selectedWeapon == 0) { // Sword - melee attack
            int attackRange = 50;
            if (facingRight) {
                return new Rectangle((int)x + currentSprite.getWidth(), (int)y, attackRange, currentSprite.getHeight());
            } else {
                return new Rectangle((int)x - attackRange, (int)y, attackRange, currentSprite.getHeight());
            }
        }
        
        return null; // Bow attacks are handled separately as projectiles
    }
    
    public Rectangle getBounds() {
        
        int reducedWidth = currentSprite.getWidth() / 2;
        int reducedHeight = currentSprite.getHeight() / 2;
        int offsetX = (currentSprite.getWidth() - reducedWidth) / 2;
        int offsetY = (currentSprite.getHeight() - reducedHeight) / 2;
        
        return new Rectangle((int)x + offsetX, (int)y + offsetY, reducedWidth, reducedHeight);
    }
    
    public void keyPressed(int keyCode) {
        if (keyCode < keys.length) {
            keys[keyCode] = true;
        }
        
        if (keyCode == KeyEvent.VK_SPACE) {
            dash();
        } else if (keyCode == KeyEvent.VK_J && !attackKeyPressed) {
            // ต้องกดปุ่มใหม่ทุกครั้ง (ไม่ให้กดค้าง)
            attackKeyPressed = true;
            attack();
        }
    }
    
    public void keyReleased(int keyCode) {
        if (keyCode < keys.length) {
            keys[keyCode] = false;
        }
        
        // รีเซ็ตสถานะการกดปุ่มโจมตีเมื่อปล่อยปุ่ม
        if (keyCode == KeyEvent.VK_J) {
            attackKeyPressed = false;
        }
    }
    
    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getStamina() { return stamina; }
    public int getMaxStamina() { return maxStamina; }
    public boolean isAttacking() { return isAttacking; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isDead() { return hp <= 0; }
    public boolean hasShot() { return hasShot; } // เพิ่มเมธอดเพื่อตรวจสอบว่าได้ยิงแล้วหรือยัง
    
    // Setter สำหรับ hasShot
    public void setHasShot(boolean hasShot) { this.hasShot = hasShot; }
}