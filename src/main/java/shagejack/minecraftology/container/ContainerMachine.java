package shagejack.minecraftology.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.api.container.IMachineWatcher;
import shagejack.minecraftology.container.slot.SlotInventory;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.tile.MCLTileEntityContainer;
import shagejack.minecraftology.util.MCLInventoryHelper;

import javax.annotation.Nonnull;

public class ContainerMachine<T extends MCLTileEntityContainer> extends MCLBaseContainer implements IMachineWatcher {
    protected T machine;
    EntityPlayer entityPlayer;
    private int progressScaled;

    public ContainerMachine() {
        super();
    }

    public ContainerMachine(InventoryPlayer inventory, T machine) {
        super(inventory);
        this.machine = machine;
        entityPlayer = inventory.player;
        init(inventory);
    }

    protected void init(InventoryPlayer inventory) {

    }

    /*
    @Override
    public void addListener(IContainerListener icrafting) {
        super.addListener(icrafting);
        if (icrafting instanceof EntityPlayerMP) {
            machine.addWatcher(this);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        if (playerIn instanceof EntityPlayerMP) {
            machine.removeWatcher(this);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.machine != null) {
            short progressScaled = (short) (this.machine.getProgress() * Short.MAX_VALUE);
            for (IContainerListener listener : this.listeners) {
                if (this.progressScaled != progressScaled) {
                    listener.sendWindowProperty(this, 0, progressScaled);
                }
            }

            this.progressScaled = progressScaled;
        }
    }

     */

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue) {
        if (slot == 0) {
            this.progressScaled = newValue;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public void addAllSlotsFromInventory(Inventory inventory) {
        for (shagejack.minecraftology.data.inventory.Slot slot : inventory.getSlots()) {
            addSlotToContainer(new SlotInventory(inventory, inventory.getSlot(slot.getId()), 0, 0));
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotID);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (slotID < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    protected boolean putInPlayerInventory(ItemStack itemStack) {
        return MCLInventoryHelper.mergeItemStack(inventorySlots, itemStack, machine.getSizeInventory(), inventorySlots.size() - machine.getSizeInventory(), true, true);
    }

    protected boolean tryAndPutInMachineSlots(ItemStack itemStack, IInventory inventory) {
        return MCLInventoryHelper.mergeItemStack(inventorySlots, itemStack, 0, inventory.getSizeInventory(), false, true);
    }

    public T getMachine() {
        return machine;
    }

    public float getProgress() {
        return (float) progressScaled / (float) Short.MAX_VALUE;
    }

    @Override
    public EntityPlayer getPlayer() {
        return entityPlayer;
    }

    @Override
    public void onWatcherAdded(MCLTileEntityMachine machine) {

    }

    @Override
    public boolean isWatcherValid() {
        if (entityPlayer instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) entityPlayer).getServerWorld().getPlayerEntityByUUID(entityPlayer.getUniqueID()) != null;
        }
        return false;
    }
}
