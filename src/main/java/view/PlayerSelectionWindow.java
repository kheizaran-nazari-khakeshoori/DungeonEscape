package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

import model.Player;
import model.Bean;
import model.Elfo;
import model.Lucy;

public class PlayerSelectionWindow extends JFrame {

    public PlayerSelectionWindow() {
        setTitle("Choose Your Hero");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 3, 10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        ImageIcon icon = null;
        java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
            Image image = icon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
        }
        JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);

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
            this.dispose();
            new GameWindow(selectedPlayer).setVisible(true);
        });

        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(descArea, BorderLayout.CENTER);
        southPanel.add(selectButton, BorderLayout.SOUTH);
        
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }
}