package com.islandescape.window;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.inventory.Inventory;
import com.islandescape.item.Item;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;
import com.islandescape.structures.CraftingTable;
import com.islandescape.ui.CraftingScreen;

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
    private CraftingScreen craftingScreen;
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

    public void setCraftingScreen(CraftingScreen craftingScreen) {
        this.craftingScreen = craftingScreen;
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
            // Forward input to crafting screen for cursor movement and item placement
            if (craftingScreen != null) {
                boolean p1Near = isPlayerNearTable(player);
                boolean p2Near = isPlayerNearTable(player2);

                craftingScreen.handleInput(
                        keyHandler.consumeP1Action(), keyHandler.consumeP2Action(),
                        keyHandler.getP1Direction().getX(), keyHandler.getP1Direction().getY(),
                        keyHandler.getP2Direction().getX(), keyHandler.getP2Direction().getY(),
                        p1Near, p2Near,
                        craftingSystem, p1Inventory, p2Inventory);
            }

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
        return isPlayerNearTable(player) || isPlayerNearTable(player2);
    }

    private boolean isPlayerNearTable(Player p) {
        return craftingTable != null && p != null
                && craftingTable.isPlayerNearby(p.getX(), p.getY());
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

        // Draw crafting UI overlay on top (pixel-crisp, not in the world buffer)
        if (gameState == GameState.INVENTORY_OPEN && craftingScreen != null) {
            Graphics2D g2 = (Graphics2D) g;
            craftingScreen.render(g2, getWidth(), getHeight(),
                    craftingSystem, p1Inventory, p2Inventory,
                    isPlayerNearTable(player), isPlayerNearTable(player2));
        }
    }
}
