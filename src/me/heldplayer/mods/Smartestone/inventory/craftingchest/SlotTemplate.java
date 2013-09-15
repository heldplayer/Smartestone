
package me.heldplayer.mods.Smartestone.inventory.craftingchest;

import me.heldplayer.mods.Smartestone.item.ItemTemplate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTemplate extends Slot {

    public SlotTemplate(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.getItem() instanceof ItemTemplate;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {}

}
