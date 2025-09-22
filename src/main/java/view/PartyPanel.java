package view;

import model.Player;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PartyPanel extends JPanel {
    private final JPanel playersContainer;

    public PartyPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Party"));
        setPreferredSize(new Dimension(200, 300));

        playersContainer = new JPanel();
        playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
        add(new JScrollPane(playersContainer), BorderLayout.CENTER);
    }

    public void updateParty(Player activePlayer, List<Player> allPlayers) {
        playersContainer.removeAll();

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