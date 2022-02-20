package shagejack.industrimania.client.handler;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BlockColorHandler {

    private static final Map<RegistryObject<Block>, BlockColor> blockColorMap = new HashMap<>();

    @SubscribeEvent
    public static void registerBlockColors(final ColorHandlerEvent.Block event)
    {
        blockColorMap.forEach((block, blockColor) -> event.getBlockColors().register(blockColor, block.get()));
    }

    public static void register(RegistryObject<Block> block, BlockColor blockColor) {
        blockColorMap.put(block, blockColor);
    }

    public static void register(RegistryObject<Block> block, Color color) {
        blockColorMap.put(block, (state, blockAndTintGetter, pos, p_92649_) -> color.getRGB());
    }

}
