package shagejack.industrimania.content.electricity.material;

public enum NodeMaterials implements NodeMaterial{
    COPPER("copper", 1, 1085, 386);
    ;

    private final String name;
    private final double resistance;
    private final double meltTemp;
    private final double heatCapacity;

    NodeMaterials(String name, double resistance, double meltTemp, double heatCapacity) {
        this.name = name;
        this.resistance = resistance;
        this.meltTemp = meltTemp;
        this.heatCapacity = heatCapacity;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getResistance() {
        return this.resistance;
    }

    @Override
    public double getMeltTemp() {
        return meltTemp;
    }

    @Override
    public double getHeatCapacity() {
        return heatCapacity;
    }

    @Override
    public boolean isSuperConductive() {
        return this.resistance == -1;
    }
}
