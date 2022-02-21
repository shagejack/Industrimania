package shagejack.industrimania.content.electricity.components;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.electricity.material.NodeMaterial;

public class Transformer extends AbstractCrossGridComponent {

    public int turnsInput;
    public int turnsOutput;

    public Transformer(Level level, BlockPos pos, NodeMaterial material, int turnsInput, int turnsOutput) {
        super(level, pos, material);
        this.turnsInput = turnsInput;
        this.turnsOutput = turnsOutput;
    }

    @Override
    public void update() {
        double multiplier = (double) turnsOutput / (double) turnsInput;

        this.crossNode.voltage = this.node.getVoltage() * multiplier;

        if (this.node.getVoltage() == 0) {
            this.node.current = 0;
            return;
        }

        this.crossNode.genCurrent = Double.POSITIVE_INFINITY;
        this.node.current = this.crossNode.getCurrent() / multiplier;
    }


}
