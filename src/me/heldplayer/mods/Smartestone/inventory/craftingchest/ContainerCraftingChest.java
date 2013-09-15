
package me.heldplayer.mods.Smartestone.inventory.craftingchest;

import me.heldplayer.mods.Smartestone.item.ItemTemplate;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.util.Const;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

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
                this.addSlotToContainer(new Slot(this.craftMatrix, slotX + slotY * 3, 71 + slotX * 18, 17 + slotY * 18));
            }
        }

        this.addSlotToContainer(new SlotCraftingResult(playerInventory.player, this.craftMatrix, this.craftResult, Const.CRAFTINGCHEST_CRAFTRESULT_SLOT, 165, 35));

        for (int slotY = 0; slotY < 3; ++slotY) {
            for (int slotX = 0; slotX < 2; ++slotX) {
                Slot slot = new SlotTemplate(this.tile, Const.CRAFTINGCHEST_CRAFTRESULT_SLOT + 1 + slotX + slotY * 2, 17 + slotX * 18, 17 + slotY * 18);
                slot.setBackgroundIcon(Objects.itemTemplate.iconOverlay);
                this.addSlotToContainer(slot);
            }
        }

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

    private boolean clearGrid() {
        boolean failed = false;

        for (int i = 0; i < Const.CRAFTINGCHEST_CRAFTMATRIX_INV_SIZE; i++) {
            ItemStack stack = this.craftMatrix.getStackInSlot(i);

            if (stack == null || stack.stackSize <= 0 || stack.itemID <= 0) {
                continue;
            }

            for (int j = 0; j < Const.CRAFTINGCHEST_INV_SIZE && stack != null && stack.stackSize > 0; j++) {
                ItemStack currentStack = this.tile.getStackInSlot(j);
                if (currentStack == null) {
                    continue;
                }

                if (stack.isItemEqual(currentStack) && ItemStack.areItemStackTagsEqual(stack, currentStack)) {
                    while (currentStack.stackSize < currentStack.getMaxStackSize() && stack.stackSize > 0) {
                        currentStack.stackSize++;
                        stack.stackSize--;
                    }

                    this.tile.setInventorySlotContents(j, currentStack);

                    if (stack.stackSize <= 0) {
                        stack = null;
                    }
                }
            }

            if (stack == null || stack.stackSize <= 0 || stack.itemID <= 0) {
                this.craftMatrix.setInventorySlotContents(i, stack);
                continue;
            }

            for (int j = 0; j < Const.CRAFTINGCHEST_INV_SIZE && stack != null && stack.stackSize > 0; j++) {
                ItemStack currentStack = this.tile.getStackInSlot(j);
                if (currentStack == null) {
                    this.tile.setInventorySlotContents(j, stack);
                    stack = null;
                    break;
                }
            }

            this.craftMatrix.setInventorySlotContents(i, stack);

            if (stack != null) {
                failed = true;
            }
        }

        return !failed;
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int slot) {
        if (slot == 100) {
            return this.clearGrid();
        }
        else if (slot == 101) {
            for (int i = Const.CRAFTINGCHEST_INV_SIZE - 1; i >= 0; i--) {
                ItemStack stack = this.tile.getStackInSlot(i);

                if (stack == null || stack.stackSize <= 0 || stack.itemID <= 0) {
                    continue;
                }

                for (int j = 0; j < Const.CRAFTINGCHEST_INV_SIZE && stack != null && stack.stackSize > 0; j++) {
                    if (j >= i) {
                        break;
                    }
                    ItemStack currentStack = this.tile.getStackInSlot(j);

                    if (currentStack == null) {
                        continue;
                    }

                    if (stack.isItemEqual(currentStack) && ItemStack.areItemStackTagsEqual(stack, currentStack)) {
                        while (currentStack.stackSize < currentStack.getMaxStackSize() && stack.stackSize > 0) {
                            currentStack.stackSize++;
                            stack.stackSize--;
                        }

                        this.tile.setInventorySlotContents(j, currentStack);

                        if (stack.stackSize <= 0) {
                            stack = null;
                        }

                        this.tile.setInventorySlotContents(i, stack);
                    }
                }
            }
        }
        else if (slot >= 0 && slot <= 6) {
            ItemStack stack = this.tile.getStackInSlot(slot + Const.CRAFTINGCHEST_CRAFTRESULT_SLOT + 1);
            if (stack == null || stack.getItem() == null) {
                return false;
            }
            NBTTagCompound compound = stack.stackTagCompound;

            if (compound == null) {
                ItemStack result = this.tile.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT);
                if (result == null) {
                    return false;
                }

                compound = stack.stackTagCompound = new NBTTagCompound("tag");
                NBTTagCompound template = new NBTTagCompound();
                compound.setTag("Template", template);

                NBTTagList ingredients = new NBTTagList();
                template.setTag("Ingredients", ingredients);

                NBTTagCompound simple = new NBTTagCompound();
                template.setTag("Simple", simple);
                simple.setString("Output", result.getDisplayName());
                NBTTagList inputSimple = new NBTTagList();
                simple.setTag("Input", inputSimple);
                for (int y = 0; y < 3; y++) {
                    String line = "";
                    for (int x = 0; x < 3; x++) {
                        NBTTagCompound ingredient = new NBTTagCompound();
                        ingredients.appendTag(ingredient);

                        ItemStack in = this.tile.getStackInSlot(Const.CRAFTINGCHEST_INV_SIZE + x + y * 3);
                        if (in == null) {
                            line += (x > 0 ? " | " : "") + "-";
                        }
                        else {
                            line += (x > 0 ? " | " : "") + in.getDisplayName();

                            NBTTagCompound stackTag = new NBTTagCompound();
                            stackTag = in.writeToNBT(stackTag);
                            ingredient.setTag("stack", stackTag);
                        }
                    }
                    inputSimple.appendTag(new NBTTagString("", line));
                }

                stack.setItemDamage(1);
                this.tile.onInventoryChanged();
                return true;
            }
            else {
                if (!this.clearGrid()) {
                    return false;
                }

                NBTTagCompound template = compound.getCompoundTag("Template");
                NBTTagList ingredients = template.getTagList("Ingredients");

                for (int i = 0; i < Const.CRAFTINGCHEST_CRAFTMATRIX_INV_SIZE && i < ingredients.tagCount(); i++) {
                    NBTTagCompound ingredient = (NBTTagCompound) ingredients.tagAt(i);
                    if (!ingredient.hasKey("stack")) {
                        continue;
                    }

                    ItemStack item = ItemStack.loadItemStackFromNBT(ingredient.getCompoundTag("stack"));

                    for (int j = 0; j < Const.CRAFTINGCHEST_INV_SIZE; j++) {
                        ItemStack currentStack = this.tile.getStackInSlot(j);

                        if (currentStack == null || item.itemID <= 0 || item.stackSize <= 0) {
                            continue;
                        }

                        currentStack = currentStack.copy();
                        ItemStack newStack = currentStack.copy();
                        newStack.stackSize = 1;

                        if (item.isItemEqual(currentStack) && ItemStack.areItemStackTagsEqual(item, currentStack)) {
                            currentStack.stackSize--;
                            if (currentStack.itemID <= 0 || currentStack.stackSize <= 0) {
                                currentStack = null;
                            }
                            this.tile.setInventorySlotContents(j, currentStack);
                            this.craftMatrix.setInventorySlotContents(i, newStack);
                            break;
                        }
                        else if (OreDictionary.getOreID(item) != -1 && OreDictionary.getOreID(item) == OreDictionary.getOreID(currentStack)) {
                            currentStack.stackSize--;
                            if (currentStack.itemID <= 0 || currentStack.stackSize <= 0) {
                                currentStack = null;
                            }
                            this.tile.setInventorySlotContents(j, currentStack);
                            this.craftMatrix.setInventorySlotContents(i, newStack);
                            break;
                        }
                    }
                }
            }

            this.tile.onInventoryChanged();
            return true;
        }

        return false;
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
                if (slotStack.getItem() instanceof ItemTemplate) {
                    if (!this.mergeItemStack(slotStack, Const.CRAFTINGCHEST_CRAFTRESULT_SLOT + 1, Const.CRAFTINGCHEST_TOTAL_INV_SIZE, false)) {
                        return null;
                    }
                }
                else {
                    if (!this.mergeItemStack(slotStack, Const.CRAFTINGCHEST_TOTAL_INV_SIZE, 36 + Const.CRAFTINGCHEST_TOTAL_INV_SIZE, true)) {
                        return null;
                    }
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
            else if (slotId < Const.CRAFTINGCHEST_TOTAL_INV_SIZE) {
                if (this.mergeItemStack(slotStack, 0, Const.CRAFTINGCHEST_INV_SIZE, true)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else {
                    return null;
                }
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
