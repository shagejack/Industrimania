package shagejack.industrimania.foundation.multiblock

import net.minecraft.core.Direction

interface IMultiBlockCore {
    val multiBlock: MultiBlock
    val direction : Direction
        get() = Direction.NORTH

    fun isDirectional() : Boolean

    fun tryForm() = multiBlock.checkAll()

    fun isComplete(dir: Direction): Boolean = multiBlock.isComplete(dir)

    fun isComplete() : Boolean = if (isDirectional()) multiBlock.isComplete(direction) else multiBlock.isComplete()

    fun setRemoved() = multiBlock.unregister()
}