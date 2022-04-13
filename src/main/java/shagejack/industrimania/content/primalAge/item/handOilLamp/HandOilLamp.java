package shagejack.industrimania.content.primalAge.item.handOilLamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.dynamicLights.DynamicLights;
import shagejack.industrimania.content.dynamicLights.DynamicLightsItem;

import javax.annotation.Nullable;
import java.util.stream.StreamSupport;

public class HandOilLamp extends Item implements DynamicLightsItem {

    public HandOilLamp(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {

        if (isLightUp(stack)) {
            if (entity.isAlive() && StreamSupport.stream(entity.getHandSlots().spliterator(), false).anyMatch(itemStack -> itemStack.equals(stack))) {
                BlockPos pos = entity.getOnPos().above();

                if (!level.isEmptyBlock(pos))
                    pos = pos.above();

                if (level.isEmptyBlock(pos) && (!getPrevPos(stack).equals(pos) || !DynamicLights.isLit(level, pos))) {
                    DynamicLights.addLight(level, pos, 7, entity);
                    DynamicLights.removeLight(level, getPrevPos(stack));
                    setPrevPos(stack, pos);
                }
            } else {
                DynamicLights.removeLight(level, getPrevPos(stack));
            }

            if (shouldDamage(level, entity instanceof Player ? (Player) entity : null, stack)) {
                ItemStack stackBackup = stack.copy();
                stack.hurtAndBreak(1, (LivingEntity) entity, (e) -> DynamicLights.removeLight(level, getPrevPos(stackBackup)));
            }

        } else {
            DynamicLights.removeLight(level, getPrevPos(stack));
        }

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown())
            setLightUp(stack, !isLightUp(stack));

        return super.use(level, player, hand);
    }


    @Override
    public boolean isLightUp(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("IsLightUp");
    }

    @Override
    public void setLightUp(ItemStack stack, boolean light) {
        stack.getOrCreateTag().putBoolean("IsLightUp", light);
    }

    @Override
    public BlockPos getPrevPos(ItemStack stack) {
        return new BlockPos(
                stack.getOrCreateTag().getInt("PrevPosX"),
                stack.getOrCreateTag().getInt("PrevPosY"),
                stack.getOrCreateTag().getInt("PrevPosZ")
        );
    }

    @Override
    public void setPrevPos(ItemStack stack, BlockPos pos) {
        stack.getOrCreateTag().putInt("PrevPosX", pos.getX());
        stack.getOrCreateTag().putInt("PrevPosY", pos.getY());
        stack.getOrCreateTag().putInt("PrevPosZ", pos.getZ());
    }

    @Override
    public boolean shouldDamage(Level level, @Nullable Player player, ItemStack stack) {
        if (player == null)
            return level.getRandom().nextDouble() < 0.01;

        if (player.isCreative())
            return false;

        return level.getRandom().nextDouble() < 0.01;
    }






}
