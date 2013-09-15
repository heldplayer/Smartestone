
package me.heldplayer.mods.Smartestone.tileentity;

import java.util.Arrays;

import me.heldplayer.mods.Smartestone.CommonProxy;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.ContainerCraftingChest;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.InventoryCraftingMatrix;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.InventoryCraftingResult;
import me.heldplayer.mods.Smartestone.util.Const;
import me.heldplayer.util.HeldCore.sync.ISyncable;
import me.heldplayer.util.HeldCore.sync.SInventoryStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.inventory.ISpecialInventory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityCraftingChest extends TileEntityRotatable implements ISpecialInventory, ISidedInventory {

    private static final int[] sides = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };

    private ItemStack[] inventory = new ItemStack[Const.CRAFTINGCHEST_TOTAL_INV_SIZE];
    public float hover = 0.0F;
    public float prevHover = 0.0F;
    public InventoryCraftingMatrix craftMatrix;
    public InventoryCraftingResult craftResult;
    public ContainerCraftingChest container;

    public SInventoryStack[] input;
    public SInventoryStack output;

    public TileEntityCraftingChest() {
        super("SSCraftingChest");
        this.hover = this.prevHover = CommonProxy.rand.nextFloat() * 1000.0F;
        this.craftMatrix = new InventoryCraftingMatrix(this);
        this.craftResult = new InventoryCraftingResult(this);

        ISyncable[] syncables = new ISyncable[13];
        this.input = new SInventoryStack[9];
        for (int i = 0; i < this.input.length; i++) {
            syncables[3 + i] = this.input[i] = new SInventoryStack(this, this, Const.CRAFTINGCHEST_INV_SIZE + i);
        }
        this.output = new SInventoryStack(this, this, Const.CRAFTINGCHEST_CRAFTRESULT_SLOT);
        syncables[0] = this.direction;
        syncables[1] = this.rotation;
        syncables[2] = this.customName;
        syncables[12] = this.output;
        this.syncables = Arrays.asList(syncables);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
    }

    public void onCraftMatrixChanged(IInventory matrix) {
        ItemStack result = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj);

        this.setInventorySlotContents(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT, result);
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            this.prevHover = this.hover;
            this.hover += 0.1F;

            if (this.hover >= 6.28F) {
                this.hover -= 6.28F;
                this.prevHover -= 6.28F;
            }
        }
    }

    // TileEntityBase
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList items = new NBTTagList();

        for (int slot = 0; slot < this.inventory.length; ++slot) {
            if (this.inventory[slot] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) slot);
                this.inventory[slot].writeToNBT(itemCompound);
                items.appendTag(itemCompound);
            }
        }

        compound.setTag("Items", items);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList items = compound.getTagList("Items");
        this.inventory = new ItemStack[Const.CRAFTINGCHEST_TOTAL_INV_SIZE];

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound itemCompound = (NBTTagCompound) items.tagAt(i);
            byte slot = itemCompound.getByte("Slot");

            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
            }
        }
    }

    // IInventory
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return Const.CRAFTINGCHEST_INV_SIZE;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

    @Override
    public boolean isInvNameLocalized() {
        return super.isInvNameLocalized();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.inventory[slot] != null) {
            ItemStack stack = this.inventory[slot];
            this.inventory[slot] = null;
            return stack;
        }
        else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack newStack) {
        this.inventory[slot] = newStack;
        if (slot == Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
            this.output.setChanged(true);
        }
        else if (slot >= Const.CRAFTINGCHEST_INV_SIZE && slot < Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
            this.input[slot - Const.CRAFTINGCHEST_INV_SIZE].setChanged(true);
        }

        if (newStack != null && newStack.stackSize > this.getInventoryStackLimit()) {
            newStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventory[slot] != null) {
            ItemStack stack;

            if (this.inventory[slot].stackSize <= amount) {
                stack = this.inventory[slot];
                this.inventory[slot] = null;

                if (slot == Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
                    this.output.setChanged(true);
                }
                else if (slot >= Const.CRAFTINGCHEST_INV_SIZE && slot < Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
                    this.input[slot - Const.CRAFTINGCHEST_INV_SIZE].setChanged(true);
                }

                this.onInventoryChanged();

                return stack;
            }
            else {
                stack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0) {
                    this.inventory[slot] = null;
                }

                if (slot == Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
                    this.output.setChanged(true);
                }
                else if (slot >= Const.CRAFTINGCHEST_INV_SIZE && slot < Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) {
                    this.input[slot - Const.CRAFTINGCHEST_INV_SIZE].setChanged(true);
                }

                this.onInventoryChanged();

                return stack;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return slot < Const.CRAFTINGCHEST_INV_SIZE;
    }

    // ISidedInventory
    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return slot < Const.CRAFTINGCHEST_INV_SIZE;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot < Const.CRAFTINGCHEST_INV_SIZE;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return sides;
    }

    // ISpecialInventory
    @Override
    public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
        int insertCount = 0;
        int toInsert = stack.stackSize;

        for (boolean first = true; true; first = false) {
            for (int i = 0; i < Const.CRAFTINGCHEST_INV_SIZE; i++) {
                if (toInsert <= 0) {
                    break;
                }

                ItemStack currentStack = this.inventory[i];

                if (currentStack == null || currentStack.stackSize <= 0 || currentStack.itemID <= 0) {
                    if (!first) {
                        int inserted = stack.getMaxStackSize() > toInsert ? toInsert : stack.getMaxStackSize();
                        insertCount += inserted;
                        toInsert -= inserted;
                        if (doAdd) {
                            this.setInventorySlotContents(i, stack.copy());
                        }
                    }
                }
                else {
                    if (currentStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(currentStack, stack)) {
                        int inserted = stack.getMaxStackSize() > toInsert ? toInsert : stack.getMaxStackSize();
                        int maxInsert = currentStack.getMaxStackSize() - currentStack.stackSize;
                        inserted = inserted > maxInsert ? maxInsert : inserted;
                        if (inserted > 0) {
                            insertCount += inserted;
                            toInsert -= inserted;
                            if (doAdd) {
                                currentStack.stackSize += inserted;
                                this.setInventorySlotContents(i, currentStack);
                            }
                        }
                    }
                }
            }

            if (!first) {
                break;
            }
        }

        return insertCount;
    }

    @Override
    public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount) {
        ItemStack stack = this.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT);

        if (stack != null && stack.stackSize > 0 && stack.itemID > 0 && this.craftMatrix.canCraft()) {
            if (doRemove) {
                for (int slot = 0; slot < this.craftMatrix.getSizeInventory(); ++slot) {
                    ItemStack slotStack = this.craftMatrix.getStackInSlot(slot);

                    if (slotStack != null) {
                        this.craftMatrix.decrStackSize(slot, -999);

                        if (slotStack.getItem().hasContainerItem()) {
                            ItemStack leftoverStack = slotStack.getItem().getContainerItemStack(slotStack);

                            if (leftoverStack.isItemStackDamageable() && leftoverStack.getItemDamage() > leftoverStack.getMaxDamage()) {
                                // XXX: MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, leftoverStack));
                                leftoverStack = null;
                            }

                            if (leftoverStack != null && (!slotStack.getItem().doesContainerItemLeaveCraftingGrid(slotStack))) { // || !this.thePlayer.inventory.addItemStackToInventory(leftoverStack))) {
                                if (this.craftMatrix.getStackInSlot(slot) == null) {
                                    this.craftMatrix.setInventorySlotContents(slot, leftoverStack);
                                }
                                // else {
                                // this.thePlayer.dropPlayerItem(leftoverStack);
                                // }
                            }
                        }
                    }
                }
            }

            return new ItemStack[] { stack.copy() };
        }

        return null;
    }

}
