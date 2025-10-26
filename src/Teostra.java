public class Teostra extends Boss {
    
    public Teostra(float x, float y) {
        super(x, y, 200, "TEOSTRA");
    }
    
    @Override
    protected void loadSprites() {
        sprite1 = new Sprite("assets/sprites/Teostra-run1.png");
        sprite2 = new Sprite("assets/sprites/Teostra-run2.png");
        currentSprite = sprite1;
    }
    
    @Override
    protected void updateAI() {
        // Teostra-specific AI behavior
        if (attackCooldown <= 0) {
            attack();
            attackCooldown = 90; // 1.5 seconds at 60 FPS (more aggressive than base)
        }
        
        // More aggressive movement pattern
        if (movementTimer % 120 == 0) { // Change direction every 2 seconds
            float dy = (Math.random() > 0.5) ? speed * 1.5f : -speed * 1.5f;
            y += dy * 20;
        }
    }
}