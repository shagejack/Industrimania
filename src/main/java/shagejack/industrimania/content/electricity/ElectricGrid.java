package shagejack.industrimania.content.electricity;

import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.components.AbstractComponent;
import shagejack.industrimania.content.electricity.components.AbstractCrossGridComponent;

import javax.annotation.Nullable;
import java.util.*;

public class ElectricGrid {

    private static long counter = 0;

    private final long seriesNumber;
    private final Level level;

    public List<AbstractComponent> components = new ArrayList<>();
    public List<ElectricNode> nodes = new ArrayList<>();
    public List<ElectricNode> sources = new ArrayList<>();

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
        return nodes.add(node);
    }

    /**
     * add power source to the grid
     * @return Return true if power source has successfully added. Usually, false return value indicates that the power source already exists in the grid.
     */
    public boolean addSource(ElectricNode source) {
        if (sources.contains(source))
            return false;
        return sources.add(source);
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
            if (source.exceededGenCurrent(updateNode(null, source))) {
                source.voltage = 0;
                source.current = 0;
                updateNode(null, source);
            }
        }

    }

    public double updateNode(@Nullable ElectricWire source, ElectricNode node) {
        if (node == null)
            return 0;

        double currentSum = 0;

        for (ElectricWire wire : node.wires.keySet()) {
            ElectricNode nextNode = node.wires.get(wire);

            if (nextNode != null) {
                wire.current = nextNode.current;
                nextNode.voltage = node.voltage - wire.calculateLineLoss();
                currentSum += updateNode(wire, nextNode);
            }

        }

        if (source == null) {
            node.current = currentSum;
            return currentSum;
        }

        double voltageSum = 0;

        for (ElectricWire prevWire : node.prevWires.keySet()) {
            ElectricNode prevNode = node.prevWires.get(prevWire);
            voltageSum += prevNode.voltage;
        }

        if (voltageSum == 0)
            return 0;

        return (currentSum + node.current) * source.voltage / voltageSum;
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
