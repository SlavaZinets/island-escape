package com.islandescape.window;

import com.islandescape.input.GameKeyHandler;
import com.islandescape.input.InventoryMouseHandler;
import com.islandescape.inventory.InventoryCursor;
import com.islandescape.inventory.InventoryScreen;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;

import javax.swing.JPanel;
import java.awt.Graphics;

/*
    GamePanel sits inside GameWindow.
    It holds the TileMap and MapRenderer, and paints the map every frame.
    Renders the map at native resolution, then scales to fit the panel.
 */
public class GamePanel extends JPanel {

    private final TileMap map;
    private final MapRenderer renderer;

    // players and inventory
    private Player player1;
    private Player player2;
    private InventoryScreen inventoryScreen;

    public GamePanel(TileMap map, MapRenderer renderer) {
        this.map = map;
        this.renderer = renderer;
    }

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
    }

    private GameKeyHandler keyHandler;

    public void setKeyHandler(GameKeyHandler handler) {
        if (this.keyHandler != null) {
            removeKeyListener(this.keyHandler);
        }
        this.keyHandler = handler;
        addKeyListener(handler);
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
        map.renderMapComponent(renderer, g, map);

        // inventory UI
        if (inventoryScreen != null) {
            inventoryScreen.renderInventoryComponent(g, getWidth(), getHeight());
        }
    }
}
