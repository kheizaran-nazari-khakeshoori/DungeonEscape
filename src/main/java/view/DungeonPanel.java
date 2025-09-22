package view;

import javax.swing.*;
import java.awt.*;

public class DungeonPanel {
    public DungeonPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Dungeon Map"));
        add(new JLabel("Map will be displayed here.", SwingConstants.CENTER), BorderLayout.CENTER);
        setPreferredSize(new Dimension(400, 300));
    }
}
