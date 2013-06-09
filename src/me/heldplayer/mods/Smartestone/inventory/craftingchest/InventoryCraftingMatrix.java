
package me.heldplayer.mods.Smartestone.inventory.craftingchest;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryCraftingMatrix extends InventoryCrafting {

    protected TileEntityCraftingChest tile;

    public InventoryCraftingMatrix(TileEntityCraftingChest tile) {
        super(null, 3, 3);
        this.tile = tile;
    }

    // IInventory
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return Const.CRAFTINGCHEST_CRAFTMATRIX_INV_SIZE;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public String getInvName() {
        return "container.crafting";
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? null : this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.tile.setInventorySlotContents(index + Const.CRAFTINGCHEST_INV_SIZE, stack);
        this.tile.onCraftMatrixChanged(this);
    }

    public boolean canCraft() {
        ItemStack[] stacks = new ItemStack[this.tile.getSizeInventory()];

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = this.tile.getStackInSlot(i);
            if (stacks[i] != null) {
                stacks[i] = stacks[i].copy();
            }
        }

        for (int i = 0; i < this.getSizeInventory(); i++) {
            ItemStack stack = this.getStackInSlot(i);

            if (stack == null || stack.itemID <= 0 || stack.stackSize <= 0) {
                continue;
            }

            if (stack.stackSize > 1) {
                continue;
            }

            if (stack.getItem().hasContainerItem() && !stack.getItem().doesContainerItemLeaveCraftingGrid(stack)) {
                continue;
            }

            boolean found = false;

            for (int j = 0; j < stacks.length; j++) {
                ItemStack currentStack = stacks[j];
                if (currentStack == null || stack.itemID <= 0 || stack.stackSize <= 0) {
                    continue;
                }

                if (stack.isItemEqual(currentStack) && ItemStack.areItemStackTagsEqual(stack, currentStack)) {
                    currentStack.stackSize--;
                    found = true;
                    if (currentStack.itemID <= 0 || currentStack.stackSize <= 0) {
                        stacks[j] = null;
                        currentStack = null;
                    }
                    break;
                }
                else if (OreDictionary.getOreID(stack) != -1 && OreDictionary.getOreID(stack) == OreDictionary.getOreID(currentStack)) {
                    currentStack.stackSize--;
                    found = true;
                    if (currentStack.itemID <= 0 || currentStack.stackSize <= 0) {
                        stacks[j] = null;
                        currentStack = null;
                    }
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (amount == -999) {
            if (this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE) != null) {
                ItemStack stack;

                if (this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE).stackSize <= 1) {
                    stack = this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE);
                    this.tile.setInventorySlotContents(index + Const.CRAFTINGCHEST_INV_SIZE, null);

                    for (int i = 0; i < Const.CRAFTINGCHEST_INV_SIZE; i++) {
                        ItemStack replacement = this.tile.getStackInSlot(i);
                        if (replacement == null) {
                            continue;
                        }

                        if (replacement.isItemEqual(stack) || (OreDictionary.getOreID(stack) != -1 && OreDictionary.getOreID(stack) == OreDictionary.getOreID(replacement))) {
                            this.tile.decrStackSize(i, 1);

                            ItemStack repl = replacement.copy();
                            repl.stackSize = 1;

                            this.tile.setInventorySlotContents(index + Const.CRAFTINGCHEST_INV_SIZE, repl);

                            break;
                        }
                    }

                    this.tile.onCraftMatrixChanged(this);
                    return stack;
                }
                else {
                    stack = this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE).splitStack(1);

                    if (this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE).stackSize == 0) {
                        this.tile.setInventorySlotContents(index + Const.CRAFTINGCHEST_INV_SIZE, null);
                    }

                    this.tile.onCraftMatrixChanged(this);
                    return stack;
                }
            }
            else {
                return null;
            }
        }
        else {

            if (this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE) != null) {
                ItemStack stack;

                if (this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE).stackSize <= amount) {
                    stack = this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE);
                    this.tile.setInventorySlotContents(index + Const.CRAFTINGCHEST_INV_SIZE, null);
                    this.tile.onCraftMatrixChanged(this);
                    return stack;
                }
                else {
                    stack = this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE).splitStack(amount);

                    if (this.tile.getStackInSlot(index + Const.CRAFTINGCHEST_INV_SIZE).stackSize == 0) {
                        this.tile.setInventorySlotContents(index + Const.CRAFTINGCHEST_INV_SIZE, null);
                    }

                    this.tile.onCraftMatrixChanged(this);
                    return stack;
                }
            }
            else {
                return null;
            }
        }
    }

    @Override
    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
        return true;
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < 3) {
            int index = row + column * 3;
            return this.getStackInSlot(index);
        }
        else {
            return null;
        }
    }

    @Override
    public void onInventoryChanged() {}

}
