package view;
//this class create graphical window that lets the player choose their character 
import java.awt.BorderLayout;
import java.awt.Color;
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

import model.Bean;
import model.Elfo;
import model.Lucy;
import model.Player;

public class PlayerSelectionWindow extends JFrame {// extending the Jfram to directly represent a window in GUI 

    //frame
    public PlayerSelectionWindow() {
        setTitle("Choose Your Hero");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center the window on the screen 
        setLayout(new GridLayout(1, 3, 10, 10));//one row three columns the 10 pixlels of space between them 
        
        //using add to add component like the panels to the window 
        add(createCharacterPanel(
            "Bean",
            "A rebellious princess from Dreamland, surprisingly tough and handy with a sword.",
            "images/players/Bean.png",
            Bean::new
        ));

       
        add(createCharacterPanel(
            "Elfo",
            "An optimistic elf who left Elfwood for adventure. Not a fighter, but he tries.",
            "images/players/Elfo.png",
            Elfo::new
        ));

        
        add(createCharacterPanel(
            "Lucy",
            "Bean's personal demon. Fragile but powerful. Do it, do it, do it!",
            "images/players/Lucy.png",
            Lucy::new
        ));
    }

    //characters
    //So, the type is JPanel because the method’s purpose is to build and give you a ready-to-use JPanel.
    private JPanel createCharacterPanel(String name, String description, String imagePath, Supplier<Player> playerSupplier) {
        JPanel panel = new JPanel(new BorderLayout(5, 10));//You are saying: “This panel will use a BorderLayout to arrange its components.”
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY,3));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel;
        try {
        // Try to load the image from the resources folder
            java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);

            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
            
                Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
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

        //text 
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setForeground(Color.black);
        descArea.setOpaque(true);//backgrounf color
        descArea.setFont(new Font("MV Boli", Font.PLAIN, 20));
        descArea.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        JButton selectButton = new JButton("Select " + name);

        //when the selection button is clicked 
        selectButton.addActionListener(e -> {
            Player selectedPlayer = playerSupplier.get();
            this.setVisible(false);
            new ItemSelectionWindow(selectedPlayer).setVisible(true);
            this.dispose(); // This closes and destroys the current PlayerSelectionWindow so it is removed from memory and the user only sees the next window.
        });

        bottomPanel.add(descArea, BorderLayout.CENTER);
        bottomPanel.add(selectButton, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
}