import java.awt.*;
import java.awt.event.KeyEvent;

public class WeaponSelection {
    private GameState gameState;
    private int selectedWeapon; // 0 = Sword, 1 = Bow
    private Sprite swordSprite;
    private Sprite bowSprite;
    
    public WeaponSelection(GameState gameState) {
        this.gameState = gameState;
        this.selectedWeapon = 0;
        
        // Load weapon sprites for preview
        swordSprite = new Sprite("assets/sprites/Warrior_RunRight.png");
        bowSprite = new Sprite("assets/sprites/Archer_RunRight.png");
    }
    
    public void update() {
        // No continuous updates needed for weapon selection
    }
    
    public void render(Graphics2D g2d) {
        // Clear screen with dark overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, HunterArena.SCREEN_WIDTH, HunterArena.SCREEN_HEIGHT);
        
        // Title
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(36f));
        String title = "SELECT WEAPON";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 150);
        
        // Weapon options
        int weaponY = 300;
        int weaponSpacing = 300;
        
        // Sword option
        int swordX = HunterArena.SCREEN_WIDTH / 2 - weaponSpacing;
        if (selectedWeapon == 0) {
            g2d.setColor(Color.YELLOW);
            g2d.drawRect(swordX - 100, weaponY - 100, 200, 200);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(24f));
        String swordText = "SWORD";
        fm = g2d.getFontMetrics();
        g2d.drawString(swordText, swordX - fm.stringWidth(swordText) / 2, weaponY - 120);
        
        // Draw sword sprite
        swordSprite.setPosition(swordX - swordSprite.getWidth() / 2, weaponY - swordSprite.getHeight() / 2);
        swordSprite.render(g2d);
        
        // Sword stats
        g2d.setFont(g2d.getFont().deriveFont(16f));
        String[] swordStats = {
            "Melee Attack",
            "Damage: 15",
            "Range: 50px",
            "Stamina: 5"
        };
        int statY = weaponY + 80;
        for (String stat : swordStats) {
            fm = g2d.getFontMetrics();
            g2d.drawString(stat, swordX - fm.stringWidth(stat) / 2, statY);
            statY += 20;
        }
        
        // Bow option
        int bowX = HunterArena.SCREEN_WIDTH / 2 + weaponSpacing;
        if (selectedWeapon == 1) {
            g2d.setColor(Color.YELLOW);
            g2d.drawRect(bowX - 100, weaponY - 100, 200, 200);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(24f));
        String bowText = "BOW";
        fm = g2d.getFontMetrics();
        g2d.drawString(bowText, bowX - fm.stringWidth(bowText) / 2, weaponY - 120);
        
        // Draw bow sprite
        bowSprite.setPosition(bowX - bowSprite.getWidth() / 2, weaponY - bowSprite.getHeight() / 2);
        bowSprite.render(g2d);
        
        // Bow stats
        g2d.setFont(g2d.getFont().deriveFont(16f));
        String[] bowStats = {
            "Ranged Attack",
            "Damage: 10",
            "Range: 500px",
            "Stamina: 5"
        };
        statY = weaponY + 80;
        for (String stat : bowStats) {
            fm = g2d.getFontMetrics();
            g2d.drawString(stat, bowX - fm.stringWidth(stat) / 2, statY);
            statY += 20;
        }
        
        // Instructions
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(18f));
        String instructions = "A/D - Select    ENTER - Confirm    ESC - Back";
        fm = g2d.getFontMetrics();
        int instructX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(instructions)) / 2;
        g2d.drawString(instructions, instructX, HunterArena.SCREEN_HEIGHT - 50);
    }
    
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                selectedWeapon = 0;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                selectedWeapon = 1;
                break;
            case KeyEvent.VK_ENTER:
                // Store selected weapon and move to boss selection
                GameData.selectedWeapon = selectedWeapon;
                gameState.setState(GameState.State.BOSS_SELECTION);
                break;
            case KeyEvent.VK_ESCAPE:
                gameState.setState(GameState.State.TITLE_SCREEN);
                break;
        }
    }
    
    public void keyReleased(int keyCode) {
        // No action needed for key release
    }
}