package view;

import model.Enemy;
import model.Player;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
    // Player components
    private final JLabel playerNameLabel;
    private final JProgressBar playerHealthBar;

    // Enemy components
    private final JLabel enemyNameLabel;
    private final JProgressBar enemyHealthBar;
    private final JPanel enemyPanel;

    public HUDPanel() {
        setLayout(new GridLayout(2, 1, 5, 5)); // Two rows, one for player, one for enemy
        setBorder(BorderFactory.createTitledBorder("Status"));

        // --- Player Panel ---
        JPanel playerPanel = new JPanel(new BorderLayout(10, 0));
        playerNameLabel = new JLabel();
        playerNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        playerHealthBar = new JProgressBar();
        playerHealthBar.setStringPainted(true);
        playerPanel.add(playerNameLabel, BorderLayout.WEST);
        playerPanel.add(playerHealthBar, BorderLayout.CENTER);

        // --- Enemy Panel ---
        enemyPanel = new JPanel(new BorderLayout(10, 0));
        enemyNameLabel = new JLabel();
        enemyNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        enemyHealthBar = new JProgressBar();
        enemyHealthBar.setStringPainted(true);
        enemyPanel.add(enemyNameLabel, BorderLayout.WEST);
        enemyPanel.add(enemyHealthBar, BorderLayout.CENTER);

        add(playerPanel);
        add(enemyPanel);
    }

    public void updateStatus(Player player) {
        if (player != null) {
            playerNameLabel.setText(player.getName() + ":");
            playerHealthBar.setMaximum(player.getMaxHealth());
            playerHealthBar.setValue(player.getHealth());
            playerHealthBar.setString(player.getHealth() + " / " + player.getMaxHealth());
        }
    }

    public void updateEnemyStatus(Enemy enemy) {
        boolean enemyVisible = (enemy != null && enemy.isAlive());
        enemyPanel.setVisible(enemyVisible);
        if (enemyVisible) {
            enemyNameLabel.setText(enemy.getName() + ":");
            enemyHealthBar.setMaximum(enemy.getMaxHealth());
            enemyHealthBar.setValue(enemy.getHealth());
            enemyHealthBar.setString(enemy.getHealth() + " / " + enemy.getMaxHealth());
        }
    }
}