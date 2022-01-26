package shagejack.industrimania.foundation.utility;

import net.minecraft.nbt.CompoundTag;

public interface IPartialSafeNBT {
	void writeSafe(CompoundTag compound, boolean clientPacket);
}
