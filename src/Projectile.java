import java.awt.*;

public class Projectile {
    private float x, y;
    private float dx, dy;
    private Sprite sprite;
    private int damage;
    private boolean isPlayerProjectile;
    private int maxDistance;
    private float traveledDistance;
    
    public Projectile(float x, float y, float dx, float dy, String spritePath, int damage, boolean isPlayerProjectile) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.sprite = new Sprite(spritePath);
        this.damage = damage;
        this.isPlayerProjectile = isPlayerProjectile;
        this.maxDistance = isPlayerProjectile ? 500 : 800; // Player arrows travel 500px, boss projectiles 800px
        this.traveledDistance = 0;
    }
    
    public void update() {
        // Move projectile
        x += dx;
        y += dy;
        
        // Track traveled distance
        traveledDistance += Math.abs(dx) + Math.abs(dy);
        
        // Update sprite position
        sprite.setPosition((int)x, (int)y);
    }
    
    public void render(Graphics2D g2d) {
        sprite.render(g2d);
    }
    
    public Rectangle getBounds() {
        return sprite.getBounds();
    }
    
    public boolean isOutOfRange() {
        return traveledDistance >= maxDistance;
    }
    
    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getDamage() { return damage; }
    public boolean isPlayerProjectile() { return isPlayerProjectile; }
}