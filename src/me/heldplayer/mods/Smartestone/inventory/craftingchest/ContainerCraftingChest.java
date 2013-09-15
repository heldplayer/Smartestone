
package me.heldplayer.mods.Smartestone.inventory.craftingchest;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerCraftingChest extends net.minecraft.inventory.ContainerWorkbench {

    private TileEntityCraftingChest tile;
    public InventoryCraftingMatrix craftMatrix;
    public InventoryCraftingResult craftResult;
    private World worldObj;
    private boolean initialized = false;

    public ContainerCraftingChest(InventoryPlayer playerInventory, World world, int x, int y, int z, IInventory tileInventory) {
        super(playerInventory, world, x, y, z);

        this.initialized = true;

        this.tile = (TileEntityCraftingChest) tileInventory;
        this.craftMatrix = this.tile.craftMatrix;
        this.craftResult = this.tile.craftResult;

        this.inventorySlots.clear();

        for (int slotY = 0; slotY < 2; ++slotY) {
            for (int slotX = 0; slotX < 11; ++slotX) {
                this.addSlotToContainer(new Slot(this.tile, slotX + slotY * 11, 8 + slotX * 18, 84 + slotY * 18));
            }
        }

        for (int slotY = 0; slotY < 3; ++slotY) {
            for (int slotX = 0; slotX < 3; ++slotX) {
                //this.addSlotToContainer(new Slot(this.craftMatrix, slotX + slotY * 3, 30 + slotX * 18, 17 + slotY * 18));
                this.addSlotToContainer(new Slot(this.craftMatrix, slotX + slotY * 3, 44 + slotX * 18, 17 + slotY * 18));
            }
        }

        this.addSlotToContainer(new SlotCraftingResult(playerInventory.player, this.craftMatrix, this.craftResult, Const.CRAFTINGCHEST_CRAFTRESULT_SLOT, 138, 35));

        for (int slotY = 0; slotY < 3; ++slotY) {
            for (int slotX = 0; slotX < 9; ++slotX) {
                this.addSlotToContainer(new Slot(playerInventory, slotX + slotY * 9 + 9, 26 + slotX * 18, 133 + slotY * 18));
            }
        }

        for (int slotX = 0; slotX < 9; ++slotX) {
            this.addSlotToContainer(new Slot(playerInventory, slotX, 26 + slotX * 18, 191));
        }

        this.tile.onInventoryChanged();

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }

    @Override
    public boolean func_94530_a(ItemStack stack, Slot slot) {
        if (slot.inventory instanceof SlotCraftingResult) {
            return false;
        }
        return super.func_94530_a(stack, slot);
    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        if (this.initialized) {
            ItemStack result = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj);

            this.tile.setInventorySlotContents(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT, result);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack stack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (slotId < Const.CRAFTINGCHEST_INV_SIZE) {
                if (!this.mergeItemStack(slotStack, Const.CRAFTINGCHEST_TOTAL_INV_SIZE, 36 + Const.CRAFTINGCHEST_TOTAL_INV_SIZE, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else if (slotId < Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
                if (!this.mergeItemStack(slotStack, 0, Const.CRAFTINGCHEST_INV_SIZE, false)) {
                    if (!this.mergeItemStack(slotStack, Const.CRAFTINGCHEST_TOTAL_INV_SIZE, 36 + Const.CRAFTINGCHEST_TOTAL_INV_SIZE, true)) {
                        return null;
                    }
                }

                slot.onSlotChange(slotStack, stack);
            }
            else if (slotId == Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
                if (!this.mergeItemStack(slotStack, Const.CRAFTINGCHEST_TOTAL_INV_SIZE, 36 + Const.CRAFTINGCHEST_TOTAL_INV_SIZE, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else {
                if (this.mergeItemStack(slotStack, 0, Const.CRAFTINGCHEST_INV_SIZE, false)) {
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
