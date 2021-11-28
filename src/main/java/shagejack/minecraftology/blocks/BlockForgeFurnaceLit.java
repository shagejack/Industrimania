package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.gui.inventory.GuiElementLoader;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MachineHelper;

public class BlockForgeFurnaceLit extends BlockForgeFurnace {

    public BlockForgeFurnaceLit(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setLightLevel(4.0F);
        setHasGui(true);
        setHasRotation();
        setRotationType(MCLBlockHelper.RotationType.FOUR_WAY);
    }

}
