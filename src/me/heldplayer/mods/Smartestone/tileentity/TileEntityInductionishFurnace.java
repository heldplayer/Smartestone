
package me.heldplayer.mods.Smartestone.tileentity;

import me.heldplayer.mods.Smartestone.CommonProxy;
import me.heldplayer.mods.Smartestone.packet.Packet6SetInventorySlotContents;
import me.heldplayer.mods.Smartestone.packet.PacketHandler;
import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityInductionishFurnace extends TileEntityRotatable implements IInventory, ISidedInventory {

    private static final int[] top = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    private static final int[] sides = new int[] { Const.INDUCTIONISHFURNACE_FUEL_SLOT };
    private static final int[] bottom = new int[] { Const.INDUCTIONISHFURNACE_OUTPUT_SLOT };

    private ItemStack[] inventory = new ItemStack[Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE];
    public float hover = 0.0F;
    public float prevHover = 0.0F;
    public int burnTime;
    public int maxBurnTime;
    public int progress;
    public boolean requiresUpdate = true;

    public TileEntityInductionishFurnace() {
        super("SSInductionishFurnace");
        this.hover = this.prevHover = CommonProxy.rand.nextFloat() * 1000.0F;
        this.burnTime = this.maxBurnTime = this.progress = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        this.requiresUpdate = true;
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
        else {
            boolean changed = false;

            if (this.requiresUpdate) {
                if (this.getStackInSlot(Const.INDUCTIONISHFURNACE_FUEL_SLOT) == null) {
                    for (int i = 0; i < Const.INDUCTIONISHFURNACE_INV_SIZE; i++) {
                        ItemStack stack = this.getStackInSlot(i);

                        if (TileEntityFurnace.getItemBurnTime(stack) > 0) {
                            this.setInventorySlotContents(Const.INDUCTIONISHFURNACE_FUEL_SLOT, stack.copy());
                            this.setInventorySlotContents(i, null);
                            break;
                        }
                    }
                }
                if (this.getStackInSlot(Const.INDUCTIONISHFURNACE_INPUT_SLOT) == null) {
                    for (int i = 0; i < Const.INDUCTIONISHFURNACE_INV_SIZE; i++) {
                        ItemStack stack = this.getStackInSlot(i);

                        if (stack != null && FurnaceRecipes.smelting().getSmeltingResult(stack) != null) {
                            this.setInventorySlotContents(Const.INDUCTIONISHFURNACE_INPUT_SLOT, stack.copy());
                            this.setInventorySlotContents(i, null);
                            break;
                        }
                    }
                }

                this.requiresUpdate = false;
            }

            boolean canSmelt = this.canSmelt();

            if (this.burnTime > 0) {
                this.burnTime--;
            }

            if (this.burnTime == 0 && canSmelt) {
                ItemStack stack = this.getStackInSlot(Const.INDUCTIONISHFURNACE_FUEL_SLOT);

                int time = TileEntityFurnace.getItemBurnTime(stack);
                if (time > 0) {
                    this.burnTime = this.maxBurnTime = time;

                    stack.stackSize--;

                    if (stack.stackSize <= 0) {
                        this.setInventorySlotContents(Const.INDUCTIONISHFURNACE_FUEL_SLOT, null);
                    }

                    changed = true;
                }
            }

            if (this.burnTime > 0 && canSmelt) {
                this.progress++;

                if (this.progress >= 200) {
                    this.progress = 0;
                    this.smeltItem();
                    changed = true;

                    Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
                    Packet6SetInventorySlotContents packet = new Packet6SetInventorySlotContents(this, Const.INDUCTIONISHFURNACE_INPUT_SLOT, this.getStackInSlot(Const.INDUCTIONISHFURNACE_INPUT_SLOT));
                    PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);

                    packet = new Packet6SetInventorySlotContents(this, Const.INDUCTIONISHFURNACE_OUTPUT_SLOT, this.getStackInSlot(Const.INDUCTIONISHFURNACE_OUTPUT_SLOT));
                    PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
                }
            }
            else {
                this.progress = 0;
            }

            if (changed) {
                this.onInventoryChanged();
            }
        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(this.getStackInSlot(Const.INDUCTIONISHFURNACE_INPUT_SLOT));

            ItemStack output = this.getStackInSlot(Const.INDUCTIONISHFURNACE_OUTPUT_SLOT);
            ItemStack input = this.getStackInSlot(Const.INDUCTIONISHFURNACE_INPUT_SLOT);

            if (output == null) {
                this.setInventorySlotContents(Const.INDUCTIONISHFURNACE_OUTPUT_SLOT, result.copy());
            }
            else if (output.isItemEqual(result)) {
                output.stackSize += result.stackSize;
            }

            input.stackSize--;

            if (input.stackSize <= 0) {
                this.setInventorySlotContents(Const.INDUCTIONISHFURNACE_INPUT_SLOT, null);
            }
        }
    }

    public boolean canSmelt() {
        ItemStack output = this.getStackInSlot(Const.INDUCTIONISHFURNACE_OUTPUT_SLOT);
        ItemStack input = this.getStackInSlot(Const.INDUCTIONISHFURNACE_INPUT_SLOT);
        ItemStack fuel = this.getStackInSlot(Const.INDUCTIONISHFURNACE_FUEL_SLOT);

        if (input == null) {
            return false;
        }
        if (this.burnTime == 0 && fuel == null) {
            return false;
        }

        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(input);
        if (result == null) {
            return false;
        }
        if (output == null) {
            return true;
        }
        if (!output.isItemEqual(result)) {
            return false;
        }
        int resultingSize = output.stackSize + result.stackSize;
        if (resultingSize > result.getMaxStackSize()) {
            return false;
        }

        return true;
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

        compound.setInteger("BurnTime", this.burnTime);
        compound.setInteger("MaxBurnTime", this.maxBurnTime);
        compound.setInteger("Progress", this.progress);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList items = compound.getTagList("Items");
        this.inventory = new ItemStack[Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE];

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound itemCompound = (NBTTagCompound) items.tagAt(i);
            byte slot = itemCompound.getByte("Slot");

            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
            }
        }

        this.burnTime = compound.getInteger("BurnTime");
        this.maxBurnTime = compound.getInteger("MaxBurnTime");
        this.progress = compound.getInteger("Progress");
    }

    @Override
    public void writeToTag(NBTTagCompound compound) {
        super.writeToTag(compound);
        NBTTagList items = new NBTTagList();

        for (int slot = Const.INDUCTIONISHFURNACE_INPUT_SLOT; slot < this.inventory.length; ++slot) {
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
    public void readFromTag(NBTTagCompound compound) {
        super.readFromTag(compound);
        NBTTagList items = compound.getTagList("Items");

        for (int slot = Const.INDUCTIONISHFURNACE_INPUT_SLOT; slot < this.inventory.length; ++slot) {
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
        return Const.INDUCTIONISHFURNACE_TOTAL_INV_SIZE;
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

        if ((index == Const.INDUCTIONISHFURNACE_INPUT_SLOT || index == Const.INDUCTIONISHFURNACE_OUTPUT_SLOT) && !this.worldObj.isRemote) {
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

                if ((slot == Const.INDUCTIONISHFURNACE_INPUT_SLOT || slot == Const.INDUCTIONISHFURNACE_OUTPUT_SLOT) && !this.worldObj.isRemote) {
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

                if ((slot == Const.INDUCTIONISHFURNACE_INPUT_SLOT || slot == Const.INDUCTIONISHFURNACE_OUTPUT_SLOT) && !this.worldObj.isRemote) {
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
        return slot < Const.INDUCTIONISHFURNACE_INV_SIZE;
    }

    // ISidedInventory
    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return slot == Const.INDUCTIONISHFURNACE_OUTPUT_SLOT;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot < Const.INDUCTIONISHFURNACE_INV_SIZE;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0) {
            return bottom;
        }
        if (side == 1) {
            return top;
        }
        return sides;
    }

}
