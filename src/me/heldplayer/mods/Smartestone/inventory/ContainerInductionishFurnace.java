
package me.heldplayer.mods.Smartestone.inventory;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityInductionishFurnace;
import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerInductionishFurnace extends net.minecraft.inventory.Container {

    private TileEntityInductionishFurnace tile;

    public int prevBurnTime = 0;
    public int prevMaxBurnTime = 0;
    public int prevProgress = 0;

    public ContainerInductionishFurnace(InventoryPlayer playerInventory, IInventory tileInventory) {
        super();

        this.tile = (TileEntityInductionishFurnace) tileInventory;

        for (int slotX = 0; slotX < 9; ++slotX) {
            this.addSlotToContainer(new Slot(this.tile, slotX, 8 + slotX * 18, 84));
        }

        this.addSlotToContainer(new Slot(this.tile, 9, 56, 17));
        this.addSlotToContainer(new Slot(this.tile, 10, 56, 53));
        this.addSlotToContainer(new SlotSmeltingOutput(playerInventory.player, this.tile, 11, 116, 35));

        for (int slotY = 0; slotY < 3; ++slotY) {
            for (int slotX = 0; slotX < 9; ++slotX) {
                this.addSlotToContainer(new Slot(playerInventory, slotX + slotY * 9 + 9, 8 + slotX * 18, 115 + slotY * 18));
            }
        }

        for (int slotX = 0; slotX < 9; ++slotX) {
            this.addSlotToContainer(new Slot(playerInventory, slotX, 8 + slotX * 18, 173));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.tile.burnTime);
        crafter.sendProgressBarUpdate(this, 1, this.tile.maxBurnTime);
        crafter.sendProgressBarUpdate(this, 2, this.tile.progress);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.prevBurnTime != this.tile.burnTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.burnTime);
            }

            if (this.prevMaxBurnTime != this.tile.maxBurnTime) {
                icrafting.sendProgressBarUpdate(this, 1, this.tile.maxBurnTime);
            }

            if (this.prevProgress != this.tile.progress) {
                icrafting.sendProgressBarUpdate(this, 2, this.tile.progress);
            }
        }

        this.prevBurnTime = this.tile.burnTime;
        this.prevMaxBurnTime = this.tile.maxBurnTime;
        this.prevProgress = this.tile.progress;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            this.tile.burnTime = value;
        }
        if (id == 1) {
            this.tile.maxBurnTime = value;
        }
        if (id == 2) {
            this.tile.progress = value;
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

            if (slotId < Const.INDUCTIONISHFURNACE_INV_SIZE) {
                if (!this.mergeItemStack(slotStack, Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE, 36 + Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else if (slotId <= Const.INDUCTIONISHFURNACE_OUTPUT_SLOT) {
                if (!this.mergeItemStack(slotStack, Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE, 36 + Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else {
                if (this.mergeItemStack(slotStack, 0, Const.INDUCTIONISHFURNACE_INV_SIZE, false)) {
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
