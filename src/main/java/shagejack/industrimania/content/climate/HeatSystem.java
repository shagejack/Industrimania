package shagejack.industrimania.content.climate;

import net.minecraft.server.level.ServerLevel;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.foundation.utility.Vector3;

public class HeatSystem {

    public static double getBlockTemp(ServerLevel level, Vector3 pos) {
		long time = System.nanoTime();
		try {
			// TODO: biome temperature & world climate & make this work on an independent thread
			return new HeatSimulator(level, pos.toBlockPos(), 25.0D).getBlockTemperature(pos);

		} finally {
			long delta = System.nanoTime() - time;
			Industrimania.LOGGER.debug("total cost {} ms", delta / 1000000.0);
		}
    }


}
