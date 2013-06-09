
package me.heldplayer.mods.Smartestone.client.gui;

import me.heldplayer.mods.Smartestone.client.ClientProxy;
import me.heldplayer.mods.Smartestone.inventory.ContainerItemStand;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityItemStand;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiItemStand extends GuiBase {

    private TileEntityItemStand tile;

    public GuiItemStand(InventoryPlayer playerInventory, TileEntityItemStand tileEntity) {
        super(new ContainerItemStand(playerInventory, tileEntity), playerInventory);

        this.tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.renderEngine.bindTexture(ClientProxy.textureLocation + "gui/itemstand.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.tile.isInvNameLocalized() ? "\u00A7o" + this.tile.getInvName() : StatCollector.translateToLocal(this.tile.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInv.isInvNameLocalized() ? "\u00A7o" + this.playerInv.getInvName() : StatCollector.translateToLocal(this.playerInv.getInvName()), 8, this.ySize - 96 + 2, 4210752);
    }

}
