package com.islandescape.window;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.input.InventoryMouseHandler;
import com.islandescape.inventory.InventoryCursor;
import com.islandescape.inventory.InventoryScreen;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;
import com.islandescape.structures.CraftingTable;
import com.islandescape.ui.CraftingScreen;

import javax.swing.*;
import java.awt.*;

/*
    GamePanel sits inside GameWindow.
    It holds the TileMap and MapRenderer, and paints the map every frame.
    Renders the map at native resolution, then scales to fit the panel.
 */
public class GamePanel extends JPanel {

    private final TileMap map;
    private final MapRenderer renderer;

    private Player player1;
    private Player player2;
    private GameKeyHandler keyHandler;
    private Timer gameLoop;
    private InventoryScreen inventoryScreen;

    private CraftingSystem craftingSystem;
    private CraftingTable craftingTable;
    private CraftingScreen craftingScreen;
    private GameState gameState = GameState.PLAYING;

    public GamePanel(TileMap map, MapRenderer renderer, Player player1, Player player2) {
        this.map = map;
        this.renderer = renderer;
        this.player1 = player1;
        this.player2 = player2;

        InventoryCursor cursor = new InventoryCursor();
        this.inventoryScreen = new InventoryScreen(player1, player2, cursor);
        InventoryMouseHandler mouseHandler = new InventoryMouseHandler(inventoryScreen, this);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        setFocusable(true);
    }

    public void setKeyHandler(GameKeyHandler handler) {
        if (this.keyHandler != null) {
            removeKeyListener(this.keyHandler);
        }
        this.keyHandler = handler;
        addKeyListener(handler);
    }

    public void setCraftingSystem(CraftingSystem craftingSystem) {
        this.craftingSystem = craftingSystem;
        if (inventoryScreen != null) {
            inventoryScreen.setCraftingSystem(craftingSystem);
        }
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

        // Crafting-screen toggle (I key) — single-press consumed once per press
        if (keyHandler.consumeCraftScreenToggle()) {
            if (gameState == GameState.INVENTORY_OPEN) {
                closeCraftingScreen();
            } else if (isAnyPlayerNearTable()) {
                gameState = GameState.INVENTORY_OPEN;
                if (inventoryScreen != null) {
                    inventoryScreen.setCraftingOpen(true);
                    inventoryScreen.setOpen(true);
                }
            }
        }

        if (gameState == GameState.PLAYING) {
            if (player1 != null) {
                player1.move(keyHandler.getP1Direction());
            }
            if (player2 != null) {
                player2.move(keyHandler.getP2Direction());
            }
        }

        // Update proximity flags for inventory blocking
        if (gameState == GameState.INVENTORY_OPEN && inventoryScreen != null) {
            inventoryScreen.setPlayerNearTable(
                    isPlayerNearTable(player1), isPlayerNearTable(player2));
        }
    }

    private boolean isAnyPlayerNearTable() {
        return isPlayerNearTable(player1) || isPlayerNearTable(player2);
    }

    private boolean isPlayerNearTable(Player p) {
        return craftingTable != null && p != null
                && craftingTable.isPlayerNearby(p.getX(), p.getY());
    }

    public void closeCraftingScreen() {
        if (craftingSystem != null && player1 != null && player2 != null) {
            craftingSystem.clearGrid(player1.getInventory(), player2.getInventory());
        }
        if (inventoryScreen != null) {
            inventoryScreen.returnHeldItem();
            inventoryScreen.setCraftingOpen(false);
            inventoryScreen.setOpen(false);
        }
        gameState = GameState.PLAYING;
    }

    public TileMap getMap() {
        return map;
    }

    public MapRenderer getRenderer() {
        return renderer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void toggleInventoryScreen() {
        if (inventoryScreen == null) return;
        if (inventoryScreen.isOpen()) {
            inventoryScreen.returnHeldItem();
        }
        inventoryScreen.setOpen(!inventoryScreen.isOpen());
        repaint();
    }

    public boolean isInventoryScreenOpen() {
        return inventoryScreen != null && inventoryScreen.isOpen();
    }

    // Called by Swing whenever the panel needs to be drawn
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(new Color(77, 166, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

        map.renderMapComponent(renderer, g, getWidth(), getHeight(), player1, player2);

        if (gameState == GameState.INVENTORY_OPEN && craftingScreen != null) {
            // 1. Crafting panel (dim overlay + brown background + crafting grid)
            craftingScreen.render(g2, getWidth(), getHeight(),
                    craftingSystem,
                    player1 != null ? player1.getInventory() : null,
                    player2 != null ? player2.getInventory() : null,
                    isPlayerNearTable(player1), isPlayerNearTable(player2));

            // 2. Inventory grids (drawn ON TOP of the brown panel)
            if (inventoryScreen != null) {
                inventoryScreen.renderInventoryComponent(g, getWidth(), getHeight());
            }

            // 3. Held item on cursor — always drawn LAST so it's on top
            if (inventoryScreen != null) {
                inventoryScreen.drawHeldItem(g2);
            }
        } else {
            // Normal mode — just inventory (hotbar or full grid)
            if (inventoryScreen != null) {
                inventoryScreen.renderInventoryComponent(g, getWidth(), getHeight());
                inventoryScreen.drawHeldItem(g2);
            }
        }
    }
}
