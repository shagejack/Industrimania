package shagejack.shagecraft.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.api.firetongs.IToolFireTongs;
import shagejack.shagecraft.blocks.includes.ShageBlockContainer;
import shagejack.shagecraft.tile.TileEntityForge;

public class BlockForge extends ShageBlockContainer<TileEntityForge> {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Shagecraft.ITEMS.forge_hammer)
    };

    public BlockForge(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.ANVIL);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setLightOpacity(0);
        this.fullBlock = false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer player) {
        if (player instanceof FakePlayer || player == null)
            return;

        TileEntityForge tileEntity = getTileEntity(worldIn, pos);
        if (tileEntity != null) {
            ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
            if (isValidForgeTool(held, player)) {
                if(!player.getCooldownTracker().hasCooldown(held.getItem()))
                tileEntity.forge(player, held);
            }
        }
    }

    public static boolean isValidForgeTool(ItemStack stack, EntityPlayer player) {
        return !stack.isEmpty() && (isForgeToolWhitelisted(stack));
    }

    private static boolean isForgeToolWhitelisted(ItemStack stack) {
        for (ItemStack itemStack : toolStackList) {
            if (ItemStack.areItemsEqualIgnoreDurability(itemStack, stack))
                return true;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        Item held = stack.getItem();
        TileEntityForge tileEntity = (TileEntityForge) worldIn.getTileEntity(pos);
        if (tileEntity == null) return false;

        if (held != Shagecraft.ITEMS.fire_tongs) {

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
            if(held instanceof IToolFireTongs) {
                IToolFireTongs fireTongs = (IToolFireTongs) held;
                    ItemStack input = tileEntity.getInventory().getStackInSlot(0);
                    if (fireTongs.canFireTongs(stack) && (!input.isEmpty())) {
                        if(input.getItem() == Shagecraft.ITEMS.iron_cluster){
                            stack.setTagCompound(input.getTagCompound());
                            Shagecraft.ITEMS.fire_tongs.setHasItem(stack,true);
                            Shagecraft.ITEMS.fire_tongs.setItemID(stack, input.getItem().getRegistryName().toString());
                            if(hand == EnumHand.MAIN_HAND) {
                                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
                            } else if (hand == EnumHand.OFF_HAND){
                                playerIn.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
                            }
                            input.shrink(1);
                        }
                    } else if ((!fireTongs.canFireTongs(stack)) && input.isEmpty()) {
                        Item containItem = Item.REGISTRY.getObject(new ResourceLocation(Shagecraft.ITEMS.fire_tongs.getItemID(stack)));
                        ItemStack contain = new ItemStack(containItem);
                        Shagecraft.ITEMS.iron_cluster.setMass(contain, Shagecraft.ITEMS.fire_tongs.getMass(stack));
                        Shagecraft.ITEMS.iron_cluster.setImpurities(contain, Shagecraft.ITEMS.fire_tongs.getImpurities(stack));
                        Shagecraft.ITEMS.iron_cluster.setCarbon(contain, Shagecraft.ITEMS.fire_tongs.getCarbon(stack));
                        Shagecraft.ITEMS.iron_cluster.setTemp(contain, Shagecraft.ITEMS.fire_tongs.getTemp(stack));
                        Shagecraft.ITEMS.iron_cluster.setShape(contain, Shagecraft.ITEMS.fire_tongs.getShape(stack));
                        tileEntity.getInventory().setInventorySlotContents(0, contain);
                        Shagecraft.ITEMS.fire_tongs.setHasItem(stack, false);
                        Shagecraft.ITEMS.fire_tongs.setItemID(stack, "");
                        Shagecraft.ITEMS.fire_tongs.setMass(stack, 0);
                        Shagecraft.ITEMS.fire_tongs.setImpurities(stack, 0);
                        Shagecraft.ITEMS.fire_tongs.setCarbon(stack, 0);
                        Shagecraft.ITEMS.fire_tongs.setTemp(stack, 0);
                        Shagecraft.ITEMS.fire_tongs.setShape(stack, new int[0]);
                        if(hand == EnumHand.MAIN_HAND) {
                            playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
                        } else if (hand == EnumHand.OFF_HAND){
                            playerIn.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
                        }
                }
            }
        }

        tileEntity.markDirty();
        return true;
    }

    @Override
    public Class<TileEntityForge> getTileEntityClass() {
        return TileEntityForge.class;
    }


}
