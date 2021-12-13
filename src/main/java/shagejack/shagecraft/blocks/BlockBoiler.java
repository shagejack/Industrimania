package shagejack.shagecraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import shagejack.shagecraft.blocks.includes.ShageBlockMachine;
import shagejack.shagecraft.tile.TileEntityConcreteMixer;
import shagejack.shagecraft.tile.TileEntityMachineBoiler;
import shagejack.shagecraft.util.MachineHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockBoiler extends ShageBlockMachine<TileEntityMachineBoiler> {

    public BlockBoiler(Material material, String name) {
        super(material, name);
        setHasRotation();
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        final IFluidHandler fluidHandler = getFluidHandler(worldIn, pos);

        if (fluidHandler != null && FluidUtil.getFluidHandler(heldItem) != null) {
            FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing);
            return FluidUtil.getFluidHandler(heldItem) != null;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public Class<TileEntityMachineBoiler> getTileEntityClass() {
        return TileEntityMachineBoiler.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
        return new TileEntityMachineBoiler();
    }

    @Nullable
    private IFluidHandler getFluidHandler(IBlockAccess world, BlockPos pos) {
        TileEntityMachineBoiler tileentity = (TileEntityMachineBoiler) world.getTileEntity(pos);
        return tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }
}
