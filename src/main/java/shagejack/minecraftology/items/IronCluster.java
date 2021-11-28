package shagejack.minecraftology.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.items.includes.MCLBaseItem;
import shagejack.minecraftology.util.MCLStringHelper;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class IronCluster extends MCLBaseItem {

    public IronCluster(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public boolean hasDetails(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(itemstack, player, worldIn, infos);
        infos.add("\u8d28\u91cf(Mass): " + MCLStringHelper.formatNumber(getMass(itemstack))  + "g");
        infos.add("\u6742\u8d28(Impurities): " + MCLStringHelper.formatNumber(getImpurities(itemstack))  + "g");
        infos.add("\u78b3\u542b\u91cf(Carbon): " + MCLStringHelper.formatNumber(getCarbon(itemstack) * 100)  + "%");
        infos.add("\u6e29\u5ea6(Temp): " + MCLStringHelper.formatNumber(getTemp(itemstack))  + "K");
        infos.add("===== \u5f62\u72b6 Shape =====");
        int[] shape = getShape(itemstack);
        if(shape != null) {
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
        if (getTemp(itemStack) > 298.15) {
            setTemp(itemStack, getTemp(itemStack) - 0.01);
        }
    }

}
