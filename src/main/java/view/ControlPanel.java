package view;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    public JButton exploreButton;
    public JButton inventoryButton;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createTitledBorder("Actions"));

        exploreButton = new JButton("Explore");
        inventoryButton = new JButton("Manage Inventory");

        add(exploreButton);
        add(inventoryButton);
    }
}