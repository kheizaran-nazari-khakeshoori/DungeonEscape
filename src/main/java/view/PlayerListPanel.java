package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import model.Player;

public class PlayerListPanel extends JPanel {
    private final JPanel playersContainer;

    public PlayerListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Players"));
        setPreferredSize(new Dimension(200, 300));

        playersContainer = new JPanel();
        playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(playersContainer);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updatePlayerList(Player activePlayer, List<Player> allPlayers) {
        playersContainer.removeAll();//clears the old visual components 

        for (Player p : allPlayers) {
            JPanel playerCard = new JPanel(new GridLayout(2, 1));
            playerCard.setBorder(BorderFactory.createEtchedBorder());
            if (p == activePlayer) {
                playerCard.setBackground(new Color(200, 255, 200)); // Highlight active player
            }

            JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
            JLabel statsLabel = new JLabel("HP: " + p.getHealth() + "/" + p.getMaxHealth(), SwingConstants.CENTER);

            playerCard.add(nameLabel);
            playerCard.add(statsLabel);
            playersContainer.add(playerCard);
            playersContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        revalidate();
        repaint();
    }
}