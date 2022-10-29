package shagejack.industrimania.content.misc.shapedPartsBase;

import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.foundation.voxel.IMVoxelShape;

public interface IShapedParts {

    IMVoxelShape getShape(ItemStack stack);

    void setShape(ItemStack stack, IMVoxelShape shape);

    PartMaterial getMaterial(ItemStack stack);

    void setMaterial(ItemStack stack, PartMaterials material);

    void initFilled(ItemStack stack);

}
