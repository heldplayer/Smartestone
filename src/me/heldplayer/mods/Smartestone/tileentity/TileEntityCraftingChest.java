
package me.heldplayer.mods.Smartestone.tileentity;

import me.heldplayer.mods.Smartestone.CommonProxy;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.ContainerCraftingChest;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.InventoryCraftingMatrix;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.InventoryCraftingResult;
import me.heldplayer.mods.Smartestone.packet.Packet6SetInventorySlotContents;
import me.heldplayer.mods.Smartestone.packet.PacketHandler;
import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
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

    public TileEntityCraftingChest() {
        super("SSCraftingChest");
        this.hover = this.prevHover = CommonProxy.rand.nextFloat() * 1000.0F;
        this.craftMatrix = new InventoryCraftingMatrix(this);
        this.craftResult = new InventoryCraftingResult(this);
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

    @Override
    public void writeNBT(NBTTagCompound compound) {
        NBTTagList items = new NBTTagList();

        for (int slot = Const.CRAFTINGCHEST_INV_SIZE; slot < this.inventory.length; ++slot) {
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
    public void readNBT(NBTTagCompound compound) {
        NBTTagList items = compound.getTagList("Items");

        for (int slot = Const.CRAFTINGCHEST_INV_SIZE; slot < this.inventory.length; ++slot) {
            this.inventory[slot] = null;
        }

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
    public String getInvName() {
        return super.getInvName();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventory[index] != null) {
            ItemStack stack = this.inventory[index];
            this.inventory[index] = null;
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
    public void setInventorySlotContents(int index, ItemStack newStack) {
        this.inventory[index] = newStack;

        if (newStack != null && newStack.stackSize > this.getInventoryStackLimit()) {
            newStack.stackSize = this.getInventoryStackLimit();
        }

        if (index >= Const.CRAFTINGCHEST_INV_SIZE && index < Const.CRAFTINGCHEST_TOTAL_INV_SIZE && !this.worldObj.isRemote) {
            Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
            Packet6SetInventorySlotContents packet = new Packet6SetInventorySlotContents(this, index, newStack);
            PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
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

                this.onInventoryChanged();

                if (slot >= Const.CRAFTINGCHEST_INV_SIZE && slot < Const.CRAFTINGCHEST_TOTAL_INV_SIZE && !this.worldObj.isRemote) {
                    Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
                    Packet6SetInventorySlotContents packet = new Packet6SetInventorySlotContents(this, slot, stack);
                    PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
                }

                return stack;
            }
            else {
                stack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0) {
                    this.inventory[slot] = null;
                }

                this.onInventoryChanged();

                if (slot >= Const.CRAFTINGCHEST_INV_SIZE && slot < Const.CRAFTINGCHEST_TOTAL_INV_SIZE && !this.worldObj.isRemote) {
                    Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
                    Packet6SetInventorySlotContents packet = new Packet6SetInventorySlotContents(this, slot, stack);
                    PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
                }

                return stack;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isStackValidForSlot(int slot, ItemStack item) {
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
