package shagejack.industrimania.content.dynamicLights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


import static shagejack.industrimania.content.dynamicLights.DynamicLights.*;


public class DynamicLightsItemEntity extends ItemEntity {

    public DynamicLightsItemEntity(EntityType<? extends ItemEntity> type, Level level) {
        super(type, level);
    }

    public DynamicLightsItemEntity(Level level, double x, double y, double z, ItemStack stack) {
        super(level, x, y, z, stack);
    }

    public DynamicLightsItemEntity(Level level, double x, double y, double z, ItemStack stack, double dx, double dy, double dz) {
        super(level, x, y, z, stack, dx, dy, dz);
    }

    public static DynamicLightsItemEntity createFromItemEntity(ItemEntity entity) {
        Vec3 deltaMovement = entity.getDeltaMovement();
        return new DynamicLightsItemEntity(entity.getLevel(), entity.getX(), entity.getY(), entity.getZ(), entity.getItem(), deltaMovement.x(), deltaMovement.y(), deltaMovement.z());
    }

    @Override
    public void tick(){
        super.tick();

        ItemStack stack = this.getItem();

        if (stack.getItem() instanceof DynamicLightsItem lightsItem) {

            if (!isRemoved() && (!needCheckLight(stack) || lightsItem.isLightUp(stack))) {
                BlockPos pos = this.getOnPos().above();

                if (!level.isEmptyBlock(pos))
                    pos = pos.above();

                if (level.isEmptyBlock(pos) && (!lightsItem.getPrevPos(stack).equals(pos) || !isLit(level, pos))) {
                    addLight(level, pos, getItemLightLevel(stack), this);
                    removeLight(level, lightsItem.getPrevPos(stack));
                    lightsItem.setPrevPos(stack, pos);
                }
            } else {
                removeLight(level, lightsItem.getPrevPos(stack));
            }

            if (lightsItem.isLightUp(stack)) {
                if (lightsItem.shouldDamage(getLevel(), null, stack)) {
                    if (stack.getDamageValue() > 0) {
                        stack.setDamageValue(stack.getDamageValue() - 1);
                    } else {
                        this.setRemoved(RemovalReason.DISCARDED);
                    }
                }
            }

            if (!this.isRemoved()) {
                this.setItem(stack);
            }
        }

    }




}
