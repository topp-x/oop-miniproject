import java.awt.*;

public abstract class Boss {
    protected float x, y;
    protected int hp;
    protected int maxHp;
    protected String name;
    protected Sprite currentSprite;
    protected Sprite sprite1, sprite2;
    protected boolean facingLeft;
    
    // AI and attack system
    protected int attackCooldown;
    protected int attackTimer;
    protected boolean isAttacking;
    protected boolean hasShot; // เพิ่มตัวแปรเพื่อควบคุมการยิงทีละลูก
    protected int movementTimer;
    protected float speed;
    
    // Animation
    protected int animationTimer;
    protected boolean animationFrame;
    
    public Boss(float x, float y, int maxHp, String name) {
        this.x = x;
        this.y = y;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.name = name;
        this.facingLeft = true;
        this.speed = 2.0f;
        this.attackCooldown = 0;
        this.attackTimer = 0;
        this.hasShot = false; // เริ่มต้นยังไม่ได้ยิง
        this.movementTimer = 0;
        this.animationTimer = 0;
        this.animationFrame = false;
        
        loadSprites();
    }
    
    protected abstract void loadSprites();
    
    public void update() {
        // Update timers
        if (attackCooldown > 0) attackCooldown--;
        if (attackTimer > 0) attackTimer--;
        movementTimer++;
        
        // Update animation
        updateAnimation();
        
        // AI behavior
        updateAI();
        
        // Update attack state
        if (isAttacking && attackTimer <= 0) {
            isAttacking = false;
        }
        
        // Keep boss within screen bounds
        constrainToScreen();
    }
    
    protected void updateAnimation() {
        animationTimer++;
        if (animationTimer >= 30) { // Change frame every 0.5 seconds
            animationFrame = !animationFrame;
            animationTimer = 0;
        }
        
        // Set current sprite based on animation frame
        currentSprite = animationFrame ? sprite1 : sprite2;
    }
    
    protected void updateAI() {
        // Simple AI: Move towards player and attack periodically
        if (attackCooldown <= 0) {
            attack();
            attackCooldown = 120; // 2 seconds at 60 FPS
        }
        
        // Simple movement pattern
        if (movementTimer % 180 == 0) { // Change direction every 3 seconds
            // Move up or down randomly
            float dy = (Math.random() > 0.5) ? speed : -speed;
            y += dy * 30; // Move 30 pixels
        }
    }
    
    protected void attack() {
        isAttacking = true;
        hasShot = false; // รีเซ็ตการยิงเมื่อเริ่มโจมตี
        attackTimer = 30; // Attack lasts 0.5 seconds
    }
    
    protected void constrainToScreen() {
        if (x < HunterArena.SCREEN_WIDTH / 2) x = HunterArena.SCREEN_WIDTH / 2;
        if (y < 0) y = 0;
        if (x > HunterArena.SCREEN_WIDTH - currentSprite.getWidth()) {
            x = HunterArena.SCREEN_WIDTH - currentSprite.getWidth();
        }
        if (y > HunterArena.SCREEN_HEIGHT - currentSprite.getHeight()) {
            y = HunterArena.SCREEN_HEIGHT - currentSprite.getHeight();
        }
    }
    
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }
    
    public void render(Graphics2D g2d) {
        currentSprite.setPosition((int)x, (int)y);
        currentSprite.render(g2d);
    }
    
    public Rectangle getBounds() {
        // ลด Hitbox ลงมาอีก - ตอนนี้เหลือ 50% ของขนาดเดิม (ลดจาก 75% เป็น 50%)
        int reducedWidth = currentSprite.getWidth() / 2;
        int reducedHeight = currentSprite.getHeight() / 2;
        int offsetX = (currentSprite.getWidth() - reducedWidth) / 2;
        int offsetY = (currentSprite.getHeight() - reducedHeight) / 2;
        
        return new Rectangle((int)x + offsetX, (int)y + offsetY, reducedWidth, reducedHeight);
    }
    
    public Rectangle getAttackBounds() {
        if (!isAttacking) return null;
        
        // Melee attack range to the left
        int attackRange = 80;
        return new Rectangle((int)x - attackRange, (int)y, attackRange, currentSprite.getHeight());
    }
    
    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public String getName() { return name; }
    public boolean isAttacking() { return isAttacking; }
    public boolean hasShot() { return hasShot; } // เพิ่มเมธอดเพื่อเช็คว่ายิงแล้วหรือยัง
    public void setHasShot(boolean hasShot) { this.hasShot = hasShot; } // เพิ่มเมธอดเพื่อเซ็ตสถานะการยิง
    public boolean isDead() { return hp <= 0; }
}