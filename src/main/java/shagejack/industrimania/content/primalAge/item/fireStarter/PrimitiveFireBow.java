package shagejack.industrimania.content.primalAge.item.fireStarter;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseBlock;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseTileEntity;
import shagejack.industrimania.foundation.utility.RayTraceUtils;
import shagejack.industrimania.registers.item.AllItems;

import java.util.List;

public class PrimitiveFireBow extends Item {

    public PrimitiveFireBow(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockPos);
        ItemStack itemstack = context.getItemInHand();

        if (player != null) {
            if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate) && !(blockstate.getBlock() instanceof ItemPlaceableBaseBlock)) {
                //get pos on
                BlockPos blockPos1 = blockPos.relative(context.getClickedFace());
                if (BaseFireBlock.canBePlacedAt(level, blockPos1, context.getHorizontalDirection())) {
                   if (hasFlammableItemAbove(level, blockPos1.below())) {
                       itemstack.getOrCreateTag().putBoolean("isUsing", true);
                       player.startUsingItem(context.getHand());
                       level.playSound(null, blockPos1.below(), SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                       return InteractionResult.sidedSuccess(level.isClientSide());
                   }
                }
            } else {
                // do not start fire if it's already burning
                if (level.getBlockEntity(blockPos) instanceof ItemPlaceableBaseTileEntity te) {
                    if (te.isBurning())
                        return InteractionResult.FAIL;
                }
                if (hasFlammableItemAbove(level, blockPos)) {
                    itemstack.getOrCreateTag().putBoolean("isUsing", true);
                    player.startUsingItem(context.getHand());
                    level.playSound(null, blockPos, SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!selected) {
            stack.getOrCreateTag().putBoolean("isUsing", false);
            return;
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int slot) {
        if (entity instanceof Player player) {
            BlockHitResult result = RayTraceUtils.getPlayerPOVHitResult(player, level, ClipContext.Fluid.NONE);
            BlockPos firePos = result.getBlockPos().relative(result.getDirection());
            if (hasFlammableItemAbove(level, firePos.below())) {
                if (stack.getOrCreateTag().contains("isUsing", Tag.TAG_BYTE) && stack.getOrCreateTag().getBoolean("isUsing")) {
                    level.playSound(null, entity.getOnPos(), SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                }
            } else {
                stack.getOrCreateTag().putBoolean("isUsing", false);
                player.stopUsingItem();
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 400;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level p_41413_, LivingEntity p_41414_, int p_41415_) {
        stack.getOrCreateTag().putBoolean("isUsing", false);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        InteractionHand hand = entity.getUsedItemHand();
        if (entity instanceof Player player) {

            BlockHitResult result = RayTraceUtils.getPlayerPOVHitResult(player, level, ClipContext.Fluid.NONE);
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);
            BlockPos pos1 = pos.relative(result.getDirection());

            // check if the selected block can be set on fire
            if (!CampfireBlock.canLight(state) && !CandleBlock.canLight(state) && !CandleCakeBlock.canLight(state) && !(state.getBlock() instanceof ItemPlaceableBaseBlock)) {
                //if it can't, try start fire on block pos relative to selected side
                if (BaseFireBlock.canBePlacedAt(level, pos1, player.getDirection()) && isFireStarted(stack, level, pos1.below())) {
                    removeFlammableItemAbove(level, pos1.below());
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos1, stack);
                    }
                    BlockState state1 = BaseFireBlock.getState(level, pos1);
                    level.playSound(null, pos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.setBlock(pos1, state1, 11);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, pos1);
                    if (player instanceof ServerPlayer) {
                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
            } else {
                // if it can be set on fire, try set it on fire
                if (isFireStarted(stack, level, pos)) {
                    removeFlammableItemAbove(level, pos);
                    level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
                    if (player instanceof ServerPlayer) {
                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
            }
        }

        stack.getOrCreateTag().putBoolean("isUsing", false);
        return stack;
    }

    private static boolean isFireStarted(ItemStack stack, Level level, BlockPos pos) {

        if (hasFlammableItemAbove(level, pos)) {
            removeFlammableItemAbove(level, pos);

            if (level.getRandom().nextDouble() < 0.25) {
                return true;
            }

            // failed
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
        }

        return false;
    }

    // TODO: use item tag to check flammability

    private static boolean hasFlammableItemAbove(Level level, BlockPos pos) {
        List<ItemEntity> itemList = getItemsAtAndAbove(level, pos);

        if (itemList.isEmpty())
            return false;

        return itemList.stream().anyMatch(itemEntity -> itemEntity.getItem().is(AllItems.weedFiber.get()));
    }

    private static void removeFlammableItemAbove(Level level, BlockPos pos) {
        getItemsAtAndAbove(level, pos).stream().filter(itemEntity -> itemEntity.getItem().is(AllItems.weedFiber.get())).forEach(itemEntity -> itemEntity.remove(Entity.RemovalReason.DISCARDED));
    }

    private static List<ItemEntity> getItemsAtAndAbove(Level level, BlockPos pos) {
        AABB range = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D);
        return level.getEntitiesOfClass(ItemEntity.class, range, EntitySelector.ENTITY_STILL_ALIVE);
    }

}
