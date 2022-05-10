package shagejack.industrimania.content.world.gen;

import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Range;

public enum OreGrade {
    POOR(0),
    NORMAL(1),
    RICH(2)
    ;

    private final int index;

    OreGrade(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getLocalizedName() {
        return I18n.get("industrimania.oregrade." + getIndex());
    }

    public static OreGrade getGradeFromIndex(@Range(from = 0, to = 2) int index) {
        return switch(index) {
            case 0 -> POOR;
            case 1 -> NORMAL;
            case 2 -> RICH;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }
}
