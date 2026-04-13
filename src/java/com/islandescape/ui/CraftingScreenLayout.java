package com.islandescape.ui;

/**
 * Pure layout math for the crafting screen — no Graphics dependency.
 * Given screen dimensions and grid constants, computes every position
 * that the renderer needs.
 */
public class CraftingScreenLayout {

    // Grid / inventory dimensions (cells)
    public static final int GRID_COLS = 2;
    public static final int GRID_ROWS = 2;
    private static final int MAX_SLOT_SIZE = 64;
    private static final int MIN_SLOT_SIZE = 32;
    private static final int CONTENT_TOP_OFFSET = 25; // vertical nudge

    // Inventory constants (matching InventoryScreen)
    private static final int INV_COLS = 5;
    private static final int INV_ROWS = 4;
    private static final int INV_SLOT_SIZE = 64;
    private static final int INV_SLOT_GAP = 12;
    private static final int INV_SEPARATOR = 30;

    // Panel
    public int panelX, panelY, panelW, panelH;

    // Content area (inside panel borders)
    public int contentX, contentY, contentW, contentH;
    public int centerX;

    // Computed slot metrics
    public int slotSize;
    public int slotGap;

    // Title
    public int titleY;

    // Crafting grid + arrow + result
    public int craftingStartX, gridY;
    public int gridTotalW, gridTotalH;
    public int arrowX, arrowY, arrowW;
    public int resultX, resultY, resultSlotSize;

    // Inventory area (below crafting grid, inside the same panel)
    public int invAreaTop;
    public int invP1Left;
    public int invP2Left;
    public int invXBorder;

    // Controls hint
    public int hintY;

    public CraftingScreenLayout(int screenW, int screenH) {
        compute(screenW, screenH);
    }

    private void compute(int screenW, int screenH) {
        // Compute inventory dimensions first — the panel must fit them
        int oneInvGridW = INV_COLS * INV_SLOT_SIZE + (INV_COLS - 1) * INV_SLOT_GAP;
        int totalInvW = 2 * oneInvGridW + INV_SEPARATOR;
        int invGridH = INV_ROWS * INV_SLOT_SIZE + (INV_ROWS - 1) * INV_SLOT_GAP;

        // Panel rectangle — sized to hold both crafting and inventory
        int borderPad = 50; // padding inside panel on each side
        panelW = Math.max((int) (screenW * 0.60), totalInvW + borderPad * 2);
        panelH = (int) (screenH * 0.88);
        panelX = (screenW - panelW) / 2;
        panelY = (screenH - panelH) / 2;

        // Content area inside panel borders
        contentX = panelX + borderPad;
        contentY = panelY + 30;
        contentW = panelW - borderPad * 2;
        contentH = panelH - 60;
        centerX = contentX + contentW / 2;

        // Slot sizing for crafting grid
        int availSlotSize = Math.min(MAX_SLOT_SIZE, contentW / (GRID_COLS * 3 + 3));
        slotSize = Math.max(MIN_SLOT_SIZE, availSlotSize);
        slotGap = slotSize / 8;

        // Title
        titleY = contentY + 28;

        // Crafting grid row
        gridTotalW = GRID_COLS * slotSize + (GRID_COLS - 1) * slotGap;
        gridTotalH = GRID_ROWS * slotSize + (GRID_ROWS - 1) * slotGap;
        arrowW = slotSize * 3 / 4;
        resultSlotSize = slotSize + 12;
        int gap = slotSize / 3;
        int craftingRowW = gridTotalW + gap + arrowW + gap + resultSlotSize;
        craftingStartX = centerX - craftingRowW / 2;
        gridY = contentY + 45;

        arrowX = craftingStartX + gridTotalW + gap;
        arrowY = gridY + gridTotalH / 2 - arrowW / 2;

        resultX = arrowX + arrowW + gap;
        resultY = gridY + gridTotalH / 2 - resultSlotSize / 2;

        // Inventory area — positioned below the crafting grid, centered in panel
        invAreaTop = gridY + gridTotalH + 40;
        invP1Left = centerX - totalInvW / 2;
        invP2Left = invP1Left + oneInvGridW + INV_SEPARATOR;
        invXBorder = invP1Left + oneInvGridW + INV_SEPARATOR / 2;

        // Hint
        hintY = contentY + contentH - 10;
    }
}
