package shagejack.industrimania.content.electricity.components;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

public class Source extends AbstractComponent {
    public Source(Level level, BlockPos pos, NodeMaterial material) {
        super(level, pos, material, true);
    }

    @Override
    public void update() {

    }
}
