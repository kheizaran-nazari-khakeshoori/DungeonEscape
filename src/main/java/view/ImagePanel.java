package view;
//class is a custom Swing panel that displays images in my GUI:
//is a reusable panel for displaying layered images in my game’s interface.
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
        super.paintComponent(g);

        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Draw a simple placeholder if no background is found
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.drawString("BACKGROUND IMAGE NOT FOUND", 20, getHeight() / 2);
        }

        // Draw foreground image (if one exists)
        if (foregroundImage != null) {
            int newWidth = getWidth() - 60;
            int newHeight = getHeight() - 60;

            // Center the image
            int x = (getWidth() - newWidth) / 2;
            int y = (getHeight() - newHeight) / 2;

            g.drawImage(foregroundImage, x, y, newWidth, newHeight, this);
        }
    }
}