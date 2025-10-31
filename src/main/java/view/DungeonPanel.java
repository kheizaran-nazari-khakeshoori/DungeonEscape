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

/**
 * DungeonPanel displays the dungeon image with two doors ("Enter Left" and "Enter Right").
 * It also supports showing another image on top of the background.
 */
public final class DungeonPanel extends JLayeredPane {
    private ImageIcon backgroundImage;  // Background dungeon image
    private Image foregroundImage;      // Optional overlay image
    private final JPanel imagePanel;    // Panel that actually draws the images
    public JButton door1Button;
    public JButton door2Button;

    public DungeonPanel() {
        // Set up the basic panel look
        setBorder(BorderFactory.createTitledBorder("Dungeon Map"));
        setPreferredSize(new Dimension(400, 300));

        // --- 1. Create the image-drawing panel (bottom layer) ---
        imagePanel = new ImagePanel();
        imagePanel.setOpaque(true);
        add(imagePanel, JLayeredPane.DEFAULT_LAYER);

        // --- 2. Create the door buttons (top layer) ---
        door1Button = new JButton("Enter Left");
        door2Button = new JButton("Enter Right");

        // Style the buttons to look clean
        styleDoorButton(door1Button);
        styleDoorButton(door2Button);

        // Put both buttons in a transparent panel side-by-side
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setOpaque(false);
        buttonPanel.add(door1Button);
        buttonPanel.add(door2Button);

        // Add that panel to the top (visible) layer
        add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        // --- 3. Load the background image ---
        loadBackgroundImage("images/ui/TwoDoors.png");
    }

    /**
     * Loads the background image for the dungeon.
     */
    public void loadBackgroundImage(String imagePath) {
        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);

        if (imageURL != null) {
            backgroundImage = new ImageIcon(imageURL);
        } else {
            System.err.println("❌ Could not find background image: " + imagePath);
            backgroundImage = null;
        }

        repaint(); // Refresh display
    }

    /**
     * Adds a nice visual style to the door buttons.
     */
    private void styleDoorButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Makes sure all child components (buttons and image) fill the window.
     */
    @Override
    public void doLayout() {
        for (Component comp : getComponents()) {
            comp.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Displays an overlay (foreground) image on top of the background.
     */
    public void displayImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            foregroundImage = null;
            imagePanel.repaint();
            return;
        }

        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);

        if (imageURL != null) {
            foregroundImage = new ImageIcon(imageURL).getImage();
        } else {
            System.err.println("❌ Could not find overlay image: " + imagePath);
            foregroundImage = null;
        }

        imagePanel.repaint();
    }

    /**
     * Removes any overlay image.
     */
    public void clearImage() {
        foregroundImage = null;
        imagePanel.repaint();
    }

    // --- Inner class responsible for drawing images ---
    private class ImagePanel extends JPanel {
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
}
