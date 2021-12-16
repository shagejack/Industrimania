package shagejack.shagecraft.gui;

import net.minecraft.entity.player.InventoryPlayer;
import shagejack.shagecraft.container.ContainerBoilerHeater;
import shagejack.shagecraft.container.ContainerGlassMeltingFurnaceInput;
import shagejack.shagecraft.gui.element.ElementInventorySlot;
import shagejack.shagecraft.gui.element.ElementSlot;
import shagejack.shagecraft.tile.TileEntityBoilerHeater;
import shagejack.shagecraft.tile.TileEntityGlassMeltingFurnaceInput;

public class GuiBoilerHeater extends ShageGuiMachine<TileEntityBoilerHeater> {
    ElementSlot fuelSlotA;
    ElementSlot fuelSlotB;
    ElementSlot fuelSlotC;
    ElementSlot fuelSlotD;
    ElementSlot fuelSlotE;

    public GuiBoilerHeater(InventoryPlayer inventoryPlayer, TileEntityBoilerHeater machine) {
        super(new ContainerBoilerHeater(inventoryPlayer, machine), machine);
        name = "boiler_heater";
        fuelSlotA = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityBoilerHeater.fuel_slots[0]), 69, 55, 22, 22, "big");
        fuelSlotB = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityBoilerHeater.fuel_slots[1]), 92, 55, 22, 22, "big");
        fuelSlotC = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityBoilerHeater.fuel_slots[2]), 115, 55, 22, 22, "big");
        fuelSlotD = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityBoilerHeater.fuel_slots[3]), 138, 55, 22, 22, "big");
        fuelSlotE = new ElementInventorySlot(this, getContainer().getSlotAt(TileEntityBoilerHeater.fuel_slots[4]), 161, 55, 22, 22, "big");
    }

    @Override
    public void initGui() {
        super.initGui();

        pages.get(0).addElement(fuelSlotA);
        pages.get(0).addElement(fuelSlotB);
        pages.get(0).addElement(fuelSlotC);
        pages.get(0).addElement(fuelSlotD);
        pages.get(0).addElement(fuelSlotE);
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
