
package me.heldplayer.mods.Smartestone.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiBase extends GuiContainer {

    protected InventoryPlayer playerInv;

    public GuiBase(Container container, InventoryPlayer playerInv) {
        super(container);

        this.playerInv = playerInv;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        boolean debug = false;
        if (debug) {
            int size = this.inventorySlots.inventorySlots.size();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 200.0F);
            for (int i = 0; i < size; i++) {
                Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);
                this.fontRenderer.drawStringWithShadow("" + slot.slotNumber, slot.xDisplayPosition, slot.yDisplayPosition, 0x5555FF);
            }
            GL11.glPopMatrix();
        }
    }

}
