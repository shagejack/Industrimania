package shagejack.industrimania.content.electricity;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Electricity {

    private static final ConcurrentHashMap<Level, ConcurrentLinkedQueue<ElectricGrid>> electricGrids = new ConcurrentHashMap<>();

    public static void addGrid(Level level, ElectricGrid grid) {
        ConcurrentLinkedQueue<ElectricGrid> grids = electricGrids.get(level);

        if (grids == null) {
            grids = new ConcurrentLinkedQueue<>();
        }

        if (grids.stream().anyMatch(g -> g == grid)) {
            return;
        }

        grids.add(grid);

        electricGrids.put(level, grids);
    }

    public static void removeGrid(Level level, ElectricGrid grid) {
        ConcurrentLinkedQueue<ElectricGrid> grids = electricGrids.get(level);

        if (grids == null) {
            return;
        }

        grids.removeIf(g -> g == grid);

        electricGrids.put(level, grids);
    }

    @SubscribeEvent
    public static void serverWorldTick(TickEvent.WorldTickEvent event) {

        if (event.side != LogicalSide.SERVER) {
            return;
        }

        ConcurrentLinkedQueue<ElectricGrid> grids = electricGrids.get(event.world);

        if (grids != null) {
            grids.forEach(ElectricGrid::update);
        }

    }
}
