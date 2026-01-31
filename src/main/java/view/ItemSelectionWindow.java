package view;
//the item selection provide a window of weapons and potion a player can choose 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import model.Item;
import model.ItemFactory;
import model.Player;
import model.Potion;
import model.Weapon;

public class ItemSelectionWindow extends JFrame {
    
    private final Player selectedPlayer;
    private Weapon selectedWeapon;
    private Potion selectedPotion;

    public ItemSelectionWindow(Player player) {
        this.selectedPlayer = player;
        //sart with creating main frame 
        setTitle("Choose Your Starting accessories ");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center the window on the screen 
        setLayout(new BorderLayout(10, 10));
       
        //  creating containers for both weapons and potions
        JPanel weaponPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        JPanel potionPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        

        // WEAPON SELECTION
        List<Weapon> availableWeapons = ItemFactory.createStartingWeapons(); 
        
        // IMPROVEMENT: Add color to the border title
        TitledBorder weaponBorder = BorderFactory.createTitledBorder("Choose Your Weapon");
        weaponBorder.setTitleColor(new Color(0, 70, 200)); // A strong blue color
        weaponBorder.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        weaponPanel.setBorder(weaponBorder);
        
        ButtonGroup weaponGroup = new ButtonGroup(); 
        for (Weapon weapon : availableWeapons) {
            JPanel itemCard = createItemCard(weapon, weaponGroup);
            weaponPanel.add(itemCard);
        }

        // POTION SELECTION 
        List<Potion> availablePotions = ItemFactory.createStartingPotions();
        
        // IMPROVEMENT: Add color to the border title 
        TitledBorder potionBorder = BorderFactory.createTitledBorder("Choose Your Potion");
        potionBorder.setTitleColor(new Color(120, 0, 180)); // A magical purple color
        potionBorder.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        potionPanel.setBorder(potionBorder);

        ButtonGroup potionGroup = new ButtonGroup();// ButtonGroup - NOT a button! It's an invisible manager
        for (Potion potion : availablePotions) {
            JPanel itemCard = createItemCard(potion, potionGroup);
            potionPanel.add(itemCard);
        }

        
        // START BUTTON
        JButton startButton = new JButton("Begin Adventure!");
        startButton.setFont(new Font("Serif", Font.BOLD, 18));
        startButton.addActionListener(e -> startGame());//when the button is clicked the code inside the lambda will be excuted

        // Set default weapon  and potion selection
        if (!availableWeapons.isEmpty()) {
            selectedWeapon = availableWeapons.get(0);
            JPanel firstWeaponPanel = (JPanel) weaponPanel.getComponent(0);//getting first weapon card panel 
            JRadioButton firstWeaponButton = (JRadioButton) firstWeaponPanel.getComponent(0);//looking inside the first weapon panel and gets its first component
            firstWeaponButton.setSelected(true);
        //do not be confused >> the program must select first wepon adn potion by default so i find the radio button for the first weapon so program can select it by default 
        }
        if (!availablePotions.isEmpty()) {
            selectedPotion = availablePotions.get(0);
            JPanel firstPotionPanel = (JPanel) potionPanel.getComponent(0);
            JRadioButton firstPotionButton = (JRadioButton) firstPotionPanel.getComponent(0);
            firstPotionButton.setSelected(true);
        }

        // Add panels to frame
        JPanel selectionContainer = new JPanel(new GridLayout(2, 1, 10, 10));//rows , number of the column , horizontal and vertical gap 
        selectionContainer.add(weaponPanel);
        selectionContainer.add(potionPanel);
       

        add(selectionContainer, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }

    //creating card for each weapon and potion 
    private JPanel createItemCard(Item item, ButtonGroup group) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEtchedBorder());//just for 3d feature 
        //card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JRadioButton selectRadio = new JRadioButton(item.getName());//used for selecting one option from a group
        selectRadio.setFont(new Font("Serif", Font.BOLD, 16));
        selectRadio.setHorizontalAlignment(SwingConstants.CENTER);
        group.add(selectRadio);

        //This code ensures that when the user selects a weapon or potion, the corresponding variable (selectedWeapon or selectedPotion) is updated to match their choice.
        selectRadio.addActionListener(e -> {
            boolean isWeapon = item instanceof Weapon;
            if (isWeapon) selectedWeapon = (Weapon) item;
            else selectedPotion = (Potion) item;
        });

        JLabel imageLabel;
        java.net.URL imgURL = getClass().getClassLoader().getResource(item.getImagePath());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);//resizing the image
            imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        } else {
            System.err.println("Warning: Could not find image resource at " + item.getImagePath());
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(160, 160));
        }

        // Use the new description field from the Item class, plus the stats.
        String descriptionText = item.getDescription();
        String statsText = item.getStatsString(); // OCP in action!
        String fullDescription = "<html><div style='text-align: center;'>" + descriptionText + "<br>" + statsText + "</div></html>";
        JLabel descLabel = new JLabel(fullDescription, SwingConstants.CENTER);
        //descLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        card.add(selectRadio, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        return card;
    }

    private void startGame() {
        // Add chosen items to player's inventory
        selectedPlayer.pickItem(selectedWeapon);
        selectedPlayer.pickItem(selectedPotion); 

        // Start the game
        this.dispose();
        new GameWindow(selectedPlayer).setVisible(true);
    }
}