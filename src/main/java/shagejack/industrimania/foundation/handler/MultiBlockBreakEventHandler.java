package shagejack.industrimania.foundation.handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import shagejack.industrimania.foundation.multiblock.MultiBlock;

import java.util.HashSet;

public class MultiBlockBreakEventHandler {

    private static final HashSet<MultiBlock> registeredMultiBlocks = new HashSet<>();
    private static int IDLE = 6000;

    @SubscribeEvent
    public static void blockBreak(final BlockEvent.BreakEvent event) {
        registeredMultiBlocks.forEach(multiBlock -> {if (multiBlock.getCoreLevel() == event.getWorld()) multiBlock.check(event.getPos());});
    }

    @SubscribeEvent
    public static void serverWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side.isClient())
            return;

        if (IDLE > 0) {
            IDLE--;
            return;
        }

        cleanMap();
        IDLE = 6000;
    }

    public static boolean register(MultiBlock multiBlock) {
        return registeredMultiBlocks.add(multiBlock);
    }

    public static boolean remove(MultiBlock multiBlock) {
        return registeredMultiBlocks.remove(multiBlock);
    }

    public static void clear() {
        registeredMultiBlocks.clear();
    }

    public static void cleanMap() {
        registeredMultiBlocks.removeIf(multiBlock -> multiBlock.getCoreLevel().getBlockState(multiBlock.getCorePos()) != multiBlock.getCoreBlock());
    }

}
