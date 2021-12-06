package shagejack.shagecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.machines.ShageTileEntityMachine;

public class MachineHelper {
    public static boolean canOpenMachine(World world, BlockPos pos, EntityPlayer player, boolean hasGui, String errorMessage) {
        if (world.isRemote) {
            return true;
        } else if (hasGui) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof ShageTileEntityMachine) {
                if (((ShageTileEntityMachine) tileEntity).isUsableByPlayer(player)) {
                    FMLNetworkHandler.openGui(player, Shagecraft.INSTANCE, -1, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                } else {
                    TextComponentString message = new TextComponentString(TextFormatting.GOLD + "[Shagecraft] " + TextFormatting.RED + ShageStringHelper.translateToLocal(errorMessage).replace("$0", ((ShageTileEntityMachine) tileEntity).getDisplayName().toString()));
                    message.setStyle(new Style().setColor(TextFormatting.RED));
                    player.sendMessage(message);
                }
            }
        }

        return false;
    }

    public static boolean canRemoveMachine(World world, EntityPlayer player, BlockPos pos, boolean willHarvest) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ShageTileEntityMachine) {
            if (!player.capabilities.isCreativeMode &&
                    ((ShageTileEntityMachine) tileEntity).hasOwner() && !((ShageTileEntityMachine) tileEntity).getOwner().equals(player.getGameProfile().getId())) {
                TextComponentString message = new TextComponentString(TextFormatting.GOLD + "[Shagecraft] " + TextFormatting.RED + ShageStringHelper.translateToLocal("alert.no_rights.break").replace("$0", ((ShageTileEntityMachine) tileEntity).getDisplayName().toString()));
                message.setStyle(new Style().setColor(TextFormatting.RED));
                player.sendMessage(message);
                return false;
            }
        }
        return true;
    }
}

