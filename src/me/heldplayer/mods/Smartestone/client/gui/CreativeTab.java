
package me.heldplayer.mods.Smartestone.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

    private final ItemStack displayStack;

    public CreativeTab(String label, ItemStack displayStack) {
        super(label);
        this.displayStack = displayStack;
        this.setBackgroundImageName("item_search.png");
    }

    @Override
    public ItemStack getIconItemStack() {
        return this.displayStack;
    }

    public boolean getCanSearch() {
        return true;
    }

}
