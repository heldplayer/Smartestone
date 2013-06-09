
package me.heldplayer.mods.Smartestone.inventory;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityItemStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemStand extends net.minecraft.inventory.Container {

    private TileEntityItemStand tile;

    public ContainerItemStand(InventoryPlayer playerInventory, TileEntityItemStand tileInventory) {
        super();

        this.tile = (TileEntityItemStand) tileInventory;

        this.addSlotToContainer(new Slot(this.tile, 0, 80, 35));

        for (int slotY = 0; slotY < 3; ++slotY) {
            for (int slotX = 0; slotX < 9; ++slotX) {
                this.addSlotToContainer(new Slot(playerInventory, slotX + slotY * 9 + 9, 8 + slotX * 18, 84 + slotY * 18));
            }
        }

        for (int slotX = 0; slotX < 9; ++slotX) {
            this.addSlotToContainer(new Slot(playerInventory, slotX, 8 + slotX * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack stack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (slotId == 0) {
                if (!this.mergeItemStack(slotStack, 1, 36, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else {
                if (this.mergeItemStack(slotStack, 0, 1, false)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else {
                    return null;
                }
            }

            if (slotStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            }
            else {
                slot.onSlotChanged();
            }

            if (slotStack.stackSize == stack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, slotStack);
        }

        return stack;
    }

}
