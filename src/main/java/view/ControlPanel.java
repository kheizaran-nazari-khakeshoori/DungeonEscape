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
    private final JButton continueButton; // New button for continuing after non-combat encounters
    private final JButton exitButton;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createTitledBorder("Actions"));

        inventoryButton = new JButton("Inventory");
        attackButton = new JButton("Attack!");
        fleeButton = new JButton("Flee!");
        specialButton = new JButton("Special");
        specialButton.setOpaque(true); // Make sure background color is visible
        specialButton.setBackground(java.awt.Color.CYAN); // Default to ready (blue)
        specialButton.setBorderPainted(false); // Optional: remove border for cleaner look
        shopButton = new JButton("Shop"); // Initialize the shop button
        continueButton = new JButton("Continue"); // Initialize the continue button
        exitButton = new JButton("Exit Game");

        add(inventoryButton);
        add(attackButton);
        add(shopButton); // Add the shop button to the panel
        add(specialButton);
        add(continueButton); // Add the continue button to the panel
        add(fleeButton);
        add(exitButton);
    }

    // Public methods to add listeners without exposing the buttons themselves
    public void addInventoryListener(ActionListener listener) { inventoryButton.addActionListener(listener); }
    public void addAttackListener(ActionListener listener) { attackButton.addActionListener(listener); }
    public void addFleeListener(ActionListener listener) { fleeButton.addActionListener(listener); }
    public void addShopListener(ActionListener listener) { shopButton.addActionListener(listener); } // New listener for shop
    public void addContinueListener(ActionListener listener) { continueButton.addActionListener(listener); } // New listener for continue button
    public void addSpecialListener(ActionListener listener) { specialButton.addActionListener(listener); }
    public void addExitListener(ActionListener listener) { exitButton.addActionListener(listener); }

    // Public methods to control the state of the buttons
    public void setAttackButtonVisible(boolean visible) { attackButton.setVisible(visible); }
    public void setFleeButtonVisible(boolean visible) { fleeButton.setVisible(visible); }
    public void setSpecialButtonVisible(boolean visible) { specialButton.setVisible(visible); }
    public void setShopButtonVisible(boolean visible) { shopButton.setVisible(visible); } // New method for shop button visibility
    public void setContinueButtonVisible(boolean visible) { continueButton.setVisible(visible); } // New method for continue button visibility

    public void setInventoryButtonEnabled(boolean enabled) { inventoryButton.setEnabled(enabled); }
    public void setAttackButtonEnabled(boolean enabled) { attackButton.setEnabled(enabled); }
    public void setFleeButtonEnabled(boolean enabled) { fleeButton.setEnabled(enabled); }
    public void setSpecialButtonEnabled(boolean enabled) { specialButton.setEnabled(enabled); }
    public void setSpecialButtonColor(java.awt.Color color) { specialButton.setBackground(color); }
    public boolean isContinueButtonVisible() { return continueButton.isVisible(); } // New getter

    public void setAllButtonsEnabled(boolean enabled) {
        inventoryButton.setEnabled(enabled);
        attackButton.setEnabled(enabled);
        fleeButton.setEnabled(enabled);
        shopButton.setEnabled(enabled); // Enable/disable shop button
        specialButton.setEnabled(enabled);
        continueButton.setEnabled(enabled); // Enable/disable continue button
        exitButton.setEnabled(enabled);
    }
}