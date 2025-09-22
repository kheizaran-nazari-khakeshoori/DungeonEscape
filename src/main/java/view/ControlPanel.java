package view;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    public JButton exploreButton;
    public JButton inventoryButton;
    public JButton attackButton;
    public JButton fleeButton;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createTitledBorder("Actions"));

        exploreButton = new JButton("Explore");
        inventoryButton = new JButton("Manage Inventory");
        attackButton = new JButton("Attack!");
        fleeButton = new JButton("Flee!");

        add(exploreButton);
        add(inventoryButton);
        add(attackButton);
        add(fleeButton);

        // Combat buttons are hidden initially
        attackButton.setVisible(false);
        fleeButton.setVisible(false);
    }
}