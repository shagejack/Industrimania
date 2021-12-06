package shagejack.shagecraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.shagecraft.items.includes.ShageBaseItem;
import shagejack.shagecraft.util.ShageStringHelper;

import javax.annotation.Nullable;
import java.util.List;

public class MaterialWithMass extends ShageBaseItem {
    public MaterialWithMass(String name) {
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
        infos.add("\u8d28\u91cf(Mass): " + ShageStringHelper.formatNumber(getMass(itemstack)) + "g");
    }

    public double getMass(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getDouble("mass");
        }
        return 0;
    }

    public void setMass(ItemStack itemStack, double mass) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setDouble("mass", mass);
    }

    public int getCut(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getInteger("cut");
        }
        return 0;
    }

    public void setCut(ItemStack itemStack, int cut) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setInteger("cut", cut);
    }

}
