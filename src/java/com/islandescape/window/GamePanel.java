package com.islandescape.window;

import com.islandescape.boat.BoatWreck;
import com.islandescape.crafting.CraftingSystem;
import com.islandescape.input.GameKeyHandler;
import com.islandescape.input.InventoryMouseHandler;
import com.islandescape.inventory.InventoryCursor;
import com.islandescape.inventory.InventoryScreen;
import com.islandescape.item.Item;
import com.islandescape.item.ItemCategory;
import com.islandescape.item.ItemType;
import com.islandescape.map.MapRenderer;
import com.islandescape.map.TileMap;
import com.islandescape.player.Player;
import com.islandescape.resources.ResourceNode;
import com.islandescape.resources.ResourceSpawner;
import com.islandescape.save.SaveData;
import com.islandescape.save.SaveManager;
import com.islandescape.structures.CraftingTable;
import com.islandescape.ui.BoatRepairScreen;
import com.islandescape.ui.CraftingScreen;
import com.islandescape.ui.MainMenu;
import com.islandescape.ui.WinOverlay;

import javax.swing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private BoatWreck boatWreck;
    private BoatRepairScreen boatRepairScreen;
    private boolean boatRepairOpen = false;
    private final WinOverlay winOverlay = new WinOverlay();
    private final MainMenu mainMenu = new MainMenu();
    private GameState gameState = GameState.MAIN_MENU;
    private Runnable quitAction = () -> System.exit(0);
    private boolean quitRequested = false;
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

        resourceNodes.addAll(ResourceSpawner.spawnFromMap(map));

        mainMenu.setOnNewGame(this::startNewGame);
        mainMenu.setOnLoadGame(this::loadGame);
        mainMenu.setOnQuit(this::quitGame);
        refreshLoadMenuAvailability();

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

    public void setBoatWreck(BoatWreck boatWreck) {
        this.boatWreck = boatWreck;
        if (inventoryScreen != null && boatWreck != null) {
            inventoryScreen.setBoatRepairSystem(boatWreck.getRepairSystem());
        }
    }

    public void setBoatRepairScreen(BoatRepairScreen boatRepairScreen) {
        this.boatRepairScreen = boatRepairScreen;
    }

    public BoatWreck getBoatWreck() {
        return boatWreck;
    }

    public boolean isBoatRepairOpen() {
        return boatRepairOpen;
    }

    public GameState getGameState() {
        return gameState;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public boolean isQuitRequested() {
        return quitRequested;
    }

    public List<ResourceNode> getResourceNodes() {
        return resourceNodes;
    }

    public void setQuitAction(Runnable quitAction) {
        this.quitAction = quitAction;
    }

    public void startNewGame() {
        resetToFresh();
        if (player1 != null) {
            player1.getInventory().addItem(
                new Item(ItemType.PICKAXE, ItemCategory.TOOL, "Pickaxe", "Mines stone", 1));
        }
        if (player2 != null) {
            player2.getInventory().addItem(
                new Item(ItemType.AXE, ItemCategory.TOOL, "Axe", "Chops trees for wood", 1));
        }
        gameState = GameState.PLAYING;
    }

    public void resetToFresh() {
        if (player1 != null) {
            player1.setPosition(320, 192);
            player1.getInventory().clear();
        }
        if (player2 != null) {
            player2.setPosition(352, 192);
            player2.getInventory().clear();
        }
        if (boatWreck != null) {
            boatWreck.reset();
        }
        resourceNodes.clear();
        if (map != null) {
            resourceNodes.addAll(ResourceSpawner.spawnFromMap(map));
        }
    }

    public void quitGame() {
        quitRequested = true;
        if (quitAction != null) {
            quitAction.run();
        }
    }

    public void loadGame() {
        if (!SaveManager.hasSave()) return;
        SaveData data;
        try {
            data = SaveManager.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load save", e);
        }
        SaveData.apply(data, player1, player2, boatWreck, resourceNodes);

        // Close any panels that may have been left open by the previous session.
        boatRepairOpen = false;
        if (inventoryScreen != null) {
            inventoryScreen.setBoatRepairOpen(false);
            inventoryScreen.setCraftingOpen(false);
            inventoryScreen.setOpen(false);
        }
        gameState = GameState.PLAYING;
    }

    public void onSaveKey() {
        SaveData data = SaveData.capture(player1, player2, boatWreck, resourceNodes);
        SaveManager.save(data);
        refreshLoadMenuAvailability();
        farmToastMessage = "Saved";
        farmToastFrames = 75;
    }

    public void refreshLoadMenuAvailability() {
        mainMenu.setLoadEnabled(SaveManager.hasSave());
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

        // MAIN_MENU: only menu navigation runs. Drain other edges so they
        // don't fire on the first frame after transitioning to PLAYING.
        if (gameState == GameState.MAIN_MENU) {
            if (keyHandler.consumeMenuUp())      mainMenu.moveUp();
            if (keyHandler.consumeMenuDown())    mainMenu.moveDown();
            if (keyHandler.consumeMenuConfirm()) mainMenu.confirm();

            keyHandler.consumeCraftScreenToggle();
            keyHandler.consumeCraftCommit();
            keyHandler.consumeBoatRepairToggle();
            keyHandler.consumeBoardKey();
            keyHandler.consumeP1Action();
            keyHandler.consumeP2Action();
            keyHandler.consumeP1Gather();
            keyHandler.consumeP2Gather();
            return;
        }

        // GAME_WON freezes everything — input, movement, farming.
        if (gameState == GameState.GAME_WON) {
            // Drain any stale key edges so they don't fire post-reset.
            keyHandler.consumeCraftScreenToggle();
            keyHandler.consumeBoatRepairToggle();
            keyHandler.consumeBoardKey();
            keyHandler.consumeP1Gather();
            keyHandler.consumeP2Gather();
            keyHandler.consumeP1Action();
            keyHandler.consumeP2Action();
            return;
        }

        if (farmToastFrames > 0) {
            farmToastFrames--;
        }

        for (ResourceNode node : resourceNodes) {
            node.tick();
        }

        // Crafting-screen toggle (I key) — single-press consumed once per press.
        // Ignored while the boat-repair panel is open.
        if (keyHandler.consumeCraftScreenToggle()) {
            if (boatRepairOpen) {
                // do nothing — boat repair owns the screen
            } else if (gameState == GameState.INVENTORY_OPEN) {
                closeCraftingScreen();
            } else if (isAnyPlayerNearTable()) {
                gameState = GameState.INVENTORY_OPEN;
                if (inventoryScreen != null) {
                    inventoryScreen.setCraftingOpen(true);
                    inventoryScreen.setOpen(true);
                }
            }
        }

        // Boat-repair toggle (R key). Ignored while crafting is the open panel.
        if (keyHandler.consumeBoatRepairToggle()) {
            if (boatRepairOpen) {
                closeBoatRepairScreen();
            } else if (gameState == GameState.PLAYING && isAnyPlayerNearBoat()) {
                openBoatRepairScreen();
            }
            // else: crafting is open, or no player is near the boat — ignore.
        }

        // Boarding (B key): global — whichever players are currently within
        // range of the fully-repaired boat are boarded.
        if (keyHandler.consumeBoardKey()) {
            onBoardKey();
        }

        // Save hotkey (F5). Edge is only set in PLAYING (gated in GameKeyHandler).
        if (keyHandler.consumeSaveKey()) {
            onSaveKey();
        }

        if (gameState == GameState.PLAYING) {
            if (player1 != null) {
                player1.move(keyHandler.getP1Direction());
            }
            if (player2 != null) {
                player2.move(keyHandler.getP2Direction());
            }

            // Gather keys: G for P1, M for P2.
            if (keyHandler.consumeP1Gather()) {
                tryFarmNearestResource(player1);
            }
            if (keyHandler.consumeP2Gather()) {
                tryFarmNearestResource(player2);
            }
        }
        // When INVENTORY_OPEN, all crafting / boat interaction is mouse-driven
        // (handled by InventoryScreen.handleClick)

        // Update proximity flags for inventory blocking
        if (gameState == GameState.INVENTORY_OPEN && inventoryScreen != null) {
            if (boatRepairOpen) {
                inventoryScreen.setPlayerNearBoat(
                        isPlayerNearBoat(player1), isPlayerNearBoat(player2));
            } else {
                inventoryScreen.setPlayerNearTable(
                        isPlayerNearTable(player1), isPlayerNearTable(player2));
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
        nearest.disable();

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
            if (node.isDisabled()) {
                continue;
            }
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

    private boolean isAnyPlayerNearBoat() {
        return isPlayerNearBoat(player1) || isPlayerNearBoat(player2);
    }

    private boolean isPlayerNearBoat(Player p) {
        return boatWreck != null && p != null
                && boatWreck.isPlayerInRange(p.getX(), p.getY());
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

    // Opens the boat-repair panel. Items already deposited on the boat are
    // preserved — they are stored in boatWreck.getRepairSystem(), never
    // touched here. Caller is expected to have verified a player is in range.
    public void openBoatRepairScreen() {
        if (boatWreck == null) return;
        gameState = GameState.INVENTORY_OPEN;
        boatRepairOpen = true;
        if (inventoryScreen != null) {
            inventoryScreen.setBoatRepairSystem(boatWreck.getRepairSystem());
            inventoryScreen.setBoatRepairOpen(true);
            inventoryScreen.setOpen(true);
            inventoryScreen.setPlayerNearBoat(
                    isPlayerNearBoat(player1), isPlayerNearBoat(player2));
        }
    }

    // Closes the boat-repair panel. Deposited items STAY on the boat —
    // only the held cursor item (if any) is returned to a player's inventory.
    public void closeBoatRepairScreen() {
        if (inventoryScreen != null) {
            inventoryScreen.returnHeldItem();
            inventoryScreen.setBoatRepairOpen(false);
            inventoryScreen.setOpen(false);
        }
        boatRepairOpen = false;
        gameState = GameState.PLAYING;
    }

    // Handles a single press of the 'B' key. For each player currently within
    // boat range, ask the boat to board them; if both end up boarded on a
    // fully-repaired boat, transition to GAME_WON. Works in both PLAYING and
    // INVENTORY_OPEN (boat repair) states.
    public void onBoardKey() {
        if (boatWreck == null) return;
        if (!boatWreck.isFullyRepaired()) return;

        // Player ids in Main are 1 and 2; BoatWreck.boardedPlayers uses 0 and 1.
        if (player1 != null && isPlayerNearBoat(player1)) {
            boatWreck.board(player1.getId() - 1, player1.getX(), player1.getY());
        }
        if (player2 != null && isPlayerNearBoat(player2)) {
            boatWreck.board(player2.getId() - 1, player2.getX(), player2.getY());
        }

        if (boatWreck.bothBoarded()) {
            // Tear down any open panels first so overlays don't linger.
            boatRepairOpen = false;
            if (inventoryScreen != null) {
                inventoryScreen.setBoatRepairOpen(false);
                inventoryScreen.setOpen(false);
            }
            gameState = GameState.GAME_WON;
        }
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

        // Main menu owns the screen
        if (gameState == GameState.MAIN_MENU) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            mainMenu.render(g2, getWidth(), getHeight());
            return;
        }

        g.setColor(new Color(77, 166, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

        Set<Point> disabledTiles = ResourceSpawner.disabledTileCoords(resourceNodes);
        map.renderMapComponent(renderer, g, getWidth(), getHeight(), player1, player2, disabledTiles);

        if (gameState == GameState.GAME_WON) {
            winOverlay.render(g2, getWidth(), getHeight());
            return;
        }

        if (gameState == GameState.INVENTORY_OPEN && boatRepairOpen && boatRepairScreen != null) {
            // 1. Boat repair panel (dim overlay + brown background + repair slots + progress bar)
            boatRepairScreen.render(g2, getWidth(), getHeight(),
                    boatWreck != null ? boatWreck.getRepairSystem() : null);

            // 2. Inventory grids (drawn ON TOP of the brown panel)
            if (inventoryScreen != null) {
                inventoryScreen.renderInventoryComponent(g, getWidth(), getHeight());
            }

            // 3. Held item on cursor — always drawn LAST so it's on top
            if (inventoryScreen != null) {
                inventoryScreen.drawHeldItem(g2);
            }
        } else if (gameState == GameState.INVENTORY_OPEN && craftingScreen != null) {
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

        if (farmToastFrames > 0 && !farmToastMessage.isEmpty()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(20, 20, 380, 36, 10, 10);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.setColor(new Color(120, 255, 120));
            g2.drawString(farmToastMessage, 30, 44);
        }
    }

}
