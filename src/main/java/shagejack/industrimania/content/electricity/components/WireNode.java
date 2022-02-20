package shagejack.industrimania.content.electricity.components;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

public class WireNode extends AbstractComponent {
    public WireNode(Level level, BlockPos pos, NodeMaterial material) {
        super(level, pos, material);
    }

    @Override
    public void update() {
        this.node.current = 0;
    }
}
