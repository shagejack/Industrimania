package shagejack.industrimania.foundation.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import shagejack.industrimania.content.dynamicLights.DynamicLights;
import shagejack.industrimania.content.dynamicLights.DynamicLightsItemEntity;

public class DynamicLightsItemJoinWorldHandler {

    @SubscribeEvent
    public static void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        Level level = event.getWorld();
        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity itemEntity && !(entity instanceof DynamicLightsItemEntity)) {
            if (DynamicLights.isDynamicLightsItem(itemEntity.getItem())) {
                DynamicLightsItemEntity dynamicLightsItemEntity = DynamicLightsItemEntity.createFromItemEntity(itemEntity);
                dynamicLightsItemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(dynamicLightsItemEntity);
                event.setCanceled(true);
            }
        }
    }
}
