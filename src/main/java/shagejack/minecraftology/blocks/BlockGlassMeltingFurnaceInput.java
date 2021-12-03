package shagejack.minecraftology.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.api.firetongs.IToolFireTongs;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnace;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnaceInput;

public class BlockGlassMeltingFurnaceInput extends MCLBlockMachine<TileEntityGlassMeltingFurnaceInput> implements ITileEntityProvider {

    public BlockGlassMeltingFurnaceInput(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setHasGui(true);
    }

    @Override
    public Class<TileEntityGlassMeltingFurnaceInput> getTileEntityClass() {
        return TileEntityGlassMeltingFurnaceInput.class;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

}
