package shagejack.minecraftology.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.tile.TileEntityConcreteMixer;
import shagejack.minecraftology.tile.TileEntityGlassMould;

import javax.annotation.Nullable;

public class BlockGlassMould extends MCLBlockMachine<TileEntityGlassMould> implements ITileEntityProvider {

    public BlockGlassMould(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public Class<TileEntityGlassMould> getTileEntityClass() {
        return TileEntityGlassMould.class;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        final IFluidHandler fluidHandler = getFluidHandler(world, pos);

        if (fluidHandler != null && FluidUtil.getFluidHandler(heldItem) != null) {
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
            return FluidUtil.getFluidHandler(heldItem) != null;
        }

        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityGlassMould) {
            TileEntityGlassMould tile = (TileEntityGlassMould) world.getTileEntity(pos);
            if (!player.isSneaking()) {
                if (!heldItem.isEmpty()) {
                    if(heldItem.getItem() != Minecraftology.ITEMS.iron_pipe) {
                        for (int i = 0; i <= 0; i++) {
                            if (tile.getStackInSlot(i).isEmpty()) {
                                ItemStack ingredient = heldItem.copy();
                                ingredient.setCount(1);
                                tile.setInventorySlotContents(i, ingredient);
                                if (!player.capabilities.isCreativeMode)
                                    heldItem.shrink(1);
                                world.notifyBlockUpdate(pos, state, state, 3);
                                return true;
                            }
                        }
                    } else if (tile != null && tile.tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
                        if(!tile.getBlowing()) {
                            tile.setBlowing(true);
                            tile.setBlowCount(tile.getBlowCount() + 1);
                            world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.BLOCKS, 0.5F, 0.25F);
                            world.notifyBlockUpdate(pos, state, state, 3);
                            return true;
                        } else {
                            tile.setBlowCount(0);
                            tile.tank.drain(Fluid.BUCKET_VOLUME, true);
                        }
                    }
                }
            }

            if (player.isSneaking()) {
                    if (!tile.getStackInSlot(0).isEmpty()) {
                        if (!player.inventory.addItemStackToInventory(tile.getStackInSlot(0)))
                            ForgeHooks.onPlayerTossEvent(player, tile.getStackInSlot(0), false);
                        tile.removeStackFromSlot(0);
                        world.notifyBlockUpdate(pos, state, state, 3);
                        tile.markDirty();
                        return true;
                    }
            }
        }
        return true;
    }

    @Nullable
    private IFluidHandler getFluidHandler(IBlockAccess world, BlockPos pos) {
        TileEntityGlassMould tileentity = (TileEntityGlassMould) world.getTileEntity(pos);
        return tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }


}
