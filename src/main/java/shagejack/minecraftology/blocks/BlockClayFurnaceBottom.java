package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.util.MCLBlockHelper;

public class BlockClayFurnaceBottom extends MCLBlockContainer<TileEntityClayFurnaceBottom> {

    public BlockClayFurnaceBottom(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.GROUND);
        setResistance(4.0F);
    }

    @Override
    public Class<TileEntityClayFurnaceBottom> getTileEntityClass() {
        return TileEntityClayFurnaceBottom.class;
    }

}
