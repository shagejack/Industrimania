package shagejack.minecraftology.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.Reference;
import shagejack.minecraftology.items.IronCluster;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class guiContainerDemo extends GuiContainer {
    public guiContainerDemo(ContainerDemo inventorySlotsIn) {

        super(inventorySlotsIn);
        this.xSize=176;
        this.ySize=133;
    }

    protected Slot inputSlot;
    protected Slot outputSlot;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;


    /*
    @Override
    public void initGui(){
        super.initGui();
        int offsetX=(this.width-this.xSize)/2,offsetY=(this.height-this.ySize)/2;
        this.buttonList.add(new GuiButton(BUTTON_UP,offsetX+153,offsetY+17,15,1-0,""){
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY){
                if(this.visible){
                    GlStateManager.color(1.0f,1.0f,1.0f);
                    mc.getTextureManager().bindTexture(TEXTURE);
                    int x=mouseX-this.xPosition,y=mouseY-this.yPosition;
                    if(x>=0&&y>=0&&x<this.width&&y<this.height){
                        this.drawTexturedModalRect(this.xPostion,this.yPostion,1,146,this.width,this.height);
                    }
                    else{
                        this.drawTexturedModalRect(this.xPostion,this.yPostion,1,134,this.width,this.height);
                    }
                }
            }
        });
        this.buttonList.add(new GuiButton(BUTTON_DOWN,offsetX+153,offsetY+29,15,10,""){
           @Override
           public void drawButton(Minecraft mc,int mouseX,int mouseY){
               if(this.visible){
                   GlStateManager.color(1.0f,1.0f,1.0f);
                   mc.getTextureManager().bindTexture(TEXTURE);
                   if(x>=0&&y>=0&&x<this.width&&y<this.height){
                       this.drawTexturedModalRect(this.xPostion,this.yPostion,20,146,this.width,this.height);
                   }
                   else{
                       this.drawTexturedModalRect(this.xPostion,this.yPostion,20,134,this.width,this.height);
                   }
               }
           }
        });
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        ItemStack stack =this.inputSlot.getStack();
        int amount = stack == null ?0:stack.getMaxStackSize();
        switch (button.id){
            case BUTTON_DOWN:
                amount = (amount+64)%65;
                break;
            case BUTTON_UP:
                amount = (amount+1)%65;
                break;
            default:
                super.actionPerformed(button);
                return;
        }
        //改成有效返回
        //this.inputSlot.putStack(amount == 0 ? null : new ItemStack(Item.iron_ingot,amount));
    }
     */

    private static final String TETURE_PATH = Reference.MOD_ID + ":"+"textures/gui/gui_demo.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TETURE_PATH);
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int offsetX = (this.width - this.xSize) / 2,offsetY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(offsetX,offsetY,0,0,this.xSize,this.ySize);
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.drawVerticalLine(30,19,36,0xFF000000);
        this.drawHorizontalLine(8,167,43,0xFF000000);
        String title = I18n.format("container.minecraftology.demo");
        this.fontRenderer.drawString(title,(this.xSize - this.fontRenderer.getStringWidth(title))/2,6,0x404040);
        //ItemStack item = new ItemStack(ItemLoader.ironCluster);
        //this.itemRender.renderItemAndEffectIntoGUI(item,8,20);
    }
}
