package shagejack.minecraftology.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.api.firetongs.IToolFireTongs;
import shagejack.minecraftology.items.includes.MCLBaseItem;
import shagejack.minecraftology.util.MCLStringHelper;

import javax.annotation.Nullable;
import java.util.List;

public class FireTongs extends MCLBaseItem implements IToolFireTongs {

    public FireTongs(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        IBlockState state = world.getBlockState(pos);
        EnumActionResult result = EnumActionResult.PASS;

        if (!state.getBlock().isAir(state, world, pos)) {
            PlayerInteractEvent e = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
            if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY) {
                return EnumActionResult.FAIL;
            }
        }

        if (result == EnumActionResult.SUCCESS) {
            player.swingArm(hand);
        }

        return result;

    }

    @Override
    public boolean hasDetails(ItemStack stack) {
        return getHasItem(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(itemstack, player, worldIn, infos);
        if(getHasItem(itemstack)) {
            infos.add("\u88c5\u6709\u7269\u54c1:");
            infos.add("\u8d28\u91cf(Mass): " + MCLStringHelper.formatNumber(getMass(itemstack)) + "g");
            infos.add("\u6742\u8d28(Impurities): " + MCLStringHelper.formatNumber(getImpurities(itemstack)) + "g");
            infos.add("\u78b3\u542b\u91cf(Carbon): " + MCLStringHelper.formatNumber(getCarbon(itemstack) * 100) + "%");
            infos.add("\u6e29\u5ea6(Temp): " + MCLStringHelper.formatNumber(getTemp(itemstack)) + "K");
            infos.add("===== \u5f62\u72b6 Shape =====");
            int[] shape = getShape(itemstack);
            if (shape != null) {
                if (shape[0] > 0 && shape[1] > 0) {
                    for (int i = 0; i < shape[1]; i++) {
                        String temp = "";
                        for (int j = 0; j < shape[0]; j++) {
                            temp += "\u2b1b";
                        }
                        infos.add(temp);
                    }
                }
            }
        }

    }

    public boolean canFireTongs(ItemStack itemstack){
        Item item = itemstack.getItem();
        if (item == Minecraftology.ITEMS.fire_tongs) {
            return !getHasItem(itemstack);
        }
        return false;
    }

    public String getItemID(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getString("itemID");
        }
        return null;
    }

    public void setItemID(ItemStack itemStack, String ID) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setString("itemID", ID);
    }

    public boolean getHasItem(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getBoolean("hasItem");
        }
        return false;
    }

    public void setHasItem(ItemStack itemStack, boolean flag) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setBoolean("hasItem", flag);
    }

    public double getMass(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getDouble("mass");
        }
        return 0;
    }

    public double getCarbon(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getDouble("carbon");
        }
        return 0;
    }

    public double getImpurities(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getDouble("impurities");
        }
        return 0;
    }

    public double getTemp(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getDouble("temp");
        }
        return 0;
    }

    public int[] getShape(ItemStack item){
        if (item.hasTagCompound()) {
            return item.getTagCompound().getIntArray("shape");
        }
        return null;
    }

    public void setMass(ItemStack itemStack, double mass) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setDouble("mass", mass);
    }

    public void setCarbon(ItemStack itemStack, double carbon) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setDouble("carbon", carbon);
    }

    public void setTemp(ItemStack itemStack, double temp) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setDouble("temp", temp);
    }

    public void setImpurities(ItemStack itemStack, double impurities) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setDouble("impurities", impurities);
    }

    public void setShape(ItemStack itemStack, int[] shape) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setIntArray("shape", shape);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHolding) {
        if(getHasItem(itemStack)) {
            if (getTemp(itemStack) > 298.15) {
                setTemp(itemStack, getTemp(itemStack) - 0.25);
            }
        }
    }

}
