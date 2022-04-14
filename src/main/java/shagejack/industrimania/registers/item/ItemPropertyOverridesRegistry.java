package shagejack.industrimania.registers.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.Tag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.block.AllBlocks;

public class ItemPropertyOverridesRegistry {

    @SubscribeEvent
    public static void propertyOverrideRegistry(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(AllBlocks.mechanic_wooden_barrel.item().get(), Industrimania.asResource("open"), (itemStack, clientLevel, livingEntity, entityId) -> {
                if (itemStack.getOrCreateTag().contains("Open", Tag.TAG_BYTE)) {
                    return itemStack.getOrCreateTag().getBoolean("Open") ? 1.0F : 0.0F;
                }
                return 1.0F;
            });
        });
    }


}
