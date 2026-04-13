package com.islandescape.window;

import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.input.InventoryMouseHandler;
import com.islandescape.inventory.InventoryCursor;
import com.islandescape.inventory.InventoryScreen;
import com.islandescape.item.Item;
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
        } else if (gameState == GameState.INVENTORY_OPEN) {
            // Forward input to crafting screen for cursor movement and item placement
            if (craftingScreen != null && craftingSystem != null) {
                boolean p1Near = isPlayerNearTable(player1);
                boolean p2Near = isPlayerNearTable(player2);

                craftingScreen.handleInput(
                        keyHandler.consumeP1Action(), keyHandler.consumeP2Action(),
                        keyHandler.getP1Direction().getX(), keyHandler.getP1Direction().getY(),
                        keyHandler.getP2Direction().getX(), keyHandler.getP2Direction().getY(),
                        p1Near, p2Near,
                        craftingSystem,
                        player1 != null ? player1.getInventory() : null,
                        player2 != null ? player2.getInventory() : null);
            }

            // Craft commit (Enter key) — route output to P1 inventory by default
            if (keyHandler.consumeCraftCommit() && craftingSystem != null && player1 != null) {
                Item result = craftingSystem.craft(player1.getInventory());
                if (result != null) {
                    player1.getInventory().add(result);
                }
            }
        }
    }

    private boolean isAnyPlayerNearTable() {
        return isPlayerNearTable(player1) || isPlayerNearTable(player2);
    }

    private boolean isPlayerNearTable(Player p) {
        return craftingTable != null && p != null
                && craftingTable.isPlayerNearby(p.getX(), p.getY());
    }

    private void closeCraftingScreen() {
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
        g.setColor(new Color(77, 166, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

        map.renderMapComponent(renderer, g, getWidth(), getHeight(), player1, player2);

        // Dev inventory (mouse-driven) overlay
        if (inventoryScreen != null) {
            inventoryScreen.renderInventoryComponent(g, getWidth(), getHeight());
        }

        // Crafting overlay when open (keyboard-driven)
        if (gameState == GameState.INVENTORY_OPEN && craftingScreen != null) {
            Graphics2D g2 = (Graphics2D) g;
            craftingScreen.render(g2, getWidth(), getHeight(),
                    craftingSystem,
                    player1 != null ? player1.getInventory() : null,
                    player2 != null ? player2.getInventory() : null,
                    isPlayerNearTable(player1), isPlayerNearTable(player2));
        }
    }
}
