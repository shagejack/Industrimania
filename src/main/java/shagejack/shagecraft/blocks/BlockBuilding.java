package shagejack.shagecraft.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.blocks.includes.ShageBlock;
import shagejack.shagecraft.util.ShageBlockHelper;

public class BlockBuilding extends ShageBlock {


    public BlockBuilding(Material material, String name, float hardness, int harvestLevel, float resistance, SoundType soundtype) {
        super(material, name);
        setHardness(hardness);
        setHarvestLevel("pickaxe", harvestLevel);
        setResistance(resistance);
        setCreativeTab(Shagecraft.TAB_Shagecraft);
        setRotationType(ShageBlockHelper.RotationType.PREVENT);
        setSoundType(soundtype);
    }

}
