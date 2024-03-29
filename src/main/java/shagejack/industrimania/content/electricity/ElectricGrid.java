package shagejack.industrimania.content.electricity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.components.AbstractComponent;
import shagejack.industrimania.content.electricity.components.AbstractCrossGridComponent;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

import javax.annotation.Nullable;
import java.util.*;

public class ElectricGrid {

    private static long counter = 0;

    private final long seriesNumber;
    private final Level level;

    public Set<AbstractComponent> components = new HashSet<>();
    public Set<ElectricNode> nodes = new HashSet<>();
    public Set<ElectricNode> sources = new HashSet<>();

    public ElectricGrid(Level level) {
        this.seriesNumber = ++counter;
        this.level = level;
        Electricity.addGrid(level, this);
    }

    public void update() {
        updateComponents();
        updateNodes();
        updateWires();

        if (nodes.isEmpty())
            Electricity.removeGrid(level, this);

    }

    /**
     * add component to the grid
     * this method will add nodes of component at the same time
     * @return Return true if component has successfully added. Usually, false return value indicates that the component already exists in the grid.
     */
    public <T extends AbstractComponent> boolean addComponent(T component) {
        if (components.contains(component))
            return false;
        addNode(component.node);
        return components.add(component);
    }

    public <T extends AbstractCrossGridComponent> boolean addCrossGridComponent(T component) {
        if (components.contains(component))
            return false;
        addNode(component.crossNode);
        return components.add(component);
    }

    /**
     * add node to the grid
     * @return Return true if node has successfully added. Usually, false return value indicates that the node already exists in the grid.
     */
    public boolean addNode(ElectricNode node) {
        if (nodes.contains(node))
            return false;

        if (node.isPowerSource())
            sources.add(node);

        return nodes.add(node);
    }

    public boolean setSourceSeriesConnected(Level level, BlockPos pos, NodeMaterial material, ElectricNode... node) {
        if (!nodes.containsAll(Arrays.asList(node)))
            return false;

        SeriesConnectedPowerSource source = new SeriesConnectedPowerSource(level, pos, material);

        for (ElectricNode n : node) {
            nodes.remove(n);
            source.addNode(n);
        }

        source.setupWires();

        return addNode(source);
    }

    public void breakSourceSeriesConnection(ElectricNode... node) {
        for (ElectricNode source : sources) {
            if (source instanceof SeriesConnectedPowerSource) {
                if (Arrays.stream(node).anyMatch(n -> ((SeriesConnectedPowerSource) source).nodes.contains(n))) {
                    sources.addAll(((SeriesConnectedPowerSource) source).nodes);
                    sources.remove(source);
                }
            }
        }
    }

    public Set<ElectricWire> getWires() {
        Set<ElectricWire> wires = new HashSet<>();
        nodes.forEach(node -> wires.addAll(node.wires.keySet()));
        return wires;
    }

    public void DFS(ElectricNode node, Set<ElectricNode> visited, ArrayList<ElectricNode> sorted) {
        if ( visited.contains(node) ) return;
        visited.add(node);
        if ( node.getWires() != null ) {
            for (ElectricNode target : node.wires.values()) {
                DFS(target, visited, sorted);
            }
        }
        sorted.add(node);
    }

    public void updateNodes() {
        updateFromSource();
    }

    public void updateFromSource() {
        for(ElectricNode source : sources) {
            Set<ElectricNode> visited = new HashSet<>();
            visited.add(source);
            if (source.exceededGenCurrent(updateNode(null, source, visited, false))) {
                Set<ElectricNode> visitedShutdown = new HashSet<>();
                visitedShutdown.add(source);
                updateNode(null, source, visitedShutdown, true);
            }
        }

    }

    public double updateNode(@Nullable ElectricWire source, ElectricNode node, Set<ElectricNode> visited, boolean shutdown) {
        if (node == null)
            return 0;

        double currentSum = 0;

        for (ElectricWire wire : node.wires.keySet()) {
            ElectricNode nextNode = node.wires.get(wire);

            if (visited.contains(node)) {
                node.melt();
                return 0;
            }

            visited.add(node);

            if (nextNode != null) {
                if (!shutdown) {
                    wire.current = nextNode.getCurrent();
                    nextNode.voltage = node.getVoltage() - wire.calculateLineLoss();
                    currentSum += updateNode(wire, nextNode, visited,false);
                } else {
                    wire.current = 0;
                    wire.voltage = 0;
                    node.current = 0;
                    node.voltage = 0;
                    nextNode.current = 0;
                    nextNode.voltage = 0;
                    updateNode(wire, nextNode, visited,true);
                }
            }
        }

        if (shutdown)
            return 0;

        if (source == null) {
            node.current = currentSum;
            return currentSum;
        }

        double voltageSum = 0;

        for (ElectricWire prevWire : node.prevWires.keySet()) {
            ElectricNode prevNode = node.prevWires.get(prevWire);
            voltageSum += prevNode.getVoltage();
        }

        if (voltageSum == 0)
            return 0;

        return (currentSum + node.getCurrent()) * source.getVoltage() / voltageSum;
    }

    public void updateWires() {
        getWires().forEach(ElectricWire::update);
    }

    public void updateComponents() {
        components.forEach(AbstractComponent::update);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricGrid that = (ElectricGrid) o;
        return seriesNumber == that.seriesNumber && level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seriesNumber, level);
    }
}
