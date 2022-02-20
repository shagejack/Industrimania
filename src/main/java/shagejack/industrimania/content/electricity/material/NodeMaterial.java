package shagejack.industrimania.content.electricity.material;

public interface NodeMaterial {
    String getName();

    double getResistance();

    double getMeltTemp();

    double getHeatCapacity();

    boolean isSuperConductive();
}
