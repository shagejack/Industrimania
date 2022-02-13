package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackTileEntity;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import shagejack.industrimania.registers.item.AllItems;

public class ItemPlaceableRenderer extends SafeTileEntityRenderer<ItemPlaceableBaseTileEntity> {

	public ItemPlaceableRenderer(BlockEntityRendererProvider.Context context) {}

	private final ItemStack stackTest = new ItemStack(AllItems.modelTest.get());

	@Override
	protected void renderSafe(ItemPlaceableBaseTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
		int overlay) {
		//renderItems(te, partialTicks, ms, buffer, light, overlay);
	}

	protected void renderItems(ItemPlaceableBaseTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.inventory.isEmpty()) {

			for (int i = 0; i < te.inventory.getSlots(); i++) {
				ItemStack stack = te.inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {

					ms.pushPose();

					ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

					//TODO: render items
					itemRenderer.renderStatic(stackTest, ItemTransforms.TransformType.FIXED, 240, overlay, ms, buffer, 0);

					ms.popPose();

				}
			}
		}
	}

}
