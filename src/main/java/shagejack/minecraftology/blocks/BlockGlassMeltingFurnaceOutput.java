package shagejack.minecraftology.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnace;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnaceOutput;

public class BlockGlassMeltingFurnaceOutput extends MCLBlockMachine<TileEntityGlassMeltingFurnaceOutput> implements ITileEntityProvider {

    public BlockGlassMeltingFurnaceOutput(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
    }

    @Override
    public Class<TileEntityGlassMeltingFurnaceOutput> getTileEntityClass() {
        return TileEntityGlassMeltingFurnaceOutput.class;
    }

}
