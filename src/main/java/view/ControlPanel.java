package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    private final JButton inventoryButton;
    private final JButton attackButton;
    private final JButton fleeButton;
    private final JButton specialButton;
    private final JButton shopButton; // New shop button
    private final JButton exitButton;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createTitledBorder("Actions"));

        inventoryButton = new JButton("Inventory");
        attackButton = new JButton("Attack!");
        fleeButton = new JButton("Flee!");
        specialButton = new JButton("Special");
        shopButton = new JButton("Shop"); // Initialize the shop button
        exitButton = new JButton("Exit Game");

        add(inventoryButton);
        add(attackButton);
        add(shopButton); // Add the shop button to the panel
        add(specialButton);
        add(fleeButton);
        add(exitButton);
    }

    // Public methods to add listeners without exposing the buttons themselves
    public void addInventoryListener(ActionListener listener) { inventoryButton.addActionListener(listener); }
    public void addAttackListener(ActionListener listener) { attackButton.addActionListener(listener); }
    public void addFleeListener(ActionListener listener) { fleeButton.addActionListener(listener); }
    public void addShopListener(ActionListener listener) { shopButton.addActionListener(listener); } // New listener for shop
    public void addSpecialListener(ActionListener listener) { specialButton.addActionListener(listener); }
    public void addExitListener(ActionListener listener) { exitButton.addActionListener(listener); }

    // Public methods to control the state of the buttons
    public void setAttackButtonVisible(boolean visible) { attackButton.setVisible(visible); }
    public void setFleeButtonVisible(boolean visible) { fleeButton.setVisible(visible); }
    public void setShopButtonVisible(boolean visible) { shopButton.setVisible(visible); } // New method for shop button visibility
    public void setSpecialButtonVisible(boolean visible) { specialButton.setVisible(visible); }
    public void setInventoryButtonEnabled(boolean enabled) { inventoryButton.setEnabled(enabled); }
    public void setAttackButtonEnabled(boolean enabled) { attackButton.setEnabled(enabled); }
    public void setFleeButtonEnabled(boolean enabled) { fleeButton.setEnabled(enabled); }

    public void setAllButtonsEnabled(boolean enabled) {
        inventoryButton.setEnabled(enabled);
        attackButton.setEnabled(enabled);
        fleeButton.setEnabled(enabled);
        shopButton.setEnabled(enabled); // Enable/disable shop button
        specialButton.setEnabled(enabled);
        exitButton.setEnabled(enabled);
    }
}