package shagejack.industrimania.content.primalAge.block.dryingRack;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class DryingRackRenderer extends SafeTileEntityRenderer<DryingRackTileEntity> {

	public DryingRackRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(DryingRackTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
		int overlay) {
		renderItems(te, partialTicks, ms, buffer, light, overlay);
	}

	protected void renderItems(DryingRackTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.inventory.isEmpty()) {

				ItemStack stack = te.inventory.getStackInSlot(0);
				if (!stack.isEmpty()) {

					ms.pushPose();

					ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
					BakedModel modelWithOverrides = itemRenderer.getModel(stack, te.getLevel(), null, 0);
					boolean blockItem = modelWithOverrides.isGui3d();

					ms.translate(.5, blockItem ? .925f : 13f / 16f, .5);

					ms.scale(.5f, .5f, .5f);
					ms.mulPose(Vector3f.XP.rotationDegrees(90));
					itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);

					ms.popPose();

				}
		}
	}

}
