package view;
//class is a custom Swing panel that displays images in my GUI:
//is a reusable panel for displaying layered images in my game’s interface.
//Keeps the image-drawing logic separate from button and layout logic.
// Makes DungeonPanel cleaner: DungeonPanel manages layout and buttons, 
//while ImagePanel focuses purely on painting images.
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private ImageIcon backgroundImage;
    private Image foregroundImage;

    public void setBackgroundImage(ImageIcon backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint();
    }

    public void setForegroundImage(Image foregroundImage) {
        this.foregroundImage = foregroundImage;
        repaint();
    }



   @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g); // Always call this first to ensure the panel is cleared properly

    // 1 Draw the background image if it exists
    if (backgroundImage != null) {
        // Stretch the background to fill the whole panel
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    } else {
        // If no background is found, draw a simple placeholder so the panel doesn't look empty
        g.setColor(Color.DARK_GRAY);                  // Gray background
        g.fillRect(0, 0, getWidth(), getHeight());    // Fill entire panel
        g.setColor(Color.RED);                        // Red text color
        g.drawString("BACKGROUND IMAGE NOT FOUND", 20, getHeight() / 2); // Message in middle-ish
    }

    // 2 Draw the foreground image if one exists
    if (foregroundImage != null) {
        // Make the foreground slightly smaller than the panel
        int newWidth = getWidth() - 60;
        int newHeight = getHeight() - 60;

        // Center the image
        int x = (getWidth() - newWidth) / 2;
        int y = (getHeight() - newHeight) / 2;

        g.drawImage(foregroundImage, x, y, newWidth, newHeight, this);
    }
}

}