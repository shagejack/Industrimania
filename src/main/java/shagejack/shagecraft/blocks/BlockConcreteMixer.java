package shagejack.shagecraft.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import shagejack.shagecraft.blocks.includes.ShageBlockMachine;
import shagejack.shagecraft.tile.TileEntityConcreteMixer;

import javax.annotation.Nullable;

public class BlockConcreteMixer extends ShageBlockMachine<TileEntityConcreteMixer> implements ITileEntityProvider {

    public BlockConcreteMixer(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.ANVIL);
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
    public Class<TileEntityConcreteMixer> getTileEntityClass() {
        return TileEntityConcreteMixer.class;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        final IFluidHandler fluidHandler = getFluidHandler(world, pos);

        if (fluidHandler != null && FluidUtil.getFluidHandler(heldItem) != null) {
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
            return FluidUtil.getFluidHandler(heldItem) != null;
        }

        if (world.getTileEntity(pos) instanceof TileEntityConcreteMixer) {
            TileEntityConcreteMixer tile = (TileEntityConcreteMixer) world.getTileEntity(pos);
            if (!player.isSneaking()) {
                if (tile != null && heldItem.isEmpty() && tile.tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
                    if(!tile.getMixing()) {
                        tile.setMixing(true);
                        tile.setStirCount(tile.getStirCount() + 1);
                        if(!world.isRemote) world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.BLOCKS, 0.5F, 0.25F);
                        tile.forceSync();
                        return true;
                    }
                }
                if (!heldItem.isEmpty()) {
                    for (int i = 0; i <= 8; i++) {
                        if (tile.getStackInSlot(i).isEmpty()) {
                            ItemStack ingredient = heldItem.copy();
                            ingredient.setCount(1);
                            tile.setInventorySlotContents(i, ingredient);
                            if (!player.capabilities.isCreativeMode)
                                heldItem.shrink(1);
                            tile.setStirCount(0);
                            tile.setMixing(false);
                            if(!world.isRemote) world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS, 0.75F, 2F);
                            tile.forceSync();
                            return true;
                        }
                    }
                }
            }

            if (player.isSneaking()) {
                for (int i = 8; i >= 0; i--) {
                    if (!tile.getStackInSlot(i).isEmpty()) {

                        ItemStack result = tile.removeStackFromSlot(i);

                        if (result.isEmpty()) {
                            if (!heldItem.isEmpty())
                                return false;
                        }
                        if (!result.isEmpty())
                            ItemHandlerHelper.giveItemToPlayer(player, result, EntityEquipmentSlot.MAINHAND.getSlotIndex());

                        tile.setStirCount(0);
                        tile.setMixing(false);
                        if(!world.isRemote) world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5F, 2F);
                        tile.forceSync();
                        tile.markDirty();
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    private IFluidHandler getFluidHandler(IBlockAccess world, BlockPos pos) {
        TileEntityConcreteMixer tileentity = (TileEntityConcreteMixer) world.getTileEntity(pos);
        return tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }


}
