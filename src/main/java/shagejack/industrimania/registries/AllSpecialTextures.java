package shagejack.industrimania.registries;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.resources.ResourceLocation;
import shagejack.industrimania.Industrimania;

public enum AllSpecialTextures {

    BLANK("blank.png"),
    CHECKERED("checkerboard.png"),
    THIN_CHECKERED("thin_checkerboard.png"),
    CUTOUT_CHECKERED("cutout_checkerboard.png"),
    HIGHLIGHT_CHECKERED("highlighted_checkerboard.png"),
    SELECTION("selection.png"),

    ;

    public static final String ASSET_PATH = "textures/special/";
    private ResourceLocation location;

    AllSpecialTextures(String filename) {
        location = Industrimania.asResource(ASSET_PATH + filename);
    }

    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    public ResourceLocation getLocation() {
        return location;
    }

}
