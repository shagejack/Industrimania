package shagejack.industrimania.content.electricity.material;

public enum WireMaterials implements WireMaterial{
    COPPER("copper", 0.1, 1085, 10.61);
    ;

    private final String name;
    private final double lineLoss;
    private final double meltTemp;
    private final double heatCapacity;

    WireMaterials(String name, double lineLoss, double meltTemp, double heatCapacity) {
        this.name = name;
        this.lineLoss = lineLoss;
        this.meltTemp = meltTemp;
        this.heatCapacity = heatCapacity;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getLineLoss() {
        return this.lineLoss;
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
        return this.lineLoss == -1;
    }
}
