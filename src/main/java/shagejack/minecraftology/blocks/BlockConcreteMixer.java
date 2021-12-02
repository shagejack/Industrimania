package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
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
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.tile.TileEntityConcreteMixer;

import javax.annotation.Nullable;

public class BlockConcreteMixer extends MCLBlockMachine<TileEntityConcreteMixer> {

    public BlockConcreteMixer(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.ANVIL);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setLightOpacity(0);
        this.fullBlock = false;
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

        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityConcreteMixer) {
            TileEntityConcreteMixer tile = (TileEntityConcreteMixer) world.getTileEntity(pos);
            if (!player.isSneaking()) {
                if (tile != null && heldItem.isEmpty() && tile.tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
                    if(!tile.getMixing()) {
                        tile.setMixing(true);
                        tile.setStirCount(tile.getStirCount() + 1);
                        world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.BLOCKS, 0.5F, 0.25F);
                        world.notifyBlockUpdate(pos, state, state, 3);
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
                            world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS, 0.75F, 2F);
                            world.notifyBlockUpdate(pos, state, state, 3);
                            return true;
                        }
                    }
                }
            }

            if (player.isSneaking()) {
                for (int i = 8; i >= 0; i--) {
                    if (!tile.getStackInSlot(i).isEmpty()) {
                        if (!player.inventory.addItemStackToInventory(tile.getStackInSlot(i)))
                            ForgeHooks.onPlayerTossEvent(player, tile.getStackInSlot(i), false);
                        tile.setInventorySlotContents(i, ItemStack.EMPTY);
                        tile.setStirCount(0);
                        tile.setMixing(false);
                        world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5F, 2F);
                        world.notifyBlockUpdate(pos, state, state, 3);
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
