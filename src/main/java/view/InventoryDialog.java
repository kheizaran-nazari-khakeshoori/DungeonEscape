package view;

import java.util.List;

import javax.swing.JOptionPane;

public class InventoryDialog {

    public String showAndGetSelectedItem(List<String> itemNames) {
        if (itemNames.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your inventory is empty.");
            return null;
        }

        Object[] itemNamesArray = itemNames.toArray();
        String selectedItem = (String) JOptionPane.showInputDialog(null,
                "Choose an item to use:", "Inventory",
                JOptionPane.PLAIN_MESSAGE, null, itemNamesArray, itemNamesArray[0]);

        // Returns the selected item name, or null if the user cancelled.
        return selectedItem;
    }
}
