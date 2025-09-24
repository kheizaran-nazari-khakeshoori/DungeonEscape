package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.dungeonescape.Game;

import model.Antidote;
import model.InvisibilityPotion;
import model.Item;
import model.Potion;
import model.ShopEncounter;
import model.StaminaElixir;
import model.Weapon;

public class ShopDialog extends JDialog {
    private final Game game;
    private final ShopEncounter shopEncounter;
    private final JLabel goldLabel;
    private final JPanel itemsPanel;

    public ShopDialog(Game game, ShopEncounter shopEncounter) {
        super((JFrame) null, "Welcome to the Shop!", true); // true for modal
        this.game = game;
        this.shopEncounter = shopEncounter;

        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);

        // Gold Display Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        goldLabel = new JLabel("Your Gold: " + game.getActivePlayer().getGold(), SwingConstants.RIGHT);
        goldLabel.setFont(new Font("Serif", Font.BOLD, 18));
        topPanel.add(goldLabel, BorderLayout.EAST);

        // Items Panel
        itemsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        refreshShopItems();

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null); // Center the dialog
    }

    private void attemptPurchase(Item item) {
        int itemCost = item.getCost();
        boolean purchaseSuccessful = shopEncounter.purchaseItem(game.getActivePlayer(), item.getName());

        if (purchaseSuccessful) {
            game.getLogPanel().addMessage("You bought " + item.getName() + " for " + itemCost + " gold.");
            // Refresh display to show updated inventory and gold
            refreshShopItems();
            game.updateGUI(); // Update all panels
        } else {
            game.getLogPanel().addMessage("Not enough gold to buy " + item.getName() + ".");
        }
    }
    
    private void refreshShopItems() {
        itemsPanel.removeAll(); // Clear old items before refreshing
        for (Item item : shopEncounter.getShop().getInventory()) {
            JPanel card = createShopItemCard(item);
            itemsPanel.add(card);
        }
        // Re-validate and repaint the panel to show the new cards
        itemsPanel.revalidate();
        itemsPanel.repaint();
        goldLabel.setText("Your Gold: " + game.getActivePlayer().getGold());
    }

    private JPanel createShopItemCard(Item item) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEtchedBorder());

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 16));

        JLabel imageLabel;
        java.net.URL imgURL = getClass().getClassLoader().getResource(item.getImagePath());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        } else {
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(120, 120));
        }

        String descriptionText = item.getDescription();
        String statsText;
        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            statsText = "<b>Dmg: " + w.getDamage() + " | Dura: " + w.getDurability() + "</b>";
        } else if (item instanceof InvisibilityPotion) {
            statsText = "<b>Guarantees Flee</b>";
        } else if (item instanceof Antidote) {
            statsText = "<b>Cures Poison</b>";
        } else if (item instanceof StaminaElixir) {
            Potion p = (Potion) item;
            statsText = "<b>Heals: " + p.getHealAmount() + " + Regen</b>";
        } else { // Regular Potion
            Potion p = (Potion) item;
            statsText = "<b>Heals: " + p.getHealAmount() + " HP</b>";
        }
        String fullDescription = "<html><div style='text-align: center;'>" + descriptionText + "<br>" + statsText + "</div></html>";
        JLabel descLabel = new JLabel(fullDescription, SwingConstants.CENTER);

        JButton buyButton = new JButton("Buy (" + item.getCost() + " Gold)");
        buyButton.addActionListener(e -> attemptPurchase(item));
        // Disable button if player can't afford it
        if (game.getActivePlayer().getGold() < item.getCost()) {
            buyButton.setEnabled(false);
        }

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        card.add(buyButton, BorderLayout.PAGE_END); // Use PAGE_END for consistency
        return card;
    }
}