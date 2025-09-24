package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

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
        door1Button.setBounds(50, 60, 120, 180);
        door2Button.setBounds(230, 60, 120, 180);

        // Make buttons transparent to show the door image underneath
        styleDoorButton(door1Button);
        styleDoorButton(door2Button);

        add(door1Button);
        add(door2Button);

        // Load the default dungeon image
        loadBackgroundImage("images/ui/TwoDoors.png");
    }

    public void loadBackgroundImage(String imagePath) {
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
            // Calculate the scaled dimensions while maintaining aspect ratio.
            // This approach is better than getScaledInstance() as it's synchronous.
            int padding = 30;
            int maxWidth = getWidth() - padding;
            int maxHeight = getHeight() - padding;

            int originalWidth = foregroundImage.getWidth(this);
            int originalHeight = foregroundImage.getHeight(this);

            if (originalWidth > 0 && originalHeight > 0 && maxWidth > 0 && maxHeight > 0) {
                double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
                int newWidth = (int) (originalWidth * scale);
                int newHeight = (int) (originalHeight * scale);

                // Calculate centered position
                int x = (getWidth() - newWidth) / 2;
                int y = (getHeight() - newHeight) / 2;

                // Draw the image, letting drawImage handle the scaling and observing
                g.drawImage(foregroundImage, x, y, newWidth, newHeight, this);
            }
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

    public void clearImage() {
        this.foregroundImage = null;
        repaint();
    }
}
