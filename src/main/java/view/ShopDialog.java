package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.dungeonescape.Game;

import model.Item;
import model.ShopEncounter;

public class ShopDialog extends JDialog {
    private final Game game;
    private final ShopEncounter shopEncounter;
    private final JTextArea inventoryDisplay;

    public ShopDialog(Game game, ShopEncounter shopEncounter) {
        super((JFrame) null, "Welcome to the Shop!", true); // true for modal
        this.game = game;
        this.shopEncounter = shopEncounter;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));

        // Display shop inventory
        inventoryDisplay = new JTextArea();
        inventoryDisplay.setEditable(false);
        updateShopDisplay(); // Call a method to update the display
        add(new JScrollPane(inventoryDisplay), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        for (Item item : shopEncounter.getShop().getInventory()) {
            JButton buyButton = new JButton("Buy " + item.getName());
            buyButton.addActionListener(new ActionListener() { // Anonymous inner class for action listener
                @Override
                public void actionPerformed(ActionEvent e) {
                    attemptPurchase(item.getName());
                }
            });
            buttonPanel.add(buyButton);
        }
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the dialog
    }

    private void attemptPurchase(String itemName) {
        boolean purchaseSuccessful = shopEncounter.purchaseItem(game.getActivePlayer(), itemName);

        if (purchaseSuccessful) {
            game.getLogPanel().addMessage("You bought " + itemName + "!");
            // Refresh display to show updated inventory and gold
            updateShopDisplay();
            game.updateGUI(); // Update all panels
        } else {
            game.getLogPanel().addMessage("Could not buy " + itemName + ".");
        }
    }
    
    private void updateShopDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Shop Inventory ---").append("\n");
        sb.append("Your Gold: ").append(game.getActivePlayer().getGold()).append("\n\n");
        for (Item item : shopEncounter.getShop().getInventory()) {
            sb.append("- ").append(item.getName());
            sb.append(" (Cost: ").append(item.getCost()).append(" Gold)");
            sb.append("\n");
        }
        inventoryDisplay.setText(sb.toString());
    }

    public JTextArea getInventoryDisplay() {
        return inventoryDisplay;
    }
}