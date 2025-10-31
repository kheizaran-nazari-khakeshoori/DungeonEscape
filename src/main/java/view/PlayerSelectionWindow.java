package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.dungeonescape.ItemSelectionWindow;

import model.Bean;
import model.Elfo;
import model.Lucy;
import model.Player;

public class PlayerSelectionWindow extends JFrame {

    public PlayerSelectionWindow() {
        setTitle("Choose Your Hero");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center the window on the screen 
        setLayout(new GridLayout(1, 3, 10, 10));//one row three columns the 10 pixlels of space between them 
        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bean Panel
        add(createCharacterPanel(
            "Bean",
            "A rebellious princess from Dreamland, surprisingly tough and handy with a sword.",
            "images/players/Bean.png",
            Bean::new
        ));

        // Elfo Panel
        add(createCharacterPanel(
            "Elfo",
            "An optimistic elf who left Elfwood for adventure. Not a fighter, but he tries.",
            "images/players/Elfo.png",
            Elfo::new
        ));

        // Lucy Panel
        add(createCharacterPanel(
            "Lucy",
            "Bean's personal demon. Fragile but powerful. Do it, do it, do it!",
            "images/players/Lucy.png",
            Lucy::new
        ));
    }

    private JPanel createCharacterPanel(String name, String description, String imagePath, Supplier<Player> playerSupplier) {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel;
        try {
        // Try to load the image from the resources folder
            java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);

            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
            // Resize image to 128x128 pixels to make it fit nicely
                Image image = icon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(image), SwingConstants.CENTER);
            }    
            else
            {  
                // If the image isn't found, show a placeholder text instead
                imageLabel = new JLabel("[Image not found]", SwingConstants.CENTER);
            }
        }   
        catch (Exception e) 
        {
            imageLabel = new JLabel("[Error loading image]", SwingConstants.CENTER);
        }
        panel.add(imageLabel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea descArea = new JTextArea(description);

    
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton selectButton = new JButton("Select " + name);

        selectButton.addActionListener(e -> {
            Player selectedPlayer = playerSupplier.get();
            this.setVisible(false);
            new ItemSelectionWindow(selectedPlayer).setVisible(true);
            this.dispose();
        });

        bottomPanel.add(descArea, BorderLayout.CENTER);
        bottomPanel.add(selectButton, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
}