package view;

import model.Item;
import model.Player;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private JTextArea inventoryArea;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Inventory"));
        setPreferredSize(new Dimension(200, 300));

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setMargin(new Insets(5, 5, 5, 5));
        add(new JScrollPane(inventoryArea), BorderLayout.CENTER);
    }

    public void updateInventory(Player player) {
        inventoryArea.setText(""); // Clear old text
        for (Item item : player.getInventory().getItems()) {
            inventoryArea.append("- " + item.getName() + "\n");
        }
    }
}
