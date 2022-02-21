package shagejack.industrimania.content.electricity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.material.WireMaterial;
import shagejack.industrimania.foundation.utility.Pair;

import java.util.Objects;

public class ElectricWire {

    private static long counter = 0;

    private final long seriesNumber;
    public final Pair<ElectricNode, ElectricNode> path;
    public final WireMaterial material;
    public final double size;
    public final double length;

    public double voltage;
    public double current;
    public double resistance;
    public double temperature;

    public ElectricWire(ElectricNode node1, ElectricNode node2, WireMaterial material, double size) {
        this.seriesNumber = ++counter;
        this.path = Pair.of(node1, node2);
        this.material = material;
        this.voltage = 0;
        this.current = 0;
        this.size = size;
        this.length = ElectricNode.getDirectDistance(node1, node2);
        this.resistance = material.getLineLoss() * this.length / Math.pow(size, 2);
        this.temperature = 297.15;
    }

    public Pair<ElectricNode, ElectricNode> getPath() {
        return this.path;
    }

    public ElectricNode getNext(ElectricNode node) {
        return this.path.getFirst() == node ? this.path.getSecond() : this.path.getFirst();
    }

    public double calculateLineLoss() {
        if (material.isSuperConductive()) {
            this.voltage = 0;
            return 0;
        }
        this.voltage = this.resistance * current;
        return this.voltage;
    }

    public void update() {
        if (temperature >= this.material.getMeltTemp()) {
            this.melt();
        } else if (!material.isSuperConductive() && (current > 0 || voltage > 0)) {
            calculateLineLoss();
            this.temperature += Math.pow(current, 2) * material.getLineLoss() / (Math.pow(this.size, 4) * this.material.getHeatCapacity());
        }
        this.temperature -= 0.005 * (this.temperature - 297.15);
    }

    public void melt() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricWire that = (ElectricWire) o;
        return seriesNumber == that.seriesNumber && Objects.equals(path, that.path) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seriesNumber, path, material);
    }

    public double getCurrent() {
        return current;
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
