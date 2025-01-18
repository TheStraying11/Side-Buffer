package uk.studiolucia.sidebuffer.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import uk.studiolucia.sidebuffer.SideBuffer;


public class SideBufferScreen extends AbstractContainerScreen<SideBufferMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SideBuffer.MODID, "textures/gui/side_buffer_menu.png");

    public SideBufferScreen(SideBufferMenu sideBufferMenu, Inventory inventory, Component component) {
        super(sideBufferMenu, inventory, component);

        this.imageWidth = 188;
        this.imageHeight = 241;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
