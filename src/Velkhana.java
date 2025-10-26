public class Velkhana extends Boss {
    
    public Velkhana(float x, float y) {
        super(x, y, 250, "VELKHANA");
    }
    
    @Override
    protected void loadSprites() {
        sprite1 = new Sprite("assets/sprites/Velkhana-run1.png");
        sprite2 = new Sprite("assets/sprites/Velkhana-run2.png");
        currentSprite = sprite1;
    }
    
    @Override
    protected void updateAI() {
        // Velkhana-specific AI behavior
        if (attackCooldown <= 0) {
            attack();
            attackCooldown = 150; // 2.5 seconds at 60 FPS (less aggressive but more HP)
        }
        
        // More defensive movement pattern
        if (movementTimer % 240 == 0) { // Change direction every 4 seconds
            float dy = (Math.random() > 0.5) ? speed : -speed;
            y += dy * 40;
        }
    }
}