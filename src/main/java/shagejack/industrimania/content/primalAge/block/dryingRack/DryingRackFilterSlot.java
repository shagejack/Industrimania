package shagejack.industrimania.content.primalAge.block.dryingRack;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import shagejack.industrimania.foundation.tileEntity.behaviour.ValueBoxTransform;
import shagejack.industrimania.foundation.utility.VecHelper;

public class DryingRackFilterSlot extends ValueBoxTransform {

	@Override
	protected Vec3 getLocalOffset(BlockState state) {
		return VecHelper.voxelSpace(12.25f, 12.5f, 8f);
	}

	@Override
	protected void rotate(BlockState state, PoseStack ms) {
	}

}