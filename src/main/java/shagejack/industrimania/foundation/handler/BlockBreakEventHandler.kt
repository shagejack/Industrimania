package shagejack.industrimania.foundation.handler

import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.world.BlockEvent.BreakEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import shagejack.industrimania.foundation.multiblock.MultiBlock

class BlockBreakEventHandler {

    companion object {
        private val registeredMultiBlocks: MutableSet<MultiBlock> = HashSet()
        private var IDLE = 6000;

        @JvmStatic
        @SubscribeEvent
        fun blockBreak(event: BreakEvent) {
            registeredMultiBlocks.forEach { multiBlock -> if (multiBlock.coreLevel == event.world) multiBlock.check(event.pos) }
        }

        @JvmStatic
        @SubscribeEvent
        fun serverWorldTick(event: TickEvent.WorldTickEvent) {
            if (event.side != LogicalSide.SERVER)
                return

            if (IDLE > 0) {
                IDLE--
                return
            }

            cleanMap()
            IDLE = 6000
        }

        fun register(multiBlock: MultiBlock) : Boolean {
            return registeredMultiBlocks.add(multiBlock)
        }

        fun remove(multiBlock : MultiBlock) : Boolean {
            return registeredMultiBlocks.remove(multiBlock)
        }

        fun clear() {
            registeredMultiBlocks.clear()
        }

        fun cleanMap() {
            registeredMultiBlocks.removeIf { multiBlock -> multiBlock.coreLevel.getBlockState(multiBlock.corePos) != multiBlock.coreBlock }
        }
    }

}