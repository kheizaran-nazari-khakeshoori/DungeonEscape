package view;

import model.Player;
import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
    private JLabel healthLabel;
    private JLabel weaponLabel;

    public HUDPanel() {
        setLayout(new GridLayout(2, 1));
        setBorder(BorderFactory.createTitledBorder("Player Status"));
        setPreferredSize(new Dimension(200, 80));

        healthLabel = new JLabel("Health: ");
        weaponLabel = new JLabel("Weapon: ");

        add(healthLabel);
        add(weaponLabel);
    }

    public void updateStatus(Player player) {
        healthLabel.setText("Health: " + player.getHealth() + " / 100");
        String weaponText = (player.getEquippedWeapon() != null)
                ? "Weapon: " + player.getEquippedWeapon().getName()
                : "Weapon: Fists";
        weaponLabel.setText(weaponText);
    }
}
