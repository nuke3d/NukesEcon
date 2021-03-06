package com.nuke3dtv.nukesecon.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuke3dtv.nukesecon.NukesEcon;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class StrongBoxScreen extends ContainerScreen<StrongBoxContainer> {

    private ResourceLocation GUI = new ResourceLocation(NukesEcon.MODID, "textures/gui/strongbox_gui.png");

    public StrongBoxScreen(StrongBoxContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawString(matrixStack, Minecraft.getInstance().fontRenderer, "Coins: " + container.getCoins(), 10, 2, 0xffffff);
        drawString(matrixStack, Minecraft.getInstance().fontRenderer, "Code: " + container.getKeyCode(), 10, 14, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
