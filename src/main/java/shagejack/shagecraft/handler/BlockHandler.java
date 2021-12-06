package shagejack.shagecraft.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shagejack.shagecraft.Reference;

public class BlockHandler {
    @SubscribeEvent
    public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() != null) {
        }
    }

    @SubscribeEvent
    public void onBlockPlaceEvent(BlockEvent.PlaceEvent event) {
        if (event.getPlayer() != null) {
            ResourceLocation blockName = event.getState().getBlock().getRegistryName();
            if (blockName.getNamespace().equals(Reference.MOD_ID)) {
            }
        }
    }
}
