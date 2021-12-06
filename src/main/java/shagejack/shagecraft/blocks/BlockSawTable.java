package shagejack.shagecraft.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.blocks.includes.ShageBlockContainer;
import shagejack.shagecraft.tile.TileEntitySawTable;

public class BlockSawTable extends ShageBlockContainer<TileEntitySawTable> {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Shagecraft.ITEMS.saw)
    };

    public BlockSawTable(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.ANVIL);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setLightOpacity(0);
        this.fullBlock = false;
    }

    public static boolean isValidSawTool(ItemStack stack, EntityPlayer player) {
        return !stack.isEmpty() && (isSawToolWhitelisted(stack));
    }

    private static boolean isSawToolWhitelisted(ItemStack stack) {
        for (ItemStack itemStack : toolStackList) {
            if (ItemStack.areItemsEqualIgnoreDurability(itemStack, stack))
                return true;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            ItemStack stack = playerIn.getHeldItem(hand);
            ItemStack held = playerIn.getHeldItem(EnumHand.MAIN_HAND);
            TileEntitySawTable tileEntity = (TileEntitySawTable) worldIn.getTileEntity(pos);
            if (tileEntity == null) return false;

            if (!isValidSawTool(held, playerIn)) {
                if (!stack.isEmpty() && tileEntity.isItemValidForSlot(0, stack)) {
                    ItemStack itemStack = tileEntity.getStackInSlot(0);
                    boolean flag = false;

                    if (itemStack.isEmpty()) {
                        ItemStack ingredient = stack.copy();
                        ingredient.setCount(1);
                        tileEntity.setInventorySlotContents(0, ingredient);
                        stack.setCount(stack.getCount() - 1);
                        flag = true;
                    }

                    if (flag)
                        return true;
                }

                ItemStack result = tileEntity.removeStackFromSlot(0);

                if (result.isEmpty()) {
                    if (!stack.isEmpty())
                        return false;
                }

                if (!result.isEmpty())
                    ItemHandlerHelper.giveItemToPlayer(playerIn, result, EntityEquipmentSlot.MAINHAND.getSlotIndex());

            } else {
                if (playerIn instanceof FakePlayer || playerIn == null)
                    return false;

                if(!playerIn.getCooldownTracker().hasCooldown(held.getItem()))
                    tileEntity.saw(playerIn, held);
            }

            tileEntity.markDirty();
            return true;
    }

    @Override
    public Class<TileEntitySawTable> getTileEntityClass() {
        return TileEntitySawTable.class;
    }


}