package com.dungeonescape;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import view.GameWindow;

public class ItemSelectionWindow extends JFrame {

    private final Player selectedPlayer;
    private Weapon selectedWeapon;
    private Potion selectedPotion;

    public ItemSelectionWindow(Player player) {
        this.selectedPlayer = player;

        setTitle("Choose Your Starting Gear");
        setSize(800, 900); // Increased height to accommodate all items
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- WEAPON SELECTION ---
        JPanel weaponPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, as many rows as needed
        weaponPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Weapon"));

        List<Weapon> availableWeapons = createAvailableWeapons();
        ButtonGroup weaponGroup = new ButtonGroup();
        for (Weapon weapon : availableWeapons) {
            JPanel itemCard = createItemCard(weapon, weaponGroup);
            weaponPanel.add(itemCard);
        }

        // --- POTION SELECTION ---
        JPanel potionPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, as many rows as needed
        potionPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Potion"));

        List<Potion> availablePotions = createAvailablePotions();
        ButtonGroup potionGroup = new ButtonGroup();
        for (Potion potion : availablePotions) {
            JPanel itemCard = createItemCard(potion, potionGroup);
            potionPanel.add(itemCard);
        }

        // --- START BUTTON ---
        JButton startButton = new JButton("Begin Adventure!");
        startButton.setFont(new Font("Serif", Font.BOLD, 18));
        startButton.addActionListener(e -> startGame());

        // Set default selections
        if (!availableWeapons.isEmpty()) {
            selectedWeapon = availableWeapons.get(0);
            ((JRadioButton)((JPanel)weaponPanel.getComponent(0)).getComponent(0)).setSelected(true);
        }
        if (!availablePotions.isEmpty()) {
            selectedPotion = availablePotions.get(0);
            ((JRadioButton)((JPanel)potionPanel.getComponent(0)).getComponent(0)).setSelected(true);
        }

        // Add panels to frame
        JPanel selectionContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        selectionContainer.add(weaponPanel);
        selectionContainer.add(potionPanel);

        add(selectionContainer, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }

    private List<Weapon> createAvailableWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(new Weapon("Greatsword", "A heavy two-handed sword. High damage, but average durability.", 15, 30, DamageType.SLASHING, "images/weapons/Greatsword.png"));
        weapons.add(new Weapon("Runic Bow", "A bow etched with runes. Good damage and durability.", 14, 35, DamageType.PIERCING, "images/weapons/RunicBow.png"));
        weapons.add(new Weapon("Dual Daggers", "A pair of quick daggers. Lower damage, but very durable.", 8, 40, DamageType.PIERCING, "images/weapons/DualDaggers.png"));
        weapons.add(new Weapon("Enchanted Staff", "A staff crackling with fire. High damage, but fragile.", 18, 20, DamageType.FIRE, "images/weapons/EnchantedStaff.png"));
        weapons.add(new Weapon("War Axe", "A brutal axe that cleaves through armor. Good damage, low durability.", 16, 25, DamageType.SLASHING, "images/weapons/WarAxe..png"));
        weapons.add(new Weapon("Crossbow", "A powerful crossbow that hits hard but is very fragile.", 20, 15, DamageType.PIERCING, "images/weapons/CrossBow.png"));
        return weapons;
    }

    private List<Potion> createAvailablePotions() {
        List<Potion> potions = new ArrayList<>();
        potions.add(new Potion("Health Potion", "A swirling red liquid that restores 25 health.", 25, 1, "images/potions/ManaPotion.png")); // Assuming ManaPotion.png is your health potion image
        potions.add(new Potion("Stamina Elixir", "A potent green elixir that restores a large amount of health (50).", 50, 1, "images/potions/StaminaElixir.png"));
        potions.add(new Potion("Antidote", "A chalky fluid that cures poison, but provides no direct healing.", 0, 1, "images/potions/Antidote..png"));
        return potions;
    }

    private JPanel createItemCard(Item item, ButtonGroup group) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEtchedBorder());

        JRadioButton selectRadio = new JRadioButton(item.getName());
        selectRadio.setFont(new Font("Serif", Font.BOLD, 16));
        selectRadio.setHorizontalAlignment(SwingConstants.CENTER);
        group.add(selectRadio);

        selectRadio.addActionListener(e -> {
            boolean isWeapon = item instanceof Weapon;
            if (isWeapon) selectedWeapon = (Weapon) item;
            else selectedPotion = (Potion) item;
        });

        JLabel imageLabel;
        java.net.URL imgURL = getClass().getClassLoader().getResource(item.getImagePath());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        } else {
            System.err.println("Warning: Could not find image resource at " + item.getImagePath());
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(160, 160));
        }

        // Use the new description field from the Item class, plus the stats.
        String descriptionText = item.getDescription();
        String statsText;
        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            statsText = "<b>Dmg: " + w.getDamage() + " | Dura: " + w.getDurability() + "</b>";
        } else {
            Potion p = (Potion) item;
            statsText = "<b>Heals: " + p.getHealAmount() + " HP</b>";
        }
        String fullDescription = "<html><div style='text-align: center;'>" + descriptionText + "<br>" + statsText + "</div></html>";
        JLabel descLabel = new JLabel(fullDescription, SwingConstants.CENTER);

        card.add(selectRadio, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        return card;
    }

    private void startGame() {
        // Add chosen items to player's inventory
        selectedPlayer.pickItem(selectedWeapon);
        selectedPlayer.pickItem(selectedPotion); // A valid potion is always selected by default

        // Start the game
        this.dispose();
        new GameWindow(selectedPlayer).setVisible(true);
    }
}