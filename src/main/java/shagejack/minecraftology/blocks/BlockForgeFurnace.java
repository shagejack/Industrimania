package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.gui.inventory.GuiElementLoader;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.tile.TileEntityForge;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MachineHelper;

public class BlockForgeFurnace extends MCLBlockMachine<TileEntityForgeFurnace> {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Minecraftology.ITEMS.iron_cluster)
    };

    public BlockForgeFurnace(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setHasGui(true);
        setHasRotation();
        setRotationType(MCLBlockHelper.RotationType.FOUR_WAY);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int id = GuiElementLoader.GUI_DEMO;
        playerIn.openGui(Minecraftology.INSTANCE,id,worldIn,pos.getX(),pos.getY(), pos.getZ());
        return MachineHelper.canOpenMachine(worldIn, pos, playerIn, hasGui, getUnlocalizedMessage(0));
    }


    @Override
    public Class<TileEntityForgeFurnace> getTileEntityClass() {
        return TileEntityForgeFurnace.class;
    }


}
