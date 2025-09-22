package view;

import javax.swing.*;
import java.awt.*;

public class DungeonPanel extends JPanel {
    private ImageIcon backgroundImage;
    private Image foregroundImage;
    public JButton door1Button;
    public JButton door2Button;

    public DungeonPanel() {
        setLayout(null); // Use null layout for absolute positioning
        setBorder(BorderFactory.createTitledBorder("Dungeon Map"));
        setPreferredSize(new Dimension(400, 300));

        // Create door buttons
        door1Button = new JButton("Enter Left");
        door2Button = new JButton("Enter Right");

        // Position the buttons over the doors (adjust coordinates as needed)
        door1Button.setBounds(50, 90, 120, 180);
        door2Button.setBounds(230, 90, 120, 180);

        // Make buttons transparent to show the door image underneath
        styleDoorButton(door1Button);
        styleDoorButton(door2Button);

        add(door1Button);
        add(door2Button);

        // Load the default dungeon image
        loadBackgroundImage("images/ui/TwoDoors.png");
    }

    private void loadBackgroundImage(String imagePath) {
        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);
        if (imageURL != null) {
            this.backgroundImage = new ImageIcon(imageURL);
        } else {
            System.err.println("CRITICAL ERROR: Could not find background image resource: " + imagePath);
            this.backgroundImage = null;
        }
        repaint(); // Redraw the panel with the new background
    }

    private void styleDoorButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image, scaled to fill the entire panel
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // If background is missing, draw a prominent error message
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("BACKGROUND IMAGE NOT FOUND", getWidth() / 2 - 120, getHeight() / 2);
        }
        if (foregroundImage != null) {
            // Draw the foreground image, scaled and centered
            Image scaledImage = getScaledImage(foregroundImage);
            int x = (getWidth() - scaledImage.getWidth(null)) / 2;
            int y = (getHeight() - scaledImage.getHeight(null)) / 2;
            g.drawImage(scaledImage, x, y, null);
        }
    }

    public void displayImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Error: displayImage called with null or empty path.");
            this.foregroundImage = null;
            repaint();
            return;
        }
        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);
        if (imageURL != null) {
            System.out.println("Successfully found image resource: " + imageURL.getPath());
            this.foregroundImage = new ImageIcon(imageURL).getImage();
        } else {
            // If an image is not found, log an error and show nothing.
            System.err.println("Error: Could not find image resource at " + imagePath);
            this.foregroundImage = null;
        }
        repaint();
    }

    private Image getScaledImage(Image srcImg) {
        int padding = 30; // Add some space around the image
        int maxWidth = Math.max(0, getWidth() - padding);
        int maxHeight = Math.max(0, getHeight() - padding);

        int originalWidth = srcImg.getWidth(null);
        int originalHeight = srcImg.getHeight(null);

        if (originalWidth <= 0 || originalHeight <= 0 || maxWidth <= 0 || maxHeight <= 0) {
            return srcImg; // Cannot scale
        }

        double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        return srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    public void clearImage() {
        this.foregroundImage = null;
        repaint();
    }
}
