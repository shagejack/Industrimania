package shagejack.industrimania.foundation.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

interface IItemHandlerModifiableIntermediate extends IItemHandlerModifiable {

	@Override
	default ItemStack getStackInSlot(int slot) {
		return getStackInSlotIntermediate(slot);
	}

	ItemStack getStackInSlotIntermediate(int slot);

}