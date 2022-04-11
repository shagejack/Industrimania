package shagejack.industrimania.content.primalAge.item.fireStarter;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
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

        if (player != null) {
            if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate)) {
                BlockPos blockPos1 = blockPos.relative(context.getClickedFace());
                if (BaseFireBlock.canBePlacedAt(level, blockPos1, context.getHorizontalDirection())) {
                    if (!level.isClientSide()) {
                        ItemStack itemstack = context.getItemInHand();

                        if (isFireStarted(itemstack, level, blockPos1.below())) {
                                removeFlammableItemAbove(level, blockPos1.below());
                                if (player instanceof ServerPlayer) {
                                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos1, itemstack);
                                }
                                BlockState blockState1 = BaseFireBlock.getState(level, blockPos1);
                                level.playSound(null, blockPos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                                level.setBlock(blockPos1, blockState1, 11);
                                level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
                                resetFireCounter(itemstack);
                                resetTick(itemstack);
                            if (player instanceof ServerPlayer) {
                                itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                            }
                        } else {
                            itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                            if (hasFlammableItemAbove(level, blockPos1.below())) {
                                increaseFireCounter(itemstack);
                                level.playSound(null, blockPos1.below(), SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                            } else {
                                resetFireCounter(itemstack);
                            }
                            resetTick(itemstack);
                        }
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                if (!level.isClientSide()) {
                    ItemStack itemstack = context.getItemInHand();

                    if (isFireStarted(itemstack, level, blockPos)) {
                        removeFlammableItemAbove(level, blockPos);
                        level.playSound(null, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                        level.setBlock(blockPos, blockstate.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                        level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
                        context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                    } else {
                        context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                        if (hasFlammableItemAbove(level, blockPos)) {
                            increaseFireCounter(itemstack);
                            level.playSound(null, blockPos, SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                        } else {
                            resetFireCounter(itemstack);
                        }
                        resetTick(itemstack);
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (level.isClientSide())
            return;

        if (getFireCounter(stack) > 0) {
            if (getTick(stack) > 20) {
                resetFireCounter(stack);
                resetTick(stack);
            } else {
                increaseTick(stack);
            }
        }
    }

    private static boolean isFireStarted(ItemStack stack, Level level, BlockPos pos) {
        if (level.isClientSide())
            return false;

        if (getFireCounter(stack) >= 18) {
            resetFireCounter(stack);
            resetTick(stack);

            if (hasFlammableItemAbove(level, pos)) {
                if (level.getRandom().nextDouble() < 0.25) {
                    return true;
                }

                removeFlammableItemAbove(level, pos);

                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            }
        }
        return false;
    }

    private static boolean hasFlammableItemAbove(Level level, BlockPos pos) {
        List<ItemEntity> itemList = getItemsAtAndAbove(level, pos);

        if (itemList.isEmpty())
            return false;

        return itemList.stream().anyMatch(itemEntity -> itemEntity.getItem().is(AllItems.weedFiber.get()));
    }

    private static void removeFlammableItemAbove(Level level, BlockPos pos) {
        getItemsAtAndAbove(level, pos).stream().filter(itemEntity -> itemEntity.getItem().is(AllItems.weedFiber.get())).forEach(itemEntity -> itemEntity.remove(Entity.RemovalReason.KILLED));
    }

    private static List<ItemEntity> getItemsAtAndAbove(Level level, BlockPos pos) {
        AABB range = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D);
        return level.getEntitiesOfClass(ItemEntity.class, range, EntitySelector.ENTITY_STILL_ALIVE);
    }

    public static void setTick(ItemStack stack, int tick) {
        stack.getOrCreateTag().putInt("Tick", tick);
    }

    public static void setFireCounter(ItemStack stack, int fireCounter) {
        stack.getOrCreateTag().putInt("FireCounter", fireCounter);
    }

    public static int getTick(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Tick");
    }

    public static int getFireCounter(ItemStack stack) {
        return stack.getOrCreateTag().getInt("FireCounter");
    }

    public static void resetFireCounter(ItemStack stack) {
        setFireCounter(stack, 0);
    }

    public static void resetTick(ItemStack stack) {
        setTick(stack, 0);
    }

    public static void increaseFireCounter(ItemStack stack) {
        setFireCounter(stack, getFireCounter(stack) + 1);
    }

    public static void increaseTick(ItemStack stack) {
        setTick(stack, getTick(stack) + 1);
    }

}
