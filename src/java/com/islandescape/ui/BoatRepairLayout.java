package com.islandescape.ui;


public class BoatRepairLayout {

    // 6 fixed-purpose slots: PLANK, MAST, FRAME, SAIL, RUDDER, FITTINGS
    public static final int SLOT_COUNT = 6;

    private static final int MAX_SLOT_SIZE = 64;
    private static final int MIN_SLOT_SIZE = 32;

    // Inventory constants (matching InventoryScreen / CraftingScreenLayout)
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

    // Repair slots row (6 slots horizontally)
    public int repairSlotsStartX, repairSlotsY;
    public int repairRowW;

    // Progress bar (below the repair slots)
    public int progressBarX, progressBarY, progressBarW, progressBarH;

    // Inventory area (same geometry as CraftingScreenLayout for consistency)
    public int invAreaTop;
    public int invP1Left;
    public int invP2Left;
    public int invXBorder;

    // Controls hint line
    public int hintY;

    public BoatRepairLayout(int screenW, int screenH) {
        compute(screenW, screenH);
    }

    private void compute(int screenW, int screenH) {
        // Inventory dimensions — panel must fit them
        int oneInvGridW = INV_COLS * INV_SLOT_SIZE + (INV_COLS - 1) * INV_SLOT_GAP;
        int totalInvW = 2 * oneInvGridW + INV_SEPARATOR;

        int borderPad = 50;
        panelW = Math.max((int) (screenW * 0.60), totalInvW + borderPad * 2);
        panelH = (int) (screenH * 0.88);
        panelX = (screenW - panelW) / 2;
        panelY = (screenH - panelH) / 2;

        contentX = panelX + borderPad;
        contentY = panelY + 30;
        contentW = panelW - borderPad * 2;
        contentH = panelH - 60;
        centerX = contentX + contentW / 2;

        // Slot sizing: fit all 6 slots across the content width with gaps
        int availSlotSize = Math.min(MAX_SLOT_SIZE, (contentW - (SLOT_COUNT - 1) * 8) / SLOT_COUNT);
        slotSize = Math.max(MIN_SLOT_SIZE, availSlotSize);
        slotGap = slotSize / 8;

        titleY = contentY + 28;

        // 6-slot repair row, centered
        repairRowW = SLOT_COUNT * slotSize + (SLOT_COUNT - 1) * slotGap;
        repairSlotsStartX = centerX - repairRowW / 2;
        repairSlotsY = contentY + 45;

        // Progress bar directly below the slot row
        progressBarW = repairRowW;
        progressBarH = 18;
        progressBarX = repairSlotsStartX;
        progressBarY = repairSlotsY + slotSize + 20;

        // Inventory area — below the progress bar, same widths as CraftingScreenLayout
        invAreaTop = progressBarY + progressBarH + 40;
        invP1Left = centerX - totalInvW / 2;
        invP2Left = invP1Left + oneInvGridW + INV_SEPARATOR;
        invXBorder = invP1Left + oneInvGridW + INV_SEPARATOR / 2;

        hintY = contentY + contentH - 10;
    }
}
