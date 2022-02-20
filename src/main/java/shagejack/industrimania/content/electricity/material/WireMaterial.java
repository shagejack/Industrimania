package shagejack.industrimania.content.electricity.material;

public interface WireMaterial {
    String getName();

    double getLineLoss();

    double getMeltTemp();

    double getHeatCapacity();

    boolean isSuperConductive();
}
