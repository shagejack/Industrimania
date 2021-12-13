package shagejack.shagecraft.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.items.includes.ShageBaseItem;

public class Multimeter extends ShageBaseItem {
    public Multimeter(String name) {
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

            TileEntity te = world.getTileEntity(pos);

            if (te != null && te.hasCapability(ShagecraftCapabilities.STEAM_HANDLER, null)) {
                double[] steam_properties = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).mergeProperties();
                double steam_pressure = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getSteamPressure();
                double steam_capacity = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getCapacity();
                double steam_occupied_capacity = steam_properties[0] * steam_pressure;

                double steam_enthalpy = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getActualEnthalpy(steam_properties);
                double steam_enthalpy_consumable = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getCurrentEnthalpyConsume();
                double steam_power = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getCurrentActualPower(1);
                double steam_work = te.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getActualWork(1, steam_properties[0], steam_enthalpy_consumable);

                String steam_state_string = "\u7a7a";
                switch ((int) steam_properties[2]) {
                    case 0:
                        break;
                    case 1:
                        steam_state_string = "\u9971\u548c\u84b8\u6c7d";
                        break;
                    case 2:
                        steam_state_string = "\u8fc7\u70ed\u84b8\u6c7d";
                }

                if(!player.isSneaking()) {
                    if (world.isRemote) {
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u8d28\u91cf: " + steam_properties[0] + " KG"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u6e29\u5ea6: " + steam_properties[1] + " K"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u72b6\u6001: " + steam_state_string));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u538b\u5f3a: " + steam_pressure + " MPa"));
                        player.sendMessage(new TextComponentString("\u5bb9\u91cf: " + steam_occupied_capacity + " / " + steam_capacity + " (" + (steam_occupied_capacity / steam_capacity * 100) + "%)"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u7113\u503c: " + steam_enthalpy + " KJ/KG"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u5355\u6b21\u505a\u529f\u6709\u6548\u7113\u503c: " + steam_enthalpy_consumable + " KJ/KG"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u505a\u529f\u529f\u7387\u0028\u6548\u7387\u0031\u0030\u0030\u0025\u0029: " + steam_power + " KW"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u5355\u6b21\u6240\u4f5c\u673a\u68b0\u529f\u0028\u6548\u7387\u0031\u0030\u0030\u0025\u0029: " + steam_work + " KJ"));
                    }
                } else {
                    if (!world.isRemote) {
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u8d28\u91cf: " + steam_properties[0] + " KG"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u6e29\u5ea6: " + steam_properties[1] + " K"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u72b6\u6001: " + steam_state_string));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u538b\u5f3a: " + steam_pressure + " MPa"));
                        player.sendMessage(new TextComponentString("\u5bb9\u91cf: " + steam_occupied_capacity + " / " + steam_capacity + " (" + (steam_occupied_capacity / steam_capacity * 100) + "%)"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u7113\u503c: " + steam_enthalpy + " KJ/KG"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u5355\u6b21\u505a\u529f\u6709\u6548\u7113\u503c: " + steam_enthalpy_consumable + " KJ/KG"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u505a\u529f\u529f\u7387\u0028\u6548\u7387\u0031\u0030\u0030\u0025\u0029: " + steam_power + " KW"));
                        player.sendMessage(new TextComponentString("\u84b8\u6c7d\u5355\u6b21\u6240\u4f5c\u673a\u68b0\u529f\u0028\u6548\u7387\u0031\u0030\u0030\u0025\u0029: " + steam_work + " KJ"));
                    }
                }
            }
        }

        if (result == EnumActionResult.SUCCESS)
            player.swingArm(hand);
        return result;
    }


}
