package shagejack.minecraftology.client.render.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import shagejack.minecraftology.tile.TileEntityFilingTable;
import shagejack.minecraftology.tile.TileEntityForge;

public class TileEntityRendererFilingTable extends MCLTileEntityRendererBase<TileEntityFilingTable> {
    private EntityItem item;

    @Override
    public void render(TileEntityFilingTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (!te.getStackInSlot(0).isEmpty())
            renderStillItem(te, te.getStackInSlot(0), 0.5F, 1.15F, 0.5F, 2F);
        GlStateManager.popMatrix();

        super.render(te, x, y + 1, z, partialTicks, destroyStage, alpha);
    }

}
