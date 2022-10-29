package shagejack.industrimania.content.world.gen;

import java.util.List;
import java.util.Random;

public enum OreDepositShape {
    ELLIPSOID,
    CYLINDER
    ;

    private static final List<OreDepositShape> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static OreDepositShape getRandom(Random random) {
        return VALUES.get(random.nextInt(SIZE));
    }

    public static OreDepositShape getRandom() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
