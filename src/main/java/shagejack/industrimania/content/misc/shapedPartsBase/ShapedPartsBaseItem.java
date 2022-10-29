package shagejack.industrimania.content.misc.shapedPartsBase;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.foundation.voxel.IMVoxelShape;

public class ShapedPartsBaseItem extends Item implements IShapedParts {

    public ShapedPartsBaseItem(Properties properties) {
        super(properties);
    }

    @Override
    public IMVoxelShape getShape(ItemStack stack) {
        if (!stack.hasTag())
            return IMVoxelShape.EMPTY;

        IMVoxelShape shape = new IMVoxelShape();

        shape.deserializeNBT(stack.getOrCreateTag().getCompound("VoxelShape"));

        return shape;
    }

    @Override
    public void setShape(ItemStack stack, IMVoxelShape shape) {
        if (shape == null)
            return;

        stack.getOrCreateTag().put("VoxelShape", shape.serializeNBT());
    }

    @Override
    public PartMaterial getMaterial(ItemStack stack) {
        if (!stack.hasTag())
            return PartMaterials.EMPTY;

        return PartMaterials.getMaterialFromNBT(stack.getOrCreateTag().getCompound("VoxelMaterial"));
    }

    @Override
    public void setMaterial(ItemStack stack, PartMaterials material) {
        if (material == null || material == PartMaterials.EMPTY)
            return;

        stack.getOrCreateTag().putString("VoxelMaterial", material.getName());
    }

    @Override
    public void initFilled(ItemStack stack) {
        IMVoxelShape voxelShape = new IMVoxelShape();
        voxelShape.initFilled();
        stack.getOrCreateTag().put("VoxelShape", voxelShape.serializeNBT());
    }

}
