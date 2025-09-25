package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class DungeonPanel extends JLayeredPane { // Inherit from JLayeredPane
    private ImageIcon backgroundImage;
    private Image foregroundImage;
    public JButton door1Button;
    public JButton door2Button;
    private ImagePanel imagePanel; // Panel to draw images

    public DungeonPanel() {
        setBorder(BorderFactory.createTitledBorder("Dungeon Map"));
        setPreferredSize(new Dimension(400, 300));

        // --- Button Panel Setup ---
        // Create a panel for the buttons that will sit on top
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // 1 row, 2 columns
        buttonPanel.setOpaque(false); // Make it transparent

        door1Button = new JButton("Enter Left");
        door2Button = new JButton("Enter Right");

        styleDoorButton(door1Button);
        styleDoorButton(door2Button);

        buttonPanel.add(door1Button);
        buttonPanel.add(door2Button);

        // Add the button panel to the top layer
        add(buttonPanel, JLayeredPane.PALETTE_LAYER); // Use a higher layer for buttons

        // Create and add the image drawing panel to the bottom layer
        imagePanel = new ImagePanel();
        imagePanel.setOpaque(true); // Ensure it paints its background
        add(imagePanel, JLayeredPane.DEFAULT_LAYER); // Bottom layer for images

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
        // JLayeredPane's paintComponent handles painting its children.
        // We don't draw directly here anymore, the ImagePanel does it.
        super.paintComponent(g); // Call super to ensure JLayeredPane's own background is painted
    }

    @Override
    public void doLayout() {
        // Ensure all child components fill the entire JLayeredPane
        for (Component comp : getComponents()) {
            comp.setBounds(0, 0, getWidth(), getHeight());
        }
        super.doLayout(); // Call super's doLayout to handle any internal layout management
    }

    // Inner class to handle image drawing
    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Paint background of this panel
            // --- Image Drawing ---
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
    }

    public void displayImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Error: displayImage called with null or empty path.");
            this.foregroundImage = null;
            imagePanel.repaint(); // Repaint the image panel
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
        imagePanel.repaint(); // Repaint the image panel
    }

    public void clearImage() {
        this.foregroundImage = null;
        imagePanel.repaint(); // Repaint the image panel
    }
}
