package shagejack.minecraftology.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import shagejack.minecraftology.items.includes.MCLBaseItem;

public class Multimeter extends MCLBaseItem {
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
        }

        if (result == EnumActionResult.SUCCESS)
            player.swingArm(hand);
        return result;
    }
}
