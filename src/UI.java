import java.awt.*;

public class UI {
    
    public UI() {
        // Constructor
    }
    
    public void render(Graphics2D g2d, Player player, Boss boss, int gameTimer, int maxGameTime) {
        // Set font
        g2d.setFont(g2d.getFont().deriveFont(16f));
        
        // Draw player HP and Stamina (top left)
        drawPlayerStats(g2d, player);
        
        // Draw boss info (top right)
        drawBossInfo(g2d, boss);
        
        // Draw timer (bottom center)
        drawTimer(g2d, gameTimer, maxGameTime);
    }
    
    private void drawPlayerStats(Graphics2D g2d, Player player) {
        int x = 20;
        int y = 30;
        int barWidth = 200;
        int barHeight = 20;
        
        // Player HP Bar
        g2d.setColor(Color.WHITE);
        g2d.drawString("HP", x, y - 5);
        
        // HP Bar background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, barWidth, barHeight);
        
        // HP Bar foreground
        g2d.setColor(Color.RED);
        int hpWidth = (int)((float)player.getHp() / player.getMaxHp() * barWidth);
        g2d.fillRect(x, y, hpWidth, barHeight);
        
        // HP Bar border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, barWidth, barHeight);
        
        // HP Text
        g2d.setColor(Color.WHITE);
        String hpText = player.getHp() + "/" + player.getMaxHp();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (barWidth - fm.stringWidth(hpText)) / 2;
        g2d.drawString(hpText, textX, y + 15);
        
        // Player Stamina Bar
        y += 40;
        g2d.setColor(Color.WHITE);
        g2d.drawString("STAMINA", x, y - 5);
        
        // Stamina Bar background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, barWidth, barHeight);
        
        // Stamina Bar foreground
        g2d.setColor(Color.CYAN);
        int staminaWidth = (int)((float)player.getStamina() / player.getMaxStamina() * barWidth);
        g2d.fillRect(x, y, staminaWidth, barHeight);
        
        // Stamina Bar border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, barWidth, barHeight);
        
        // Stamina Text
        g2d.setColor(Color.WHITE);
        String staminaText = player.getStamina() + "/" + player.getMaxStamina();
        textX = x + (barWidth - fm.stringWidth(staminaText)) / 2;
        g2d.drawString(staminaText, textX, y + 15);
    }
    
    private void drawBossInfo(Graphics2D g2d, Boss boss) {
        int x = HunterArena.SCREEN_WIDTH - 220;
        int y = 30;
        int barWidth = 200;
        int barHeight = 20;
        
        // Boss Name
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(18f));
        String bossName = boss.getName();
        FontMetrics fm = g2d.getFontMetrics();
        int nameX = x + (barWidth - fm.stringWidth(bossName)) / 2;
        g2d.drawString(bossName, nameX, y - 10);
        
        // Boss HP Bar
        g2d.setFont(g2d.getFont().deriveFont(16f));
        
        // HP Bar background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, barWidth, barHeight);
        
        // HP Bar foreground (color based on boss type)
        if (GameData.selectedBoss == 0) { // Teostra
            g2d.setColor(Color.ORANGE);
        } else { // Velkhana
            g2d.setColor(Color.CYAN);
        }
        int bossHpWidth = (int)((float)boss.getHp() / boss.getMaxHp() * barWidth);
        g2d.fillRect(x, y, bossHpWidth, barHeight);
        
        // HP Bar border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, barWidth, barHeight);
        
        // HP Text
        g2d.setColor(Color.WHITE);
        String hpText = boss.getHp() + "/" + boss.getMaxHp();
        fm = g2d.getFontMetrics();
        int textX = x + (barWidth - fm.stringWidth(hpText)) / 2;
        g2d.drawString(hpText, textX, y + 15);
    }
    
    private void drawTimer(Graphics2D g2d, int gameTimer, int maxGameTime) {
        // Calculate remaining time in seconds
        int remainingSeconds = gameTimer / 60;
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        
        // Format time string
        String timeString = String.format("%d:%02d", minutes, seconds);
        
        // Set font and color
        g2d.setFont(g2d.getFont().deriveFont(24f));
        Color timerColor = Color.WHITE;
        
        // Change color when time is running low
        if (remainingSeconds <= 10) {
            timerColor = Color.RED;
        } else if (remainingSeconds <= 30) {
            timerColor = Color.YELLOW;
        }
        
        g2d.setColor(timerColor);
        
        // Center the timer at bottom of screen
        FontMetrics fm = g2d.getFontMetrics();
        int timerX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(timeString)) / 2;
        int timerY = HunterArena.SCREEN_HEIGHT - 50;
        
        g2d.drawString(timeString, timerX, timerY);
        
        // Draw "TIME" label above timer
        g2d.setFont(g2d.getFont().deriveFont(16f));
        g2d.setColor(Color.LIGHT_GRAY);
        String timeLabel = "TIME";
        fm = g2d.getFontMetrics();
        int labelX = (HunterArena.SCREEN_WIDTH - fm.stringWidth(timeLabel)) / 2;
        g2d.drawString(timeLabel, labelX, timerY - 30);
    }
}