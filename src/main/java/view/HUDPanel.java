package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import model.Enemy;
import model.Player;

public class HUDPanel extends JPanel {
    // Player components
    private final JPanel playerPanel;
    private final JLabel playerNameLabel;
    private final JProgressBar playerHealthBar;
    private final JLabel goldLabel;

    // Enemy components
    private final JPanel enemyPanel;
    private final JLabel enemyNameLabel;
    private final JProgressBar enemyHealthBar;
    

    public HUDPanel() {
        setLayout(new GridLayout(2, 1, 5, 5)); 
        setBorder(BorderFactory.createTitledBorder("Status"));

        playerPanel = new JPanel(new BorderLayout(10, 5));
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

        playerNameLabel = new JLabel();
        playerNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        goldLabel = new JLabel("Gold: 0");
        goldLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        goldLabel.setForeground(new Color(218, 165, 32)); 
        playerInfoPanel.add(playerNameLabel);
        playerInfoPanel.add(goldLabel);

        playerHealthBar = new JProgressBar();
        playerHealthBar.setStringPainted(true);
        playerPanel.add(playerInfoPanel, BorderLayout.WEST);
        playerPanel.add(playerHealthBar, BorderLayout.CENTER);

        
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
            setHealthBarColor(playerHealthBar);
            goldLabel.setText("Gold: " + player.getGold());
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
            setHealthBarColor(enemyHealthBar);
        }
    }

    private void setHealthBarColor(JProgressBar healthBar) {
        double percentage = (double) healthBar.getValue() / healthBar.getMaximum();
        if (percentage <= 0.25) {
            healthBar.setForeground(new Color(200, 0, 0)); // Red
        } else if (percentage <= 0.5) {
            healthBar.setForeground(new Color(220, 220, 0)); // Yellow
        } else {
            healthBar.setForeground(new Color(0, 200, 0)); // Green
        }
    }
}