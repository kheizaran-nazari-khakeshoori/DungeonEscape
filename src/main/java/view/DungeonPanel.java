package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

// DungeonPanel displays the dungeon image with two doors ("Enter Left" and "Enter Right").
// It also supports showing another image on top of the background.

public final class DungeonPanel extends JLayeredPane {
    private final ImagePanel imagePanel;    // Panel that actually draws the images
    public JButton door1Button;
    public JButton door2Button;

    public DungeonPanel() {
        // Set up the basic panel look
        setBorder(BorderFactory.createTitledBorder("Dungeon Map"));
        setPreferredSize(new Dimension(400, 300));

        // 1. Create the image-drawing panel (bottom layer)
        imagePanel = new ImagePanel();
        imagePanel.setOpaque(true);
        add(imagePanel, JLayeredPane.DEFAULT_LAYER);

        // 2. Create the door buttons (top layer)
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

        // 3. Load the background image 
        loadBackgroundImage("images/ui/TwoDoors.png");
    }

    // Loads the background image for the dungeon.
    public void loadBackgroundImage(String imagePath) {
        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);

        if (imageURL != null) {
            imagePanel.setBackgroundImage(new ImageIcon(imageURL));
        } else {
            System.err.println(" Could not find background image: " + imagePath);
            imagePanel.setBackgroundImage(null);
        }
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
            imagePanel.setForegroundImage(null);
            return;
        }

        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);

        if (imageURL != null) {
            imagePanel.setForegroundImage(new ImageIcon(imageURL).getImage());
        } else {
            System.err.println(" Could not find overlay image: " + imagePath);
            imagePanel.setForegroundImage(null);
        }
    }

    /**
     * Removes any overlay image.
     */
    public void clearImage() {
        imagePanel.setForegroundImage(null);
    }
}