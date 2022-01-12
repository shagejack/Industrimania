package shagejack.industrimania.content.metallurgy.item.smeltery.cluster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class IronCluster extends Item {

    public IronCluster(Properties properties) {
        super(properties);
    }

    public static double getMass(ItemStack item) {
        if (item.hasTag()) {
            return item.getTag().getDouble("mass");
        }
        return 0;
    }

    public static double getCarbon(ItemStack item) {
        if (item.hasTag()) {
            return item.getTag().getDouble("carbon");
        }
        return 0;
    }

    public static double getImpurities(ItemStack item) {
        if (item.hasTag()) {
            return item.getTag().getDouble("impurities");
        }
        return 0;
    }

    public static double getTemp(ItemStack item) {
        if (item.hasTag()) {
            return item.getTag().getDouble("temp");
        }
        return 0;
    }

    public static int[] getShape(ItemStack item){
        if (item.hasTag()) {
            return item.getTag().getIntArray("shape");
        }
        return new int[1];
    }

    public static void setMass(ItemStack itemStack, double mass) {
        TagCompoundCheck(itemStack);
        itemStack.getTag().putDouble("mass", mass);
    }

    public static void setCarbon(ItemStack itemStack, double carbon) {
        TagCompoundCheck(itemStack);
        itemStack.getTag().putDouble("carbon", carbon);
    }

    public static void setTemp(ItemStack itemStack, double temp) {
        TagCompoundCheck(itemStack);
        itemStack.getTag().putDouble("temp", temp);
    }

    public static void setImpurities(ItemStack itemStack, double impurities) {
        TagCompoundCheck(itemStack);
        itemStack.getTag().putDouble("impurities", impurities);
    }

    public static void setShape(ItemStack itemStack, int[] shape) {
        TagCompoundCheck(itemStack);
        itemStack.getTag().putIntArray("shape", shape);
    }

    public static void TagCompoundCheck(ItemStack stack) {
        if (!stack.hasTag()) {
            InitTagCompound(stack);
        }
    }

    public static void InitTagCompound(ItemStack stack) {
        stack.setTag(new CompoundTag());
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (getTemp(itemStack) > 298.15) {
            setTemp(itemStack, getTemp(itemStack) - 0.25);
            if(getTemp(itemStack) > 353.15){
                entity.setRemainingFireTicks(20);
                entity.hurt(DamageSource.LAVA, 1);
            }
        }
    }

    public void appendTooltip(ItemStack itemStack, @Nullable Level level, List<String> tooltip, TooltipFlag flag) {
        tooltip.add("Mass: " + getMass(itemStack));
        tooltip.add("Carbon: " + getCarbon(itemStack));
        tooltip.add("Impurities: " + getImpurities(itemStack));
        tooltip.add("Temp: " + getTemp(itemStack));
        int[] shape = getShape(itemStack);
        tooltip.add("=====Shape=====");
        if (shape != null && shape[0] > 0 && shape[1] > 0) {
            for (int i = 0; i < shape[1]; i++) {
                String temp = "";
                for (int j = 0; j < shape[0]; j++) {
                    temp += "\u2b1b";
                }
                tooltip.add(temp);
            }
        }
    }

    //TODO: hurt player and set player on fire when in inventory


}