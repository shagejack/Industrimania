package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityForge;
import shagejack.minecraftology.tile.TileEntityIronOreSlag;

import java.util.List;

public class BlockForge extends MCLBlockContainer<TileEntityForge> {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Minecraftology.ITEMS.forge_hammer)
    };

    public BlockForge(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.ANVIL);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer player) {
        if (player instanceof FakePlayer || player == null)
            return;

        TileEntityForge tileEntity = getTileEntity(worldIn, pos);
        if (tileEntity != null) {
            ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
            if (isValidForgeTool(held, player)) {
                tileEntity.forge(player, held);
            }
        }
    }
    public static boolean isValidForgeTool(ItemStack stack, EntityPlayer player) {
        return !stack.isEmpty() && (isForgeToolWhitelisted(stack));
    }

    private static boolean isForgeToolWhitelisted(ItemStack stack) {
        for (ItemStack itemStack : toolStackList) {
            if (ItemStack.areItemsEqualIgnoreDurability(itemStack, stack))
                return true;
        }
        return false;
    }


    @Override
    public Class<TileEntityForge> getTileEntityClass() {
        return TileEntityForge.class;
    }


}
