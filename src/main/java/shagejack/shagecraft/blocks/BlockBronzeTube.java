package shagejack.shagecraft.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.blocks.includes.ShageBlock;
import shagejack.shagecraft.util.ShageBlockHelper;

public class BlockBronzeTube extends ShageBlock {

    public BlockBronzeTube(Material material, String name) {
        super(material, name);
        setHardness(1.0F);
        setHarvestLevel("pickaxe", 1);
        setResistance(4.0F);
        setCreativeTab(Shagecraft.TAB_Shagecraft);
        setHasRotation();
        setRotationType(ShageBlockHelper.RotationType.FOUR_WAY);
        setSoundType(SoundType.GROUND);
    }

}
