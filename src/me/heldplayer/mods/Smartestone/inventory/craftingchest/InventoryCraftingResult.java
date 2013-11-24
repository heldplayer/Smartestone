
package me.heldplayer.mods.Smartestone.inventory.craftingchest;

import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class InventoryCraftingResult extends InventoryCraftResult {

    private IInventory inventory;

    public InventoryCraftingResult(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void closeChest() {}

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (this.inventory.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) != null) {
            ItemStack stack = this.inventory.getStackInSlot(31);
            this.inventory.setInventorySlotContents(31, null);
            return stack;
        }
        else {
            return null;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public String getInvName() {
        return "Result";
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.getStackInSlot(31);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventory.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) != null) {
            ItemStack stack = this.inventory.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT);
            this.inventory.setInventorySlotContents(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT, null);
            return stack;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void onInventoryChanged() {}

    @Override
    public void openChest() {}

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setInventorySlotContents(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT, stack);
    }

}
