package shagejack.shagecraft.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class MatterHelper {

    private static IRecipe GetRecipeOf(ItemStack item) {
        List recipes = ForgeRegistries.RECIPES.getValues();
        for (Object recipe1 : recipes) {
            IRecipe recipe = (IRecipe) recipe1;

            if (recipe != null && !recipe.getRecipeOutput().isEmpty() && recipe.getRecipeOutput().getItem() == item.getItem()) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean DropInventory(World world, IInventory inventory, BlockPos pos) {
        if (inventory != null) {
            for (int i1 = 0; i1 < inventory.getSizeInventory(); ++i1) {
                ItemStack itemstack = inventory.getStackInSlot(i1);

                if (!itemstack.isEmpty()) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityitem = new EntityItem(world, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2), itemstack);

                    if (itemstack.hasTagCompound()) {
                        entityitem.getItem().setTagCompound(itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.05F;
                    entityitem.motionX = (double) ((float) world.rand.nextGaussian() * f3);
                    entityitem.motionY = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double) ((float) world.rand.nextGaussian() * f3);
                    world.spawnEntity(entityitem);
                }
            }
            return true;
        }

        return false;
    }
}
