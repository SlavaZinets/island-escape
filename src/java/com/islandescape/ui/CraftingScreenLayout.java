package com.islandescape.ui;

/**
 * Pure layout math for the crafting screen — no Graphics dependency.
 * Given screen dimensions and grid constants, computes every position
 * that the renderer needs.
 */
public class CraftingScreenLayout {

    // Grid / inventory dimensions (cells)
    static final int GRID_COLS = 2;
    static final int GRID_ROWS = 2;
    static final int INV_COLS = 5;
    static final int INV_ROWS = 2;

    private static final int MAX_SLOT_SIZE = 64;
    private static final int MIN_SLOT_SIZE = 32;
    private static final int SECTION_GAP = 24;
    private static final int INV_LABEL_HEIGHT = 22;
    private static final int CONTENT_TOP_OFFSET = 25; // vertical nudge

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

    // Player inventories
    public int invY;
    public int p1InvX, p2InvX;
    public int invLabelHeight;

    // Controls hint
    public int hintY;

    public CraftingScreenLayout(int screenW, int screenH) {
        compute(screenW, screenH);
    }

    private void compute(int screenW, int screenH) {
        // Panel rectangle
        panelW = (int) (screenW * 0.75);
        panelH = (int) (screenH * 0.85);
        panelX = (screenW - panelW) / 2;
        panelY = (screenH - panelH) / 2;

        // Content area inside wooden borders
        int borderL = (int) (panelW * 0.10);
        int borderR = (int) (panelW * 0.10);
        int borderT = (int) (panelH * 0.14);
        int borderB = (int) (panelH * 0.12);
        contentX = panelX + borderL;
        contentY = panelY + borderT;
        contentW = panelW - borderL - borderR;
        contentH = panelH - borderT - borderB;
        centerX = contentX + contentW / 2;

        // Slot sizing — fit two 5-col inventories side by side
        int availSlotSize = Math.min(MAX_SLOT_SIZE, contentW / (INV_COLS * 2 + 3));
        slotSize = Math.max(MIN_SLOT_SIZE, availSlotSize);
        slotGap = slotSize / 8;

        // Title
        titleY = contentY + 18 + CONTENT_TOP_OFFSET;

        // Crafting grid row
        gridTotalW = GRID_COLS * slotSize + (GRID_COLS - 1) * slotGap;
        gridTotalH = GRID_ROWS * slotSize + (GRID_ROWS - 1) * slotGap;
        arrowW = slotSize * 3 / 4;
        resultSlotSize = slotSize + 12;
        int gap = slotSize / 3;
        int craftingRowW = gridTotalW + gap + arrowW + gap + resultSlotSize;
        craftingStartX = centerX - craftingRowW / 2;
        gridY = contentY + 30 + CONTENT_TOP_OFFSET;

        arrowX = craftingStartX + gridTotalW + gap;
        arrowY = gridY + gridTotalH / 2 - arrowW / 2;

        resultX = arrowX + arrowW + gap;
        resultY = gridY + gridTotalH / 2 - resultSlotSize / 2;

        // Inventories
        invY = gridY + gridTotalH + SECTION_GAP;
        int invTotalW = INV_COLS * slotSize + (INV_COLS - 1) * slotGap;
        int invSectionGap = slotSize / 2;
        p1InvX = centerX - invTotalW - invSectionGap / 2;
        p2InvX = centerX + invSectionGap / 2;
        invLabelHeight = INV_LABEL_HEIGHT;

        // Hint
        hintY = contentY + contentH - 5;
    }
}
