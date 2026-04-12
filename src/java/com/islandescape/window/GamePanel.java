package com.islandescape.window;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;
import com.islandescape.structures.CraftingTable;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;

/*
    GamePanel sits inside GameWindow.
    It holds the TileMap and MapRenderer, and paints the map every frame.
    Renders the map at native resolution, then scales to fit the panel.
 */
public class GamePanel extends JPanel {

    private final TileMap map;
    private final MapRenderer renderer;

    private Player player;
    private Player player2;
    private GameKeyHandler keyHandler;
    private Timer gameLoop;

    private Inventory p1Inventory;
    private Inventory p2Inventory;
    private CraftingSystem craftingSystem;
    private CraftingTable craftingTable;
    private GameState gameState = GameState.PLAYING;

    public GamePanel(TileMap map, MapRenderer renderer) {
        this.map = map;
        this.renderer = renderer;
        setFocusable(true);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setKeyHandler(GameKeyHandler handler) {
        if (this.keyHandler != null) {
            removeKeyListener(this.keyHandler);
        }
        this.keyHandler = handler;
        addKeyListener(handler);
    }

    public void setInventories(Inventory p1, Inventory p2) {
        this.p1Inventory = p1;
        this.p2Inventory = p2;
    }

    public void setCraftingSystem(CraftingSystem craftingSystem) {
        this.craftingSystem = craftingSystem;
    }

    public void setCraftingTable(CraftingTable craftingTable) {
        this.craftingTable = craftingTable;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void startGameLoop() {
        gameLoop = new Timer(16, e -> {
            update();
            repaint();
        });
        gameLoop.start();
    }

    private void update() {
        if (keyHandler == null) return;

        // Handle inventory toggle (I key) — single-press consumed once per press
        if (keyHandler.consumeInventoryToggle()) {
            if (gameState == GameState.INVENTORY_OPEN) {
                closeInventory();
            } else if (isAnyPlayerNearTable()) {
                gameState = GameState.INVENTORY_OPEN;
            }
        }

        if (gameState == GameState.PLAYING) {
            // Move players
            if (player != null) {
                player.move(keyHandler.getP1Direction());
            }
            if (player2 != null) {
                player2.move(keyHandler.getP2Direction());
            }
        } else if (gameState == GameState.INVENTORY_OPEN) {
            // Handle craft commit (Enter key)
            if (keyHandler.consumeCraftCommit() && craftingSystem != null) {
                Item result = craftingSystem.craft(p1Inventory);
                if (result != null && p1Inventory != null) {
                    p1Inventory.add(result);
                }
            }
        }
    }

    private boolean isAnyPlayerNearTable() {
        if (craftingTable == null) return false;
        boolean p1Near = player != null
                && craftingTable.isPlayerNearby(player.getX(), player.getY());
        boolean p2Near = player2 != null
                && craftingTable.isPlayerNearby(player2.getX(), player2.getY());
        return p1Near || p2Near;
    }

    private void closeInventory() {
        if (craftingSystem != null && p1Inventory != null && p2Inventory != null) {
            craftingSystem.clearGrid(p1Inventory, p2Inventory);
        }
        gameState = GameState.PLAYING;
    }

    public TileMap getMap() {
        return map;
    }

    public MapRenderer getRenderer() {
        return renderer;
    }

    // Called by Swing whenever the panel needs to be drawn
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        map.renderMapComponent(renderer, g, getWidth(), getHeight(), player, player2);
    }
}
