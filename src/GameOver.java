import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOver {
    private GameState gameState;
    private int selectedOption; // 0 = Play Again, 1 = Main Menu
    
    public GameOver(GameState gameState) {
        this.gameState = gameState;
        this.selectedOption = 0;
    }
    
    public void update() {
        // No continuous updates needed for game over screen
    }
    
    public void render(Graphics2D g2d) {
        // Clear screen with dark overlay
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, HunterArena.SCREEN_WIDTH, HunterArena.SCREEN_HEIGHT);
        
        // Result title
        g2d.setFont(g2d.getFont().deriveFont(48f));
        String resultText;
        Color resultColor;
        
        if (GameData.gameWon) {
            resultText = "VICTORY!";
            resultColor = Color.GREEN;
        } else {
            resultText = "DEFEAT!";
            resultColor = Color.RED;
        }
        
        g2d.setColor(resultColor);
        FontMetrics fm = g2d.getFontMetrics();
        int resultX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(resultText)) / 2;
        g2d.drawString(resultText, resultX, 200);
        
        // Game stats
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(24f));
        
        String weaponUsed = GameData.selectedWeapon == 0 ? "SWORD" : "BOW";
        String bossName = GameData.selectedBoss == 0 ? "TEOSTRA" : "VELKHANA";
        
        String[] stats = {
            "Weapon Used: " + weaponUsed,
            "Boss: " + bossName
        };
        
        if (GameData.gameWon) {
            String timeText = "Time: " + GameData.gameTime + " seconds";
            stats = new String[]{
                "Weapon Used: " + weaponUsed,
                "Boss: " + bossName,
                timeText
            };
        }
        
        int statY = 300;
        for (String stat : stats) {
            fm = g2d.getFontMetrics();
            int statX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(stat)) / 2;
            g2d.drawString(stat, statX, statY);
            statY += 40;
        }
        
        // Menu options
        g2d.setFont(g2d.getFont().deriveFont(20f));
        String[] options = {"PLAY AGAIN", "MAIN MENU"};
        int optionY = 500;
        
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(Color.YELLOW);
                // Draw selection indicator
                fm = g2d.getFontMetrics();
                int optionX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(options[i])) / 2;
                g2d.drawString("> " + options[i] + " <", optionX - 30, optionY);
            } else {
                g2d.setColor(Color.WHITE);
                fm = g2d.getFontMetrics();
                int optionX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(options[i])) / 2;
                g2d.drawString(options[i], optionX, optionY);
            }
            optionY += 50;
        }
        
        // Instructions
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(16f));
        String instructions = "W/S - Select    ENTER - Confirm";
        fm = g2d.getFontMetrics();
        int instructX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(instructions)) / 2;
        g2d.drawString(instructions, instructX, HunterArena.SCREEN_HEIGHT - 50);
    }
    
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                selectedOption = Math.max(0, selectedOption - 1);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                selectedOption = Math.min(1, selectedOption + 1);
                break;
            case KeyEvent.VK_ENTER:
                if (selectedOption == 0) {
                    // Play again - go to weapon selection
                    GameData.reset();
                    gameState.setState(GameState.State.WEAPON_SELECTION);
                } else {
                    // Main menu
                    GameData.reset();
                    gameState.setState(GameState.State.TITLE_SCREEN);
                }
                break;
        }
    }
    
    public void keyReleased(int keyCode) {
        // No action needed for key release
    }
}