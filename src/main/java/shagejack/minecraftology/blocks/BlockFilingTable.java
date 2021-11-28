package shagejack.minecraftology.blocks;

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
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityFilingTable;
import shagejack.minecraftology.tile.TileEntityForge;

public class BlockFilingTable extends MCLBlockContainer<TileEntityFilingTable> {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Minecraftology.ITEMS.flat_file)
    };

    public BlockFilingTable(Material material, String name) {
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

        TileEntityFilingTable tileEntity = getTileEntity(worldIn, pos);
        if (tileEntity != null) {
            ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
            if (isValidFileTool(held, player)) {
                if(!player.getCooldownTracker().hasCooldown(held.getItem()))
                tileEntity.file(player, held);
            }
        }
    }

    public static boolean isValidFileTool(ItemStack stack, EntityPlayer player) {
        return !stack.isEmpty() && (isFileToolWhitelisted(stack));
    }

    private static boolean isFileToolWhitelisted(ItemStack stack) {
        for (ItemStack itemStack : toolStackList) {
            if (ItemStack.areItemsEqualIgnoreDurability(itemStack, stack))
                return true;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        TileEntityFilingTable tileEntity = (TileEntityFilingTable) worldIn.getTileEntity(pos);
        if (tileEntity == null) return false;

        if (!stack.isEmpty() && tileEntity.isItemValidForSlot(0, stack)) {
            ItemStack itemStack = tileEntity.getStackInSlot(0);
            boolean flag = false;

            if (itemStack.isEmpty()) {
                tileEntity.setInventorySlotContents(0, stack.copy());
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

        tileEntity.markDirty();
        return true;
    }

    @Override
    public Class<TileEntityFilingTable> getTileEntityClass() {
        return TileEntityFilingTable.class;
    }


}
