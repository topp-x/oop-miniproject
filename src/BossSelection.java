import java.awt.*;
import java.awt.event.KeyEvent;

public class BossSelection {
    private GameState gameState;
    private int selectedBoss; // 0 = Teostra, 1 = Velkhana
    private Sprite teostraSprite;
    private Sprite velkhanaSprite;
    
    public BossSelection(GameState gameState) {
        this.gameState = gameState;
        this.selectedBoss = 0;
        
        // Load boss sprites for preview
        teostraSprite = new Sprite("assets/sprites/Teostra-run1.png");
        velkhanaSprite = new Sprite("assets/sprites/Velkhana-run1.png");
    }
    
    public void update() {
        // No continuous updates needed for boss selection
    }
    
    public void render(Graphics2D g2d) {
        // Clear screen with dark overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, HunterArena.SCREEN_WIDTH, HunterArena.SCREEN_HEIGHT);
        
        // Title
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(36f));
        String title = "SELECT BOSS";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 150);
        
        // Boss options
        int bossY = 300;
        int bossSpacing = 300;
        
        // Teostra option
        int teostraX = HunterArena.SCREEN_WIDTH / 2 - bossSpacing;
        if (selectedBoss == 0) {
            g2d.setColor(Color.RED);
            g2d.drawRect(teostraX - 100, bossY - 100, 200, 200);
        }
        
        g2d.setColor(Color.RED);
        g2d.setFont(g2d.getFont().deriveFont(24f));
        String teostraText = "TEOSTRA";
        fm = g2d.getFontMetrics();
        g2d.drawString(teostraText, teostraX - fm.stringWidth(teostraText) / 2, bossY - 120);
        
        // Draw Teostra sprite
        teostraSprite.setPosition(teostraX - teostraSprite.getWidth() / 2, bossY - teostraSprite.getHeight() / 2);
        teostraSprite.render(g2d);
        
        // Teostra stats
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(16f));
        String[] teostraStats = {
            "Fire Element",
            "HP: 200",
            "Burn Effect:",
            "1 HP/sec for 5s"
        };
        int statY = bossY + 80;
        for (String stat : teostraStats) {
            fm = g2d.getFontMetrics();
            g2d.drawString(stat, teostraX - fm.stringWidth(stat) / 2, statY);
            statY += 20;
        }
        
        // Velkhana option
        int velkhanaX = HunterArena.SCREEN_WIDTH / 2 + bossSpacing;
        if (selectedBoss == 1) {
            g2d.setColor(Color.CYAN);
            g2d.drawRect(velkhanaX - 100, bossY - 100, 200, 200);
        }
        
        g2d.setColor(Color.CYAN);
        g2d.setFont(g2d.getFont().deriveFont(24f));
        String velkhanaText = "VELKHANA";
        fm = g2d.getFontMetrics();
        g2d.drawString(velkhanaText, velkhanaX - fm.stringWidth(velkhanaText) / 2, bossY - 120);
        
        // Draw Velkhana sprite
        velkhanaSprite.setPosition(velkhanaX - velkhanaSprite.getWidth() / 2, bossY - velkhanaSprite.getHeight() / 2);
        velkhanaSprite.render(g2d);
        
        // Velkhana stats
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(16f));
        String[] velkhanaStats = {
            "Ice Element",
            "HP: 250",
            "Slow Effect:",
            "50% speed for 3s"
        };
        statY = bossY + 80;
        for (String stat : velkhanaStats) {
            fm = g2d.getFontMetrics();
            g2d.drawString(stat, velkhanaX - fm.stringWidth(stat) / 2, statY);
            statY += 20;
        }
        
        // Instructions
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(18f));
        String instructions = "A/D - Select    ENTER - Start Battle    ESC - Back";
        fm = g2d.getFontMetrics();
        int instructX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(instructions)) / 2;
        g2d.drawString(instructions, instructX, HunterArena.SCREEN_HEIGHT - 50);
    }
    
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                selectedBoss = 0;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                selectedBoss = 1;
                break;
            case KeyEvent.VK_ENTER:
                // Store selected boss and start the game
                GameData.selectedBoss = selectedBoss;
                gameState.setState(GameState.State.PLAYING);
                break;
            case KeyEvent.VK_ESCAPE:
                gameState.setState(GameState.State.WEAPON_SELECTION);
                break;
        }
    }
    
    public void keyReleased(int keyCode) {
        // No action needed for key release
    }
}