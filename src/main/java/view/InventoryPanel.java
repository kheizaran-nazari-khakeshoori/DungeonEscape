package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Item;
import model.Player;

public class InventoryPanel extends JPanel {
    private final JTextArea inventoryArea;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Inventory"));
        setPreferredSize(new Dimension(200, 300));

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setMargin(new Insets(5, 5, 5, 5));//It adds 5 pixels of padding inside the text area on all sides.
        
        add(new JScrollPane(inventoryArea), BorderLayout.CENTER);
    }

    public void updateInventory(Player player) {
        inventoryArea.setText(""); // Clear old text
        for (Item item : player.getInventory().getItems()) {
            inventoryArea.append("- " + item.getName() + "\n");
        }
    }
}
