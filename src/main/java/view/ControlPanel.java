package view;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    public JButton inventoryButton;
    public JButton attackButton;
    public JButton fleeButton;
    public JButton exitButton;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createTitledBorder("Actions"));

        attackButton = new JButton("Attack!");
        fleeButton = new JButton("Flee!");
        exitButton = new JButton("Exit Game");

        add(attackButton);
        add(fleeButton);
        add(exitButton);
    }
}