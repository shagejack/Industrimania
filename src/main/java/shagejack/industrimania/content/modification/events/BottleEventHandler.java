package shagejack.industrimania.content.modification.events;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.ItemHandlerHelper;
import shagejack.industrimania.foundation.utility.RayTraceUtils;
import shagejack.industrimania.registries.item.AllItems;

public class BottleEventHandler {
    public static void onClick(final PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isClientSide() && event.getItemStack().getItem() instanceof BottleItem) {
            Player player = event.getPlayer();
            BlockHitResult hitResult = RayTraceUtils.getPlayerPOVHitResult(player, event.getWorld(), ClipContext.Fluid.SOURCE_ONLY);

            if (hitResult.getType() == HitResult.Type.MISS)
                return;

            if (hitResult.getType() == HitResult.Type.BLOCK) {

                final BlockPos pos = hitResult.getBlockPos();

                if (!event.getWorld().mayInteract(player, pos))
                    return;

                Level level = event.getWorld();
                BlockState state = level.getBlockState(pos);
                FluidState fluidState = level.getFluidState(pos);

                if (fluidState.is(FluidTags.WATER)) {
                    if (state.getBlock() instanceof IFluidBlock || state.getBlock() instanceof LiquidBlock) {
                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    } else {
                        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE));
                        if (!state.canSurvive(level, pos)) {
                            level.destroyBlock(pos, true);
                        }
                    }

                    if (!player.isCreative()) {
                        event.getItemStack().shrink(1);
                    }

                    event.getWorld().playSound(player, player.position().x(), player.position().y(), player.position().z(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(AllItems.dirtyWaterBottle.get()));
                    event.setCanceled(true);
                }
            }
        }
    }
}
