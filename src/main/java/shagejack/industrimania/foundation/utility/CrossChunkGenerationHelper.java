package shagejack.industrimania.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.world.gen.record.WorldGenBlockReference;
import shagejack.industrimania.content.world.gen.record.WorldGenChunkReference;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * This helper will provide a method to ensure blocks to generate correctly in un-generated chunks,
 * which is realized by trying to generate blocks again when these un-generated chunks generate.
 */
public class CrossChunkGenerationHelper {

    private final ConcurrentHashMap<WorldGenChunkReference, LinkedList<WorldGenBlockReference>> genMap;

    public CrossChunkGenerationHelper() {
        this.genMap = new ConcurrentHashMap<>();
    }

    /**
     * @param level the current world gen level
     * @param chunkPos the current chunk pos
     * @param genFun A double param boolean function that determines if block should generate
     * @param pos actual generation pos
     * @param state generated block state
     */
    public void offer(WorldGenLevel level, ChunkPos chunkPos, BiFunction<WorldGenLevel, BlockPos, Boolean> genFun, BlockPos pos, BlockState state) {
        WorldGenChunkReference chunkRef = new WorldGenChunkReference(level, chunkPos);

        if (!genMap.containsKey(chunkRef)) {
            genMap.put(chunkRef, new LinkedList<>());
        }

        LinkedList<WorldGenBlockReference> queue = genMap.get(chunkRef);

        if (queue != null) {
            queue.offer(new WorldGenBlockReference(genFun, pos, state));
        }
    }

    /**
     * This overloaded method will automatically get chunk pos from the given block pos.
     * @param level the current world gen level
     * @param genFun A double param boolean function that determines if block should generate
     * @param pos actual generation pos
     * @param state generated block state
     */
    public void offer(WorldGenLevel level, BiFunction<WorldGenLevel, BlockPos, Boolean> genFun, BlockPos pos, BlockState state) {
        WorldGenChunkReference chunkRef = new WorldGenChunkReference(level, getChunkPos(pos));

        if (!genMap.containsKey(chunkRef)) {
            genMap.put(chunkRef, new LinkedList<>());
        }

        LinkedList<WorldGenBlockReference> queue = genMap.get(chunkRef);
        if (queue != null) {
            queue.offer(new WorldGenBlockReference(genFun, pos, state));
        }
    }

    public WorldGenBlockReference poll(WorldGenLevel level, ChunkPos chunkPos) {
        WorldGenChunkReference chunkRef = new WorldGenChunkReference(level, chunkPos);

        if (!genMap.containsKey(chunkRef)) {
            genMap.put(chunkRef, new LinkedList<>());
            return null;
        }

        LinkedList<WorldGenBlockReference> queue = genMap.get(chunkRef);
        if (queue != null && !queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    /**
     * This method should be fired every time a new chunk generates.
     * @param level the current world gen level
     * @param chunkPos the current chunk pos
     */
    public void gen(WorldGenLevel level, ChunkPos chunkPos) {

        if (genMap.isEmpty())
            return;

        WorldGenChunkReference chunkRef = new WorldGenChunkReference(level, chunkPos);

        if (!genMap.containsKey(chunkRef))
            return;

        LinkedList<WorldGenBlockReference> queue = genMap.get(chunkRef);

        while(queue != null && !queue.isEmpty()) {
            WorldGenBlockReference ref = queue.poll();
            if (ref.genFun().apply(level, ref.pos()))
                level.setBlock(ref.pos(), ref.state(), 0);
        }

        genMap.remove(chunkRef);

    }

    public boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
        return pos.getX() >= chunkPos.getMinBlockX() && pos.getX() <= chunkPos.getMaxBlockX() && pos.getZ() >= chunkPos.getMinBlockZ() && pos.getZ() <= chunkPos.getMaxBlockZ();
    }

    public ChunkPos getChunkPos(BlockPos pos) {
        return new ChunkPos(pos);
    }

}
