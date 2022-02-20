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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackTileEntity;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import shagejack.industrimania.registers.item.AllItems;

import java.util.HashMap;
import java.util.Map;

public class ItemPlaceableRenderer extends SafeTileEntityRenderer<ItemPlaceableBaseTileEntity> {

	public ItemPlaceableRenderer(BlockEntityRendererProvider.Context context) {}

	private static final Map<Item, ItemStack> renderItems = new HashMap<>();

	static {
		renderItems.put(AllItems.logAcacia.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logBirch.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logDarkOak.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logJungle.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logOak.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logSpruce.get(), new ItemStack(AllItems.modelLog.get()));

		renderItems.put(AllItems.plankAcacia.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.plankBirch.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.plankDarkOak.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.plankJungle.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.plankOak.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.plankSpruce.get(), new ItemStack(AllItems.modelWood.get()));
	}

	@Override
	protected void renderSafe(ItemPlaceableBaseTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
		int overlay) {
		renderItems(te, partialTicks, ms, buffer, light, overlay);
	}

	protected void renderItems(ItemPlaceableBaseTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.inventory.isEmpty()) {

			for (int i = 0; i < te.inventory.getSlots(); i++) {
				ItemStack stack = te.inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {

					ms.pushPose();

					Vec2 renderPos = te.getItemRenderPos(i);
					ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

					ms.translate(renderPos.x, renderPos.y, 0.5f);

					if (renderItems.containsKey(stack.getItem())) {
						itemRenderer.renderStatic(renderItems.get(stack.getItem()), ItemTransforms.TransformType.FIXED, 240, overlay, ms, buffer, 0);
					}

					ms.popPose();

				}
			}
		}
	}

}
