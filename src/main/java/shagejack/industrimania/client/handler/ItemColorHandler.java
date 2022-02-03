package shagejack.industrimania.client.handler;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ItemColorHandler {

    private static final Map<RegistryObject<Item>, ItemColor> itemColorMap = new HashMap<>();

    @SubscribeEvent
    public static void registerItemColors(final ColorHandlerEvent.Item event)
    {
        itemColorMap.forEach((item, itemColor) -> event.getItemColors().register(itemColor, item.get()));
    }

    public static void register(RegistryObject<Item> item, ItemColor itemColor) {
        itemColorMap.put(item, itemColor);
    }

    public static void register(RegistryObject<Item> item, Color color) {
        itemColorMap.put(item, (stack, p_92673_) -> color.getRGB());
    }

}
