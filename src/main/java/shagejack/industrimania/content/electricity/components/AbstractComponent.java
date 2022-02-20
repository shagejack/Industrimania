package shagejack.industrimania.content.electricity.components;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.ElectricNode;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

public abstract class AbstractComponent {

    public ElectricNode node;

    public AbstractComponent(Level level, BlockPos pos, NodeMaterial material) {
        this.node = new ElectricNode(level, pos, material);
    }

    public abstract void update();

}
