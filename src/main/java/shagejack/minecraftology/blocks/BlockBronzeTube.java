package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.util.MCLBlockHelper;

public class BlockBronzeTube extends MCLBlock {

    public BlockBronzeTube(Material material, String name) {
        super(material, name);
        setHardness(1.0F);
        setHarvestLevel("pickaxe", 1);
        setResistance(4.0F);
        setCreativeTab(Minecraftology.TAB_Minecraftology);
        setHasRotation();
        setRotationType(MCLBlockHelper.RotationType.FOUR_WAY);
        setSoundType(SoundType.GROUND);
    }

}
