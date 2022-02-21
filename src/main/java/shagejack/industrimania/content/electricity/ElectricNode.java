package shagejack.industrimania.content.electricity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.material.NodeMaterial;
import shagejack.industrimania.foundation.utility.Pair;

import java.util.*;


public class ElectricNode {

    private static long counter = 0;

    private final long seriesNumber;
    private final NodeMaterial material;

    public Level level;
    public BlockPos pos;

    public double voltage;
    public double current;
    public double resistance;
    public double temperature;

    public Map<ElectricWire, ElectricNode> wires;
    public Map<ElectricWire, ElectricNode> prevWires;

    public double genCurrent;
    public boolean isPowerSource;

    public ElectricNode(Level level, BlockPos pos, NodeMaterial material) {
        this.level = level;
        this.seriesNumber = ++counter;
        this.material = material;
        this.pos = pos;
        this.voltage = 0;
        this.current = 0;
        this.resistance = 0;
        this.wires = new HashMap<>();
        this.genCurrent = 0;
        this.temperature = 297.15;
        this.isPowerSource = false;
    }

    public ElectricNode(Level level, BlockPos pos, NodeMaterial material, boolean isPowerSource) {
        this.level = level;
        this.seriesNumber = ++counter;
        this.material = material;
        this.pos = pos;
        this.voltage = 0;
        this.current = 0;
        this.resistance = 0;
        this.wires = new HashMap<>();
        this.genCurrent = 0;
        this.isPowerSource = isPowerSource;
        this.temperature = 297.15;
    }

    public static double getDirectDistance(ElectricNode node1, ElectricNode node2) {
        BlockPos pos1 = node1.getPos();
        BlockPos pos2 = node2.getPos();
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2) + Math.pow(pos1.getZ() - pos2.getZ(), 2));
    }

    public boolean exceededGenCurrent(double current) {
        return Math.abs(current) > this.getGenCurrent();
    }

    public void update() {
        if (temperature >= this.material.getMeltTemp()) {
            this.melt();
        } else if (!material.isSuperConductive() && (getCurrent() > 0 || getVoltage() > 0)) {
            //this.temperature +=...
        }
        this.temperature -= 0.005 * (this.temperature - 297.15);
    }

    public void melt() {

    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Map<ElectricWire, ElectricNode> getWires() {
        return this.wires;
    }

    public boolean addWire(ElectricWire wire) {
        if (this.wires.containsKey(wire))
            return false;

        Pair<ElectricNode, ElectricNode> path = wire.getPath();

        if (wires.keySet().stream().anyMatch(w -> w.getPath() == path || (w.getPath().getFirst() == path.getSecond() && w.getPath().getSecond() == path.getFirst())))
            return false;

        this.wires.put(wire, wire.getNext(this));
        wire.getNext(this).addPrevWire(wire);

        return true;
    }

    public boolean addPrevWire(ElectricWire wire) {
        if (this.wires.containsKey(wire))
            return false;

        Pair<ElectricNode, ElectricNode> path = wire.getPath();

        if (wires.keySet().stream().anyMatch(w -> w.getPath() == path || (w.getPath().getFirst() == path.getSecond() && w.getPath().getSecond() == path.getFirst())))
            return false;

        this.wires.put(wire, wire.getNext(this));
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricNode that = (ElectricNode) o;
        return seriesNumber == that.seriesNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seriesNumber);
    }

    public double getCurrent() {
        return current;
    }

    public double getGenCurrent() {
        return genCurrent;
    }

    public boolean isPowerSource() {
        return isPowerSource();
    }

    public double getResistance() {
        return resistance;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setGenCurrent(double genCurrent) {
        this.genCurrent = genCurrent;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
