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
    private final JButton shopButton; 
    private final JButton continueButton; 
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
        shopButton = new JButton("Shop"); // Initialize the shop button
        continueButton = new JButton("Continue"); // Initialize the continue button
        exitButton = new JButton("Exit Game");

        add(inventoryButton);
        add(attackButton);
        add(shopButton); 
        add(specialButton);
        add(continueButton); 
        add(fleeButton);
        add(exitButton);
    }

    //It keeps the buttons private (encapsulation).
    //Other classes can react to button clicks by providing a listener, but cannot modify the buttons directly.
    //It makes your code safer and easier to maintain.
    // Public methods to add listeners without exposing the buttons themselves
    public void addInventoryListener(ActionListener listener) { inventoryButton.addActionListener(listener); }
    public void addAttackListener(ActionListener listener) { attackButton.addActionListener(listener); }
    public void addFleeListener(ActionListener listener) { fleeButton.addActionListener(listener); }
    public void addShopListener(ActionListener listener) { shopButton.addActionListener(listener); } 
    public void addContinueListener(ActionListener listener) { continueButton.addActionListener(listener); } 
    public void addSpecialListener(ActionListener listener) { specialButton.addActionListener(listener); }
    public void addExitListener(ActionListener listener) { exitButton.addActionListener(listener); }

    // Public methods to control the state of the buttons
    public void setAttackButtonVisible(boolean visible) { attackButton.setVisible(visible); }
    public void setFleeButtonVisible(boolean visible) { fleeButton.setVisible(visible); }
    public void setSpecialButtonVisible(boolean visible) { specialButton.setVisible(visible); }
    public void setShopButtonVisible(boolean visible) { shopButton.setVisible(visible); } 
    public void setContinueButtonVisible(boolean visible) { continueButton.setVisible(visible); } 

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
        shopButton.setEnabled(enabled); 
        specialButton.setEnabled(enabled);
        continueButton.setEnabled(enabled);
        //exitButton.setEnabled(enabled);
    }
}