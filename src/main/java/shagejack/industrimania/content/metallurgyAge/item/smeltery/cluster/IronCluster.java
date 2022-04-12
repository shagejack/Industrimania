package shagejack.industrimania.content.metallurgyAge.item.smeltery.cluster;

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
        return item.getOrCreateTag().getDouble("mass");
    }

    public static double getCarbon(ItemStack item) {
        return item.getOrCreateTag().getDouble("carbon");
    }

    public static double getImpurities(ItemStack item) {
        return item.getOrCreateTag().getDouble("impurities");
    }

    public static double getTemp(ItemStack item) {
        return item.getOrCreateTag().getDouble("temp");
    }

    public static int[] getShape(ItemStack item){
        return item.getOrCreateTag().getIntArray("shape");
    }

    public static void setMass(ItemStack itemStack, double mass) {
        itemStack.getOrCreateTag().putDouble("mass", mass);
    }

    public static void setCarbon(ItemStack itemStack, double carbon) {
        itemStack.getOrCreateTag().putDouble("carbon", carbon);
    }

    public static void setTemp(ItemStack itemStack, double temp) {
        itemStack.getOrCreateTag().putDouble("temp", temp);
    }

    public static void setImpurities(ItemStack itemStack, double impurities) {
        itemStack.getOrCreateTag().putDouble("impurities", impurities);
    }

    /*
    public static void setShape(ItemStack itemStack, int[] shape) {
        itemStack.getOrCreateTag().putIntArray("shape", shape);
    }
     */

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

    /*
    public void appendTooltip(ItemStack itemStack, @Nullable Level level, List<String> tooltip, TooltipFlag flag) {
        tooltip.add("Mass: " + getMass(itemStack));
        tooltip.add("Carbon: " + getCarbon(itemStack));
        tooltip.add("Impurities: " + getImpurities(itemStack));
        tooltip.add("Temp: " + getTemp(itemStack));
        int[] shape = getShape(itemStack);
        tooltip.add("=====Shape=====");
        if (shape.length < 2)
            return;
        if (shape[0] > 0 && shape[1] > 0) {
            for (int i = 0; i < shape[1]; i++) {
                tooltip.add("\u2b1b".repeat(shape[0]));
            }
        }
    }
     */

    //TODO: hurt player and set player on fire when in inventory


}
