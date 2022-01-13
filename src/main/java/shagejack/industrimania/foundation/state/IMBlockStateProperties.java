package shagejack.industrimania.foundation.state;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class IMBlockStateProperties {
    public static final EnumProperty CONTAIN_ITEM = EnumProperty.create("contain_item", ContainItems.class);
    public static final EnumProperty CONTAIN_WOOD = EnumProperty.create("contain_wood", ContainWood.class);
    public static final IntegerProperty CONTAIN_SIZE = IntegerProperty.create("contain_size", 0, 4);
}
