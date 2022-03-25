package shagejack.industrimania.foundation.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import shagejack.industrimania.foundation.handler.BlockBreakEventHandler
import java.util.*

/**
 * A MultiBlock Object
 * Default Direction of BlockMap is East (X+)
 * Currently only four-way rotation is available
 */
class MultiBlock(val coreLevel: Level, val corePos: BlockPos, val coreBlock : Block) {

    private val blockMap : HashMap<BlockPos, String> = HashMap()
    private val complete : EnumMap<Direction, Boolean> = EnumMap(Direction::class.java)

    init {
        BlockBreakEventHandler.register(this)
        complete[Direction.EAST] = false
        complete[Direction.SOUTH] = false
        complete[Direction.WEST] = false
        complete[Direction.NORTH] = false
    }

    fun isComplete(direction: Direction) = this.complete[direction]

    fun isComplete() = this.complete.values.any {it}

    fun form(direction: Direction) {
        this.complete[direction] = true
    }

    fun destroy(direction: Direction) {
        this.complete[direction] = false
    }

    fun checkAll(pos : BlockPos = corePos) {
        for(direction in complete.keys) {
            if (direction == Direction.DOWN || direction == Direction.UP)
                continue

            complete[direction] = blockMap.all { (blockPos, block) -> coreLevel.getBlockState(pos.offset(blockPos.getRotated(direction))).block.isNamed(block) }
        }
    }

    fun check(pos: BlockPos) {
        if (!this.contains(pos))
            return

        for(direction in complete.keys) {
            if (direction == Direction.DOWN || direction == Direction.UP)
                continue

            complete[direction] = coreLevel.getBlockState(pos).block.isNamed(blockMap[pos.subtract(corePos).getRotated(direction)])
        }
    }

    fun put(pos : BlockPos, block: String) {
        blockMap[pos] = block
    }

    fun put(posArray: Array<BlockPos>, blockArray: Array<String>) {
        if (posArray.size != blockArray.size)
            throw IllegalArgumentException()

        for(i in posArray.indices) {
            blockMap[posArray[i]] = blockArray[i]
        }
    }

    fun put(array: Array<Pair<BlockPos, String>>) {
        blockMap.putAll(array)
    }

    fun contains(pos: BlockPos) : Boolean {
        for(direction in complete.keys) {
            if (direction == Direction.DOWN || direction == Direction.UP)
                continue

            if (blockMap.contains(pos.getRotated(direction).subtract(corePos)))
                return true
        }
        return false
    }

    /**
     * Currently unable to come up with any usage for this method...
     */
    fun remove(pos : BlockPos) {
        blockMap.remove(pos)
    }

    fun clearMap() {
        blockMap.clear()
    }

    fun getBlockMap() = blockMap

    fun getAllBlockPos() = blockMap.keys

    /**
     * This method should be fired when core block break.
     */
    fun unregister() {
        BlockBreakEventHandler.remove(this)
    }

    fun Block.isNamed(blockName : String?) : Boolean {
        if (blockName == null)
            return false

        return this.registryName.toString().equals(blockName, ignoreCase = true)
    }

    /**
     * Rotating BlockPos
     * around (0, 0, 0)
     * Only four directions(East(X+), South(Z+), West(X-), North(Z-)) available
     */
    fun BlockPos.getRotated(direction: Direction) = when (direction) {
            Direction.EAST -> this
            Direction.SOUTH -> BlockPos(-this.z,this.y, this.x)
            Direction.WEST -> BlockPos(-this.x, this.y, -this.z)
            Direction.NORTH -> BlockPos(this.z, this.y, -this.x)
            else -> throw IllegalArgumentException()
    }

}