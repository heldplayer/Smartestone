
package me.heldplayer.mods.Smartestone.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGhost extends Slot {

    int maxStackSize;

    public SlotGhost(IInventory inventory, int id, int posX, int posY, int maxStackSize) {
        super(inventory, id, posX, posY);
        this.maxStackSize = maxStackSize;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    public void clickSlot(ItemStack stack, int mouse, boolean shift) {
        if (stack != null && !stack.equals(this.getStack())) {
            int size = this.maxStackSize > stack.getMaxStackSize() ? stack.getMaxStackSize() : this.maxStackSize;
            if (shift) {
                size = this.maxStackSize;
            }
            if (mouse == 1) {
                size = 1;
            }
            ItemStack result = stack.copy();
            result.stackSize = size;
            this.putStack(result);
        }
        else if (stack == null && this.getStack() != null) {
            int amount = 1;
            if (mouse == 2) {
                amount = -1;
            }
            if (shift) {
                amount *= 16;
            }

            ItemStack result = this.getStack();

            result.stackSize += amount;

            if (result.stackSize <= 0) {
                result = null;
            }
            else if (result.stackSize > result.getMaxStackSize()) {
                result.stackSize = result.getMaxStackSize();
            }

            this.putStack(stack);
        }
        else if (stack == null && shift) {
            this.putStack(null);
        }
    }
}
