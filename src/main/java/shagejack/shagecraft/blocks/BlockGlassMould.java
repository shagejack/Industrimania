package shagejack.shagecraft.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.blocks.includes.ShageBlockMachine;
import shagejack.shagecraft.tile.TileEntityGlassMould;

import javax.annotation.Nullable;

public class BlockGlassMould extends ShageBlockMachine<TileEntityGlassMould> implements ITileEntityProvider {

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

        if (world.getTileEntity(pos) instanceof TileEntityGlassMould) {
            TileEntityGlassMould tile = (TileEntityGlassMould) world.getTileEntity(pos);
            if (!player.isSneaking()) {
                if (!heldItem.isEmpty()) {
                    if(heldItem.getItem() != Shagecraft.ITEMS.iron_pipe) {
                            if (tile.getStackInSlot(0).isEmpty()) {
                                ItemStack ingredient = heldItem.copy();
                                ingredient.setCount(1);
                                tile.setInventorySlotContents(0, ingredient);
                                if (!player.capabilities.isCreativeMode)
                                    heldItem.shrink(1);
                                tile.forceSync();
                                return true;
                            }
                    } else if (tile != null && tile.tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
                        if(!tile.getBlowing()) {
                            tile.setBlowing(true);
                            tile.setBlowCount(tile.getBlowCount() + 1);
                            if(!world.isRemote) world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.BLOCKS, 0.5F, 0.25F);
                            tile.forceSync();
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
                        ItemStack result = tile.removeStackFromSlot(0);

                        if (result.isEmpty()) {
                            if (!heldItem.isEmpty())
                                return false;
                        }
                        if (!result.isEmpty())
                            ItemHandlerHelper.giveItemToPlayer(player, result, EntityEquipmentSlot.MAINHAND.getSlotIndex());

                        tile.forceSync();
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
