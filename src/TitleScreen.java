import java.awt.*;
import java.awt.event.KeyEvent;

public class TitleScreen {
    private GameState gameState;
    private boolean blinkText;
    private int blinkTimer;
    
    public TitleScreen(GameState gameState) {
        this.gameState = gameState;
        this.blinkText = true;
        this.blinkTimer = 0;
    }
    
    public void update() {
        // Blink effect for "Press Start" text
        blinkTimer++;
        if (blinkTimer >= 30) { // Blink every 30 frames (0.5 seconds at 60 FPS)
            blinkText = !blinkText;
            blinkTimer = 0;
        }
    }
    
    public void render(Graphics2D g2d) {
        // Clear screen with dark overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, HunterArena.SCREEN_WIDTH, HunterArena.SCREEN_HEIGHT);
        
        // Game title
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(48f));
        String title = "HUNTER ARENA";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(title)) / 2;
        int titleY = HunterArena.SCREEN_HEIGHT / 2 - 100;
        g2d.drawString(title, titleX, titleY);
        
        // Press Start text with blink effect
        if (blinkText) {
            g2d.setFont(g2d.getFont().deriveFont(24f));
            String pressStart = "PRESS ENTER TO START";
            fm = g2d.getFontMetrics();
            int startX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(pressStart)) / 2;
            int startY = HunterArena.SCREEN_HEIGHT / 2 + 50;
            g2d.drawString(pressStart, startX, startY);
        }
        
        // Instructions
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(16f));
        String[] instructions = {
            "CONTROLS:",
            "WASD - Move",
            "SPACEBAR - Dash",
            "J - Attack"
        };
        
        int instructY = HunterArena.SCREEN_HEIGHT - 150;
        for (String instruction : instructions) {
            fm = g2d.getFontMetrics();
            int instructX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(instruction)) / 2;
            g2d.drawString(instruction, instructX, instructY);
            instructY += 25;
        }
    }
    
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gameState.setState(GameState.State.WEAPON_SELECTION);
        }
    }
    
    public void keyReleased(int keyCode) {
        // No action needed for key release on title screen
    }
}