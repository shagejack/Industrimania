package shagejack.industrimania.content.misc.water;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class DirtyWaterBottleItem extends Item {
    public DirtyWaterBottleItem(Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 1));
        }

        return new ItemStack(Items.GLASS_BOTTLE);
    }

    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

}
