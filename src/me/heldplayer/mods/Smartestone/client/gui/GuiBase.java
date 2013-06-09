
package me.heldplayer.mods.Smartestone.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiBase extends GuiContainer {

    protected InventoryPlayer playerInv;

    public GuiBase(Container container, InventoryPlayer playerInv) {
        super(container);

        this.playerInv = playerInv;
    }

}
