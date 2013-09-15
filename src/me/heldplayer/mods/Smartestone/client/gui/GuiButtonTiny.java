
package me.heldplayer.mods.Smartestone.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonTiny extends GuiButton {

    private int offset;

    public GuiButtonTiny(int id, int posX, int posY, int offset) {
        super(id, posX, posY, 9, 9, "");
        this.offset = offset;
    }

    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.drawButton) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            int v = offset;
            int u = 239;

            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            if (flag) {
                v += this.width;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
        }
    }

}
