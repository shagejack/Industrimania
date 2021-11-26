package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.util.MCLBlockHelper;

public class BlockBuilding extends MCLBlock {


    public BlockBuilding(Material material, String name, float hardness, int harvestLevel, float resistance, SoundType soundtype) {
        super(material, name);
        setHardness(hardness);
        setHarvestLevel("pickaxe", harvestLevel);
        setResistance(resistance);
        setCreativeTab(Minecraftology.TAB_Minecraftology);
        setRotationType(MCLBlockHelper.RotationType.PREVENT);
        setSoundType(soundtype);
    }

}
