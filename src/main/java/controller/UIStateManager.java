package controller;

import model.Enemy;
import model.Player;
import view.ControlPanel;
import view.DungeonPanel;
import view.HUDPanel;
import view.InventoryPanel;
import view.LogPanel;
import view.PartyPanel;

public class UIStateManager {
    private final DungeonPanel dungeonPanel;
    private final ControlPanel controlPanel;
    private final InventoryPanel inventoryPanel;
    private final HUDPanel hudPanel;
    private final PartyPanel partyPanel;
    private final LogPanel logPanel;

    public UIStateManager(DungeonPanel dungeonPanel, ControlPanel controlPanel,
                          InventoryPanel inventoryPanel, HUDPanel hudPanel,
                          PartyPanel partyPanel, LogPanel logPanel) {
        this.dungeonPanel = dungeonPanel;
        this.controlPanel = controlPanel;
        this.inventoryPanel = inventoryPanel;
        this.hudPanel = hudPanel;
        this.partyPanel = partyPanel;
        this.logPanel = logPanel;
    }

    public void setCombatMode(Player player) {
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.setAttackButtonVisible(true);
        controlPanel.setFleeButtonVisible(true);
        controlPanel.setSpecialButtonVisible(true);
        updateSpecialAbilityButton(player);
        controlPanel.setShopButtonVisible(false);
    }

    public void setDoorMode() {
        setDoorMode(true);
    }

    public void setDoorMode(boolean clearImage) {
        if (clearImage) {
            dungeonPanel.clearImage();
        }
        dungeonPanel.door1Button.setVisible(true);
        dungeonPanel.door2Button.setVisible(true);
        controlPanel.setAttackButtonVisible(false);
        controlPanel.setFleeButtonVisible(false);
        controlPanel.setContinueButtonVisible(false);
        controlPanel.setSpecialButtonVisible(false);
        controlPanel.setShopButtonVisible(true);
    }

    public void setPostEncounterMode() {
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.setAttackButtonVisible(false);
        controlPanel.setFleeButtonVisible(false);
        controlPanel.setSpecialButtonVisible(false);
        controlPanel.setShopButtonVisible(false);
        controlPanel.setContinueButtonVisible(true);
    }

    public void setGameOverMode() {
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.setAllButtonsEnabled(false);
    }

    public void updateAllPanels(Player activePlayer, java.util.List<Player> party, Enemy currentEnemy) {
        inventoryPanel.updateInventory(activePlayer);
        hudPanel.updateStatus(activePlayer);
        hudPanel.updateEnemyStatus(currentEnemy);
        partyPanel.updateParty(activePlayer, party);
    }

    public void updateSpecialAbilityButton(Player player) {
        controlPanel.setSpecialButtonEnabled(player.isSpecialAbilityReady());
        controlPanel.setSpecialButtonColor(player.isSpecialAbilityReady() ?
            java.awt.Color.CYAN : java.awt.Color.LIGHT_GRAY);
    }

    public void displayImage(String imagePath) {
        try {
            dungeonPanel.displayImage(imagePath);
        } catch (Exception e) {
            logPanel.addMessage("[SYSTEM] Warning: Could not load image. Error: " +
                e.getClass().getSimpleName());
        }
    }

    public void loadBackgroundImage(String imagePath) {
        dungeonPanel.loadBackgroundImage(imagePath);
    }

    public LogPanel getLogPanel() {
        return logPanel;
    }
}