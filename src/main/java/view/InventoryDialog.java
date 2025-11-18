package view;

import java.util.List;

import javax.swing.JOptionPane;

//for beign able to choose an item from the inventory 
//this will pop-up a window with a list of items 
public class InventoryDialog {

    public String showAndGetSelectedItem(List<String> itemNames) {
        if (itemNames.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your inventory is empty.");
            return null;
        }

        Object[] itemNamesArray = itemNames.toArray();//converting the list of items to an array 
        String selectedItem = (String) JOptionPane.showInputDialog(null,
                "Choose an item to use:", "Inventory",
                JOptionPane.PLAIN_MESSAGE, null, itemNamesArray, itemNamesArray[0]);

        // Returns the selected item name, or null if the user cancelled.
        return selectedItem;
    }
}
