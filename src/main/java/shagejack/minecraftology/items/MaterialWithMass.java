package shagejack.minecraftology.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.items.includes.MCLBaseItem;

import javax.annotation.Nullable;
import java.util.List;

public class MaterialWithMass extends MCLBaseItem {
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
        infos.add("\u8d28\u91cf(Mass): " + getMass(itemstack)  + "g");
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

}
