package shagejack.minecraftology.gui;

import net.minecraft.entity.player.InventoryPlayer;
import shagejack.minecraftology.Reference;
import shagejack.minecraftology.container.ContainerForgeFurnace;
import shagejack.minecraftology.container.ContainerMachine;
import shagejack.minecraftology.gui.element.ElementInventorySlot;
import shagejack.minecraftology.gui.element.ElementSlot;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;

public class GuiForgeFurnace extends MCLGuiMachine<TileEntityForgeFurnace> {
    ElementSlot fuelSlot;
    ElementSlot inputSlot;

    public GuiForgeFurnace(InventoryPlayer inventoryPlayer, TileEntityForgeFurnace machine) {
        super(new ContainerForgeFurnace(inventoryPlayer, machine), machine);
        name = "forge_furnace";
        fuelSlot = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityForgeFurnace.FUEL_SLOT_ID), 129, 55, 22, 22, "big");
        inputSlot = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityForgeFurnace.INPUT_SLOT_ID), 129, 25, 22, 22, "big");

    }

    @Override
    public void initGui() {
        super.initGui();

        pages.get(0).addElement(fuelSlot);
        pages.get(0).addElement(inputSlot);
        //this.addElement(inscribe_progress);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
        //inscribe_progress.setQuantity(Math.round((((ContainerMachine) getContainer()).getProgress() * 24)));
    }
}
