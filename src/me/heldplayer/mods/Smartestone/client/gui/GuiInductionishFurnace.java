
package me.heldplayer.mods.Smartestone.client.gui;

import me.heldplayer.mods.Smartestone.Assets;
import me.heldplayer.mods.Smartestone.inventory.ContainerInductionishFurnace;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityInductionishFurnace;
import me.heldplayer.mods.Smartestone.util.Util;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInductionishFurnace extends GuiBase {

    private TileEntityInductionishFurnace tile;

    public GuiInductionishFurnace(InventoryPlayer playerInventory, TileEntityInductionishFurnace tileEntity) {
        super(new ContainerInductionishFurnace(playerInventory, tileEntity), playerInventory);

        this.tile = tileEntity;
        this.ySize = 197;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.renderEngine.func_110577_a(Assets.BACKGROUND_INDUCTIONISH_FURNACE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int posX = (this.width - this.xSize) / 2;
        int posY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(posX, posY, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.tile.burnTime > 0) {
            i1 = Util.getScaled(12, this.tile.burnTime, this.tile.maxBurnTime);
            this.drawTexturedModalRect(posX + 56, posY + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = Util.getScaled(24, this.tile.progress, 200);
        this.drawTexturedModalRect(posX + 79, posY + 34, 176, 14, i1 + 1, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.tile.isInvNameLocalized() ? "\u00A7o" + this.tile.getInvName() : StatCollector.translateToLocal(this.tile.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInv.isInvNameLocalized() ? "\u00A7o" + this.playerInv.getInvName() : StatCollector.translateToLocal(this.playerInv.getInvName()), 8, this.ySize - 96 + 2, 4210752);
    }

}
