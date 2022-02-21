package shagejack.industrimania.content.electricity.components;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.ElectricNode;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

public abstract class AbstractCrossGridComponent extends AbstractComponent {

    public ElectricNode crossNode;

    public AbstractCrossGridComponent(Level level, BlockPos pos, NodeMaterial material) {
        super(level, pos, material);
        this.crossNode = new ElectricNode(level, pos, material, true);
    }

    public abstract void update();

}
