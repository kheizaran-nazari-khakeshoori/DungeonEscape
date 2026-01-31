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
//Extending JFrame lets us easily build and control our own application window in a structured, object-oriented way.
public class PlayerSelectionWindow extends JFrame {

    //frame
    public PlayerSelectionWindow() {
        setTitle("Choose Your Hero");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center the window on the screen 
        setLayout(new GridLayout(1, 3, 10, 10));//one row three columns the 10 pixlels of space between them 
        
        //using add to add component like the panels to the window 
        add(createCharacterPanel(
            "A rebellious princess from Dreamland, surprisingly tough and handy with a sword.",
            Bean::new
        ));


        add(createCharacterPanel(
            "An optimistic elf who left Elfwood for adventure. Not a fighter, but he tries.",
            Elfo::new
        ));

        
        add(createCharacterPanel(
            "Bean's personal demon. Fragile but powerful. Do it, do it, do it!",
            Lucy::new
        ));

      
        
    }

    //characters
    //So, the type is JPanel because the method’s purpose is to build and give you a ready-to-use JPanel.
    private JPanel createCharacterPanel(String description, Supplier<Player> playerSupplier) {
        // Create an instance to get name and image path (DRY principle - Single Source of Truth)
        Player player = playerSupplier.get();
        String name = player.getName();
        String imagePath = player.getImagePath();
        
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,3));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel;
        try {
        // Try to load the image from the resources folder
            java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);//resource locator 

           
                ImageIcon icon = new ImageIcon(imgURL);
            
                //These lines resize the character image and create a label to show it, centered in the panel.
                Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(image), SwingConstants.CENTER);
          
            
        }   
        catch (Exception e) 
        {
            imageLabel = new JLabel("[Error loading image]", SwingConstants.CENTER);
            //imageLabel.setForeground(Color.RED);
        }
        panel.add(imageLabel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea descArea = new JTextArea(description);
        //bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));

        //text 
        descArea.setWrapStyleWord(true);//If a word doesn’t fit at the end of a line, the whole word moves to the next line.
        descArea.setLineWrap(true);//enables line wrapping.
        descArea.setEditable(false);
        descArea.setForeground(Color.black);
        descArea.setOpaque(true);//background color
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