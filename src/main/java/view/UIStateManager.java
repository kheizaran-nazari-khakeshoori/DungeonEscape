package view;
import java.util.List;

import model.Iwarrior;
import model.Player;

public class UIStateManager {
    private final DungeonPanel dungeonPanel;
    private final ControlPanel controlPanel;
    private final InventoryPanel inventoryPanel;
    private final StatusPanel statusPanel;
    private final PlayerListPanel PlayerListPanel;
    private final LogPanel logPanel;

    public UIStateManager(DungeonPanel dungeonPanel, ControlPanel controlPanel,
                          InventoryPanel inventoryPanel, StatusPanel statusPanel,
                          PlayerListPanel PlayerListPanel, LogPanel logPanel) {
        this.dungeonPanel = dungeonPanel;
        this.controlPanel = controlPanel;
        this.inventoryPanel = inventoryPanel;
        this.statusPanel = statusPanel;
        this.PlayerListPanel = PlayerListPanel;
        this.logPanel = logPanel;
    }

    public void setCombatMode(Player player) {
        dungeonPanel.setDoorsVisible(false);
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
        dungeonPanel.setDoorsVisible(true);
        controlPanel.setAttackButtonVisible(false);
        controlPanel.setFleeButtonVisible(false);
        controlPanel.setContinueButtonVisible(false);
        controlPanel.setSpecialButtonVisible(false);
        controlPanel.setShopButtonVisible(true);
    }

    public void setPostEncounterMode() {//trap and luck encounter 
        dungeonPanel.setDoorsVisible(false);
        controlPanel.setAttackButtonVisible(false);
        controlPanel.setFleeButtonVisible(false);
        controlPanel.setSpecialButtonVisible(false);
        controlPanel.setShopButtonVisible(false);
        controlPanel.setContinueButtonVisible(true);
    }

    public void setGameOverMode() {
        dungeonPanel.setDoorsVisible(false);
        controlPanel.setAllButtonsEnabled(false);
    }

    public void updateAllPanels(Player activePlayer,List<Player> party, Iwarrior currentOpponent) {
        inventoryPanel.updateInventory(activePlayer);
        statusPanel.updateStatus(activePlayer);
        statusPanel.updateEnemyStatus(currentOpponent);
        PlayerListPanel.updatePlayerList(activePlayer, party);
    }

    public void updateSpecialAbilityButton(Player player) {
        boolean ready = player.isSpecialAbilityReady();
        controlPanel.setSpecialButtonEnabled(ready);
        if (ready) 
        {
            controlPanel.setSpecialButtonColor(java.awt.Color.CYAN);
        }      
        else 
        {
            controlPanel.setSpecialButtonColor(java.awt.Color.LIGHT_GRAY);
        }

    }

    public void displayImage(String imagePath) {
        try 
        {
            dungeonPanel.displayImage(imagePath);
        }
        catch (Exception e) 
        {
            logPanel.addMessage("[SYSTEM] Warning: Could not load image. Error: " +
                e.getClass().getSimpleName());
        }
    }

    public void loadBackgroundImage(String imagePath) {
        dungeonPanel.loadBackgroundImage(imagePath);
    }

    public void logMessage(String message) {
        logPanel.addMessage(message);
    }

    public LogPanel getLogPanel() {
        return logPanel;
    }
}