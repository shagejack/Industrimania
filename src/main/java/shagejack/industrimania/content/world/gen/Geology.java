package shagejack.industrimania.content.world.gen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraftforge.common.Tags;
import shagejack.industrimania.foundation.utility.CrossChunkGenerationHelper;
import shagejack.industrimania.foundation.worldGen.PerlinNoise2D;
import shagejack.industrimania.registries.block.grouped.AllRocks;
import shagejack.industrimania.registries.tags.AllTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author SkyBlade1978
 */
public class Geology {

	private static final int LAYER_THICKNESS = 12;

	private final PerlinNoise2D geomeNoiseLayer;
	private final PerlinNoise2D rockNoiseLayer;
	private final PerlinSimplexNoise intrusionNoise;

	private final CrossChunkGenerationHelper helper = new CrossChunkGenerationHelper();

	private final List<Block> igneousStonesList;
	private final List<Block> metamorphicStonesList;
	private final List<Block> sedimentaryStonesList;


	/** used to reduce game-time computation by pregenerating random numbers */
	private final short[] whiteNoiseArray;
	private final short[] rareStoneWhiteNoiseArray;

	/**
	 * 
	 * @param seed
	 *            World seed
	 * @param geomeSize
	 *            Approximate size of rock type layers (should be much bigger than <code>rockLayerSize</code>
	 * @param rockLayerSize
	 *            Approximate diameter of layers in the X-Z plane
	 */
	public Geology(long seed, double geomeSize, double rockLayerSize) {
		// this.seed = seed;
		int rockLayerUndertones = 4;
		int undertoneMultiplier = 1 << (rockLayerUndertones - 1);
		geomeNoiseLayer = new PerlinNoise2D(~seed, 128, (float) geomeSize, 2);
		rockNoiseLayer = new PerlinNoise2D(seed, (float) (4 * undertoneMultiplier), (float) (rockLayerSize * undertoneMultiplier), rockLayerUndertones);
		intrusionNoise = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(seed + 148932928320L)), ImmutableList.of(0));
		// this.geomeSize = geomeSize;

		igneousStonesList = AllRocks.igneousStones.stream().map((rock) -> rock.block().get()).collect(Collectors.toList());
		metamorphicStonesList = AllRocks.metamorphicStones.stream().map((rock) -> rock.block().get()).collect(Collectors.toList());
		sedimentaryStonesList = AllRocks.sedimentaryStones.stream().map((rock) -> rock.block().get()).collect(Collectors.toList());

		Random r = new Random(seed);
		whiteNoiseArray = new short[320];
		for (int i = 0; i < whiteNoiseArray.length; i++) {
			whiteNoiseArray[i] = (short) r.nextInt(0x7FFF);
		}

		rareStoneWhiteNoiseArray = new short[320];
		for (int i = 0; i < rareStoneWhiteNoiseArray.length; i++) {
			rareStoneWhiteNoiseArray[i] = (short) r.nextInt(0x7FFF);
		}
	}

	public void replaceStoneInChunk(int chunkX, int chunkZ, WorldGenLevel level) {
		ChunkAccess chunk = level.getChunk(chunkX, chunkZ);
		int xOffset = chunkX << 4;
		int zOffset = chunkZ << 4;

		List<BlockPos> intrusionPosList = new ArrayList<>();

		helper.gen(level, new ChunkPos(chunkX, chunkZ));
		helper.tryForceGen();

		// rock layers
		for (int dx = 0; dx < 16; dx++) {
			int x = xOffset | dx;
			for (int dz = 0; dz < 16; dz++) {
				int z = zOffset | dz;
				int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dx, dz);
				while (y > -64 && level.getBlockState(new BlockPos(x, y, z)).isAir()) {
					y--;
				}

				int baseRockVal = (int) rockNoiseLayer.valueAt(x, z);
				int gbase = (int) geomeNoiseLayer.valueAt(x, z);

				if (isIntrusion(x, z)) {
					intrusionPosList.add(new BlockPos(x, y, z));
				}

				for (; y > -64; y--) {
					BlockPos coord = new BlockPos(x, y, z);
					if (chunk.getBlockState(coord).is(Tags.Blocks.STONE)) {
						int geome = gbase + y;
						if (geome < -48) {
							// IGNEOUS
							chunk.setBlockState(coord, pickBlockFromList(baseRockVal + y, igneousStonesList).defaultBlockState(), true);
						} else if (geome < 16) {
							// METAMORPHIC
							chunk.setBlockState(coord, pickBlockFromList(baseRockVal + y, metamorphicStonesList).defaultBlockState(), true);
						} else {
							// SEDIMENTARY
							chunk.setBlockState(coord, pickBlockFromList(baseRockVal + y, sedimentaryStonesList).defaultBlockState(), true);
						}
					}
				}
			}
		}

		// intrusion rock
		for (BlockPos intrusionPos : intrusionPosList) {
			int dy = -63;
			int surface = intrusionPos.getY();
			int r = level.getRandom().nextInt(3, 7);
			BlockState intrusionRock = igneousStonesList.get(whiteNoiseArray[(177 * intrusionPos.getX() + 223 * intrusionPos.getZ()) & 0xFF] % igneousStonesList.size()).defaultBlockState();

			BlockPos pos = new BlockPos(intrusionPos.getX(), dy, intrusionPos.getZ());

			while (r > 0 && pos.getY() < surface) {

				if (level.getBlockState(pos).isAir() && level.getRandom().nextDouble() < 0.25)
					r--;

				for (int rx = -r; rx <= r; rx++) {
					for (int rz = -r; rz <= r; rz++) {
						BlockPos temp = pos.offset(rx, 0, rz);
						if (temp.distSqr(pos) < r * r) {
							helper.offer(level, (l, p) -> !l.getBlockState(p).isAir(), temp, intrusionRock);
						}
					}
				}

				pos = pos.above();

				if (level.getRandom().nextDouble() < 0.05) {
					r--;
				}
			}
		}


		// chunk.setModified(true);
	}

	/**
	 * given any number, this method grabs a block from the list based on that
	 * number.
	 * 
	 * @param value
	 *            product of noise layer + height
	 * @param list
	 * @return
	 */
	private Block pickBlockFromList(int value, List<Block> list) {
		Block stone = list.get(whiteNoiseArray[(value / LAYER_THICKNESS) & 0xFF] % list.size());

		// try to get stone again if it's rare
		if (AllRocks.rareStones.stream().anyMatch(rare -> rare.block().get().equals(stone))) {
			return list.get(rareStoneWhiteNoiseArray[(value / LAYER_THICKNESS) & 0xFF] % list.size());
		}

		return stone;
	}

	private boolean isIntrusion(int x, int z) {
		return intrusionNoise.getValue(x / 10.0D,z / 10.0D,false) > 0.9;
	}
}
