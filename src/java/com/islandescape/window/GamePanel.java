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
import com.islandescape.resources.ResourceNode;
import com.islandescape.resources.Stone;
import com.islandescape.resources.Tree;
import com.islandescape.structures.CraftingTable;
import com.islandescape.ui.CraftingScreen;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

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
    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private String farmToastMessage = "";
    private int farmToastFrames = 0;

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

        // MVP test setup: one tree and one stone at fixed world positions.
        resourceNodes.add(new Tree(180, 160));
        resourceNodes.add(new Stone(260, 160));

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

        if (farmToastFrames > 0) {
            farmToastFrames--;
        }

        // Crafting-screen toggle (I key) — single-press consumed once per press
        if (keyHandler.consumeCraftScreenToggle()) {
            if (gameState == GameState.INVENTORY_OPEN) {
                closeCraftingScreen();
            } else if (isAnyPlayerNearTable()) {
                gameState = GameState.INVENTORY_OPEN;
            }
        }

        if (gameState == GameState.PLAYING) {
            if (player1 != null) {
                player1.move(keyHandler.getP1Direction());
            }
            if (player2 != null) {
                player2.move(keyHandler.getP2Direction());
            }

            // Reuse existing action keys: E for P1 and SPACE for P2.
            if (keyHandler.consumeP1Action()) {
                tryFarmNearestResource(player1);
            }
            if (keyHandler.consumeP2Action()) {
                tryFarmNearestResource(player2);
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

    private void tryFarmNearestResource(Player player) {
        if (player == null) {
            return;
        }

        ResourceNode nearest = findNearestResourceInRange(player);
        if (nearest == null) {
            farmToastMessage = "Too far from resource";
            farmToastFrames = 50;
            return;
        }

        List<Item> drops = player.farm(nearest);
        if (drops == null) {
            farmToastMessage = "Need correct tool for " + nearest.getClass().getSimpleName();
            farmToastFrames = 60;
            return;
        }

        for (Item drop : drops) {
            player.getInventory().addItem(drop);
        }

        farmToastMessage = "+ " + formatDrops(drops);
        farmToastFrames = 75;

        System.out.println("[Farm] player=" + player.getName()
                + ", resource=" + nearest.getClass().getSimpleName()
                + ", drops=" + formatDrops(drops));
    }

    private String formatDrops(List<Item> drops) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < drops.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(drops.get(i).getType().name());
        }
        return sb.toString();
    }

    private ResourceNode findNearestResourceInRange(Player player) {
        ResourceNode nearest = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        for (ResourceNode node : resourceNodes) {
            if (!node.isPlayerInRange(player.getX(), player.getY())) {
                continue;
            }

            double dx = node.getX() - player.getX();
            double dy = node.getY() - player.getY();
            double distanceSq = dx * dx + dy * dy;

            if (distanceSq < nearestDistanceSq) {
                nearestDistanceSq = distanceSq;
                nearest = node;
            }
        }

        return nearest;
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
        g.setColor(java.awt.Color.BLACK);
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

        if (farmToastFrames > 0 && !farmToastMessage.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(20, 20, 380, 36, 10, 10);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.setColor(new Color(120, 255, 120));
            g2.drawString(farmToastMessage, 30, 44);
        }
    }
}
