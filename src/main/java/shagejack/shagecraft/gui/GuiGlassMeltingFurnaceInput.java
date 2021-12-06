package shagejack.shagecraft.gui;

import net.minecraft.entity.player.InventoryPlayer;
import shagejack.shagecraft.container.ContainerGlassMeltingFurnaceInput;
import shagejack.shagecraft.gui.element.ElementInventorySlot;
import shagejack.shagecraft.gui.element.ElementSlot;
import shagejack.shagecraft.tile.TileEntityGlassMeltingFurnaceInput;

public class GuiGlassMeltingFurnaceInput extends ShageGuiMachine<TileEntityGlassMeltingFurnaceInput> {
    ElementSlot fuelSlot;

    public GuiGlassMeltingFurnaceInput(InventoryPlayer inventoryPlayer, TileEntityGlassMeltingFurnaceInput machine) {
        super(new ContainerGlassMeltingFurnaceInput(inventoryPlayer, machine), machine);
        name = "glass_melting_furnace_input";
        fuelSlot = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityGlassMeltingFurnaceInput.fuel_slot), 129, 55, 22, 22, "big");

    }

    @Override
    public void initGui() {
        super.initGui();

        pages.get(0).addElement(fuelSlot);
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
