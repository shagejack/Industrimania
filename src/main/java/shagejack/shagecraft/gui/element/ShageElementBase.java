package shagejack.shagecraft.gui.element;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import shagejack.shagecraft.client.data.Color;
import shagejack.shagecraft.gui.ShageGuiBase;
import shagejack.shagecraft.util.RenderUtils;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class ShageElementBase {
    protected final ShageGuiBase gui;
    public ShageElementBase parent;
    protected ResourceLocation texture;
    protected int posX;
    protected int posY;
    protected int posZ;
    protected int sizeX;
    protected int sizeY;
    protected int texW = 256;
    protected int texH = 256;
    protected String name;
    private FontRenderer fontRenderer;
    private boolean visible = true;
    private boolean enabled = true;
    private Color color = new Color(255, 255, 255);

    public ShageElementBase(ShageGuiBase gui, int posX, int posY) {
        this.gui = gui;
        this.posX = posX;
        this.posY = posY;
    }

    public ShageElementBase(ShageGuiBase gui, int posX, int posY, int width, int height) {
        this.gui = gui;
        this.posX = posX;
        this.posY = posY;
        this.sizeX = width;
        this.sizeY = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int r, int g, int b, int alpha) {
        this.color = new Color(r, g, b, alpha);
    }

    protected void ApplyColor() {
        if (color != null) {
            RenderUtils.applyColor(color);
        }
    }

    protected void ResetColor() {
        GlStateManager.color(1, 1, 1);
    }

    protected int getGlobalX() {
        int x = posX;

        if (parent != null) {
            x += parent.getGlobalX();
        }
        return x;
    }

    protected int getGlobalY() {
        int y = posY;

        if (parent != null) {
            y += parent.getGlobalY();
        }
        return y;
    }

    public ShageElementBase setPosition(int var1, int var2) {
        this.posX = var1;
        this.posY = var2;
        return this;
    }

    public ShageElementBase setSize(int var1, int var2) {
        this.sizeX = var1;
        this.sizeY = var2;
        return this;
    }

    public ShageElementBase setTexture(String var1, int var2, int var3) {
        this.texture = new ResourceLocation(var1);
        this.texW = var2;
        this.texH = var3;
        return this;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public final ShageElementBase setVisible(boolean var1) {
        this.visible = var1;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public final ShageElementBase setEnabled(boolean var1) {
        this.enabled = var1;
        return this;
    }

    public void update(int mouseX, int mouseY, float partialTicks) {
        this.update();
    }

    public void update() {
    }

    public abstract void updateInfo();

    public abstract void init();

    public abstract void addTooltip(List<String> var1, int mouseX, int mouseY);

    public abstract void drawBackground(int var1, int var2, float var3);

    public abstract void drawForeground(int var1, int var2);

    public void drawModalRect(int var1, int var2, int var3, int var4, int var5) {
        this.gui.drawSizedModalRect(var1, var2, var3, var4, var5);
    }

    public void drawStencil(int xStart, int yStart, int xEnd, int yEnd, int flag) {
        GlStateManager.disableTexture2D();
        glStencilFunc(GL_ALWAYS, flag, flag);
        glStencilOp(GL_ZERO, GL_ZERO, GL_REPLACE);
        glStencilMask(flag);
        GlStateManager.colorMask(false, false, false, false);
        GlStateManager.depthMask(false);
        glClearStencil(0);
        GlStateManager.clear(GL_STENCIL_BUFFER_BIT);

        BufferBuilder vb = Tessellator.getInstance().getBuffer();
        vb.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        vb.pos(xStart, yEnd, 0).endVertex();
        vb.pos(xEnd, yEnd, 0).endVertex();
        vb.pos(xEnd, yStart, 0).endVertex();
        vb.pos(xStart, yStart, 0).endVertex();
        Tessellator.getInstance().draw();

        GlStateManager.enableTexture2D();
        glStencilFunc(GL_EQUAL, flag, flag);
        glStencilMask(0);
        GlStateManager.colorMask(true, true, true, false);
        GlStateManager.depthMask(true);
    }

    public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
        this.gui.drawSizedTexturedModalRect(var1, var2, var3, var4, var5, var6, (float) this.texW, (float) this.texH);
    }

    public void drawCenteredString(FontRenderer var1, String var2, int var3, int var4, int var5) {
        var1.drawStringWithShadow(var2, var3 - var1.getStringWidth(var2) / 2, var4, var5);
    }

    public boolean onMousePressed(int var1, int var2, int var3) {
        return false;
    }

    public void onMouseReleased(int var1, int var2) {
    }

    public boolean onMouseWheel(int var1, int var2, int var3) {
        return false;
    }

    public boolean onKeyTyped(char var1, int var2) {
        return false;
    }

    public boolean intersectsWith(int var1, int var2) {
        return var1 >= this.posX && var1 <= this.posX + this.sizeX && var2 >= this.posY && var2 <= this.posY + this.sizeY;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer == null ? this.gui.getFontRenderer() : this.fontRenderer;
    }

    public ShageElementBase setFontRenderer(FontRenderer var1) {
        this.fontRenderer = var1;
        return this;
    }

    public final String getName() {
        return this.name;
    }

    public ShageElementBase setName(String var1) {
        this.name = var1;
        return this;
    }

    public final ShageGuiBase getContainerScreen() {
        return this.gui;
    }

    public final int getPosY() {
        return this.posY;
    }

    public final int getPosX() {
        return this.posX;
    }

    public final int getHeight() {
        return this.sizeY;
    }

    public final int getWidth() {
        return this.sizeX;
    }

    public final int getPosZ() {
        return posZ;
    }

    public final void setPosZ(int z) {
        this.posZ = z;
    }
}