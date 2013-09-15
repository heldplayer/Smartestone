
package me.heldplayer.mods.Smartestone.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonTemplate extends GuiButton {

    private boolean right;
    public boolean isWritten;

    public GuiButtonTemplate(int id, int posX, int posY, boolean right) {
        super(id, posX, posY, 9, 18, "");
        this.right = right;
    }

    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.drawButton) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            int v = right ? 18 : 0;
            int u = 221;

            if (!this.enabled) {
                u -= this.width;
            }
            else {
                if (isWritten) {
                    v += 36;
                }

                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

                if (flag) {
                    u += this.width;
                }
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
        }
    }

}
