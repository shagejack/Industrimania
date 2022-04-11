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

import java.util.stream.StreamSupport;

import static shagejack.industrimania.content.dynamicLights.DynamicLights.*;

public class HandOilLamp extends Item {

    public HandOilLamp(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {

        if (isLightUp(stack)) {
            if (entity.isAlive() && StreamSupport.stream(entity.getHandSlots().spliterator(), false).anyMatch(itemStack -> itemStack == stack)) {
                BlockPos pos = entity.getOnPos().above();

                if (!level.isEmptyBlock(pos))
                    pos = pos.above();

                if (level.isEmptyBlock(pos) && (!getPrevPos(stack).equals(pos) || !DynamicLights.isLit(level, pos))) {
                    DynamicLights.addLight(level, pos, 7, entity);
                    DynamicLights.removeLight(level, getPrevPos(stack));
                    setPrevPos(stack, pos);
                }
            }

            if (level.getRandom().nextDouble() < 0.01) {
                ItemStack stackBackup = stack.copy();
                stack.hurtAndBreak(1, (LivingEntity) entity, (e) -> {
                    DynamicLights.removeLight(level, getPrevPos(stackBackup));
                });
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

    /*
    public static boolean isLightUp(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("IsLightUp");
    }

    public static void setLightUp(ItemStack stack, boolean light) {
        stack.getOrCreateTag().putBoolean("IsLightUp", light);
    }

    public static BlockPos getPrevPos(ItemStack stack) {
        return new BlockPos(
                stack.getOrCreateTag().getInt("PrevPosX"),
                stack.getOrCreateTag().getInt("PrevPosY"),
                stack.getOrCreateTag().getInt("PrevPosZ")
        );
    }

    public static void setPrevPos(ItemStack stack, BlockPos pos) {
        stack.getOrCreateTag().putInt("PrevPosX", pos.getX());
        stack.getOrCreateTag().putInt("PrevPosY", pos.getY());
        stack.getOrCreateTag().putInt("PrevPosZ", pos.getZ());
    }
     */





}
