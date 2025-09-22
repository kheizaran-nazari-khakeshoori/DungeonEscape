package view;

import model.Player;
import model.Enemy;
import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
    private JLabel healthLabel;
    private JLabel weaponLabel;
    private JLabel enemyHealthLabel;

    public HUDPanel() {
        setLayout(new GridLayout(3, 1)); // Increased rows for enemy health
        setBorder(BorderFactory.createTitledBorder("Player Status"));
        setPreferredSize(new Dimension(200, 100));

        healthLabel = new JLabel("Health: ");
        weaponLabel = new JLabel("Weapon: ");
        enemyHealthLabel = new JLabel("Enemy: ");

        add(healthLabel);
        add(weaponLabel);
        add(enemyHealthLabel);
        enemyHealthLabel.setVisible(false); // Hide it initially
    }

    public void updateStatus(Player player) {
        healthLabel.setText("Health: " + player.getHealth() + " / " + player.getMaxHealth());
        String weaponText = (player.getEquippedWeapon() != null)
                ? "Weapon: " + player.getEquippedWeapon().getName()
                : "Weapon: Fists";
        weaponLabel.setText(weaponText);
    }

    public void updateEnemyStatus(Enemy enemy) {
        if (enemy != null) {
            enemyHealthLabel.setText("Enemy: " + enemy.getName() + " | HP: " + enemy.getHealth());
            enemyHealthLabel.setVisible(true);
        } else {
            clearEnemyStatus();
        }
    }

    public void clearEnemyStatus() {
        enemyHealthLabel.setText("Enemy: ");
        enemyHealthLabel.setVisible(false);
    }
}
