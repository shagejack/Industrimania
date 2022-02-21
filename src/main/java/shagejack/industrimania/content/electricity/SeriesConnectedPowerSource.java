package shagejack.industrimania.content.electricity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeriesConnectedPowerSource extends ElectricNode {

    private static long counter = 0;

    private final long seriesNumber;

    Set<ElectricNode> nodes;


    public SeriesConnectedPowerSource(Level level, BlockPos pos, NodeMaterial material) {
        super(level, pos, material, true);
        seriesNumber = ++counter;
    }

    public boolean addNode(ElectricNode node) {
        if (!node.isPowerSource())
            return false;

        if (nodes.contains(node))
            return false;

        return nodes.add(node);
    }

    public void setupWires() {
        wires.clear();
        for (ElectricNode node : nodes) {
            for (ElectricWire wire : node.getWires().keySet()) {
                if (nodes.contains(wire.getNext(node)) || wires.containsKey(wire))
                    continue;
                wires.put(wire, wire.getNext(node));
            }
        }
    }

    @Override
    public void update() {

    }


    //TODO: calculate series connected nodes data

    @Override
    public double getGenCurrent() {
        return genCurrent;
    }

    @Override
    public double getResistance() {
        double resSum = 0;
        for (ElectricNode node : nodes) {
            resSum += node.getResistance();
        }
        return resSum;
    }

    @Override
    public double getVoltage() {
        double voltSum = 0;
        for (ElectricNode node : nodes) {
            voltSum += node.getVoltage();
        }
        for (ElectricNode node : nodes) {
            Set<ElectricWire> visited = new HashSet<>();
            for (ElectricWire wire : node.getWires().keySet()) {
                if (!visited.contains(wire)) {
                    wire.setCurrent(getCurrent());
                    voltSum -= wire.calculateLineLoss();
                    visited.add(wire);
                }
            }
        }
        return voltSum;
    }

    @Override
    public double getCurrent() {
        return current;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    public long getSeriesNumber() {
        return this.seriesNumber;
    }
}
