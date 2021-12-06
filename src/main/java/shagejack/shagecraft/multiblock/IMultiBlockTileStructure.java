package shagejack.shagecraft.multiblock;

public interface IMultiBlockTileStructure {
    boolean addMultiBlockTile(IMultiBlockTile block);

    void removeMultiBlockTile(IMultiBlockTile block);

    boolean containsMultiBlockTile(IMultiBlockTile block);
}
