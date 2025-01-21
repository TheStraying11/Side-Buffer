package uk.studiolucia.sidebuffer.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import uk.studiolucia.sidebuffer.SideBuffer;

import java.util.Arrays;
import java.util.Map;

public class SideBufferScreen extends AbstractContainerScreen<SideBufferMenu> {
    private static final ResourceLocation GUI = new ResourceLocation(SideBuffer.MODID, "textures/gui");

    public SideBufferScreen(SideBufferMenu sideBufferMenu, Inventory inventory, Component component) {
        super(sideBufferMenu, inventory, component);

        this.imageWidth = 188;
        this.imageHeight = 241;

        this.inventoryLabelX = 20;
        this.inventoryLabelY = 148;
        this.titleLabelX = 20;
        this.titleLabelY = 6;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        renderBackground(guiGraphics);
        guiGraphics.blit(
            GUI.withSuffix("/side_buffer_menu.png"), this.leftPos, this.topPos,
            0, 0, this.imageWidth, this.imageHeight,
            this.imageWidth, this.imageHeight
        );
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int p_282681_, int p_283686_) {
        super.renderLabels(guiGraphics, p_282681_, p_283686_);

        Map<String, String> lang = Language.getInstance().getLanguageData();

        char upChar = lang.get("sidebuffer.label.up").charAt(0);
        char downChar = lang.get("sidebuffer.label.down").charAt(0);
        char northChar = lang.get("sidebuffer.label.north").charAt(0);
        char southChar = lang.get("sidebuffer.label.south").charAt(0);
        char eastChar = lang.get("sidebuffer.label.east").charAt(0);
        char westChar = lang.get("sidebuffer.label.west").charAt(0);

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int upCharPos = alphabet.indexOf(upChar);
        int downCharPos = alphabet.indexOf(downChar);
        int northCharPos = alphabet.indexOf(northChar);
        int southCharPos = alphabet.indexOf(southChar);
        int eastCharPos = alphabet.indexOf(eastChar);
        int westCharPos = alphabet.indexOf(westChar);

        int i = 0;
        for (int charPos: Arrays.asList(
            upCharPos, downCharPos, northCharPos,
            southCharPos, eastCharPos, westCharPos
        )) {
            guiGraphics.blit(
                GUI.withSuffix("/label_atlas.png"), // textureAtlas
                5, // x
                19 + (22 * i), // y
                10, // width
                12, // height
                1.0F + (11*(charPos % 13)), // xOffset
                1.0F + (charPos > 12 ? 13 : 0), // yOffset
                10, // regionWidth
                12, // regionHeight
                144, // textureWidth
                27 // textureHeight
            );
            i++;
        }
    }
}
