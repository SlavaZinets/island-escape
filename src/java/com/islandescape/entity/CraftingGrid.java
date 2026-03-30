package com.islandescape.entity;

public class CraftingGrid {
	private static final int SLOT_COUNT = 4;

	// four craft slots
	private final Item[] slots;

	public CraftingGrid() {
		slots = new Item[SLOT_COUNT];
	}

	public int getSlotCount() {
		return SLOT_COUNT;
	}

	public Item getSlot(int index) {
		validate(index);
		return slots[index];
	}

	public void setSlot(int index, Item item) {
		validate(index);
		slots[index] = item;
	}

	private void validate(int index) {
		if (index < 0 || index >= SLOT_COUNT) {
			throw new IllegalArgumentException("Slot index must be from 0 to 3");
		}
	}
}
