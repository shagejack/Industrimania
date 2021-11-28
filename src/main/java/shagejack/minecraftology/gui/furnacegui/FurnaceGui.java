package shagejack.minecraftology.gui.furnacegui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FurnaceGui extends Container {
    public FurnaceGui(EntityPlayer player) {
        super();

        ItemStackHandler items = new ItemStackHandler(2);

        for(int i=0;i<2;i++){
            this.addSlotToContainer(new SlotItemHandler(items,i,38+i*32,20));
        }
        for(int i=0;i<1;++i) {
            for(int j=0;j<9;++j) {
                this.addSlotToContainer(new Slot(player.inventory,i,8+i*18,109));
            }
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
        //return new ItemStack().isItemEqual(playerIn.getCurrentEquippedItem());
    }
}
