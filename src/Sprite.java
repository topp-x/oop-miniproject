import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {
    private BufferedImage image;
    private int x, y;
    private int width, height;
    private boolean visible;
    
    public Sprite(String imagePath) {
        loadImage(imagePath);
        this.x = 0;
        this.y = 0;
        this.visible = true;
    }
    
    public Sprite(String imagePath, int x, int y) {
        loadImage(imagePath);
        this.x = x;
        this.y = y;
        this.visible = true;
    }
    
    private void loadImage(String imagePath) {
        try {
            image = ImageIO.read(new File("../" + imagePath));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            // Create a placeholder rectangle if image fails to load
            image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.MAGENTA);
            g.fillRect(0, 0, 50, 50);
            g.dispose();
            width = 50;
            height = 50;
        }
    }
    
    public void render(Graphics2D g2d) {
        if (visible && image != null) {
            g2d.drawImage(image, x, y, null);
        }
    }
    
    public void render(Graphics2D g2d, int renderWidth, int renderHeight) {
        if (visible && image != null) {
            g2d.drawImage(image, x, y, renderWidth, renderHeight, null);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean intersects(Sprite other) {
        return this.getBounds().intersects(other.getBounds());
    }
    
    public boolean intersects(Rectangle rect) {
        return this.getBounds().intersects(rect);
    }
    
    // Getters and Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    
    public BufferedImage getImage() { return image; }
    
    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }
}