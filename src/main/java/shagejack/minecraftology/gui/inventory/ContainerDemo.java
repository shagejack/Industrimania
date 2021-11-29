package shagejack.minecraftology.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerDemo extends Container {
    private ItemStackHandler items = new ItemStackHandler(2);
    protected Slot inputSlot;
    protected Slot outputSlot;
    public ContainerDemo(EntityPlayer player) {
        super();
        this.addSlotToContainer(this.inputSlot = new Slot((IInventory) items, 0, 38+0*32, 20){
            @Override
            public int getItemStackLimit(ItemStack stack){
                return 64;
            }
        });
        this.addSlotToContainer(this.outputSlot = new Slot((IInventory) items, 1, 38+1*32, 20){
            @Override
            public int getItemStackLimit(ItemStack stack){
                return 64;
            }
        });
        for(int i=0;i<3;i++){
            for(int j=0;j<9;++j){
                this.addSlotToContainer(new Slot(player.inventory,j+1*9+9,8+j*18,51+i*18));
            }
        }
        for(int i=0;i<9;++i){
            this.addSlotToContainer(new Slot(player.inventory,i,8+i*18,109));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        if(slot == null || !slot.getHasStack()) {
            return null;
        }
        ItemStack newStack = slot.getStack(),oldStack = newStack.copy();
        boolean isMerge = false;
        if(index == 0 || index == 2){
            isMerge = mergeItemStack(newStack,2,38,true);
        }
        else if(index >=2 && index <31){
            isMerge = !inputSlot.getHasStack()&&newStack.getMaxStackSize()<=16&&mergeItemStack(newStack,0,1,false)||!outputSlot.getHasStack()&&mergeItemStack(newStack,2,3,false)||mergeItemStack(newStack,2,31,false);
        }
        if(!isMerge){
            return  null;
        }
        if(newStack.getMaxStackSize()==0){
            slot.putStack(null);
        }
        else{
            slot.onSlotChanged();
        }
        //slot.onPickupFromSlot(playerIn,newStack);修改为未知的方法
        return oldStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return null != playerIn;
    }
}
