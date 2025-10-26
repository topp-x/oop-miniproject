import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class HunterArena extends JPanel implements KeyListener, Runnable {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
    private Thread gameThread;
    private boolean running = false;
    private GameState gameState;
    private BufferedImage backgroundImage;
    private Font gameFont;
    
    public HunterArena() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        
        loadAssets();
        gameState = new GameState();
    }
    
    private void loadAssets() {
        try {
            // Load background image
            backgroundImage = ImageIO.read(new File("../assets/sprites/game_background.png"));
            
            // Load font
            gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("../assets/PressStart2P.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(gameFont);
            gameFont = gameFont.deriveFont(16f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Fallback font
            gameFont = new Font("Monospaced", Font.BOLD, 16);
        }
    }
    
    public void startGame() {
        gameThread = new Thread(this);
        running = true;
        gameThread.start();
    }
    
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    
    private void update() {
        gameState.update();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw background
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }
        
        // Set font
        g2d.setFont(gameFont);
        
        // Render based on game state
        gameState.render(g2d);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        gameState.keyPressed(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        gameState.keyReleased(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hunter Arena");
        HunterArena game = new HunterArena();
        
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        game.startGame();
    }
}