
package me.heldplayer.mods.Smartestone.tileentity;

import me.heldplayer.mods.Smartestone.PacketHandler;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import me.heldplayer.mods.Smartestone.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityRotatable extends TileEntity {

    public Direction direction = Direction.UNKNOWN;
    public Rotation rotation = Rotation.UNKNOWN;
    public String customName = "";
    private final String name;

    public TileEntityRotatable(String name) {
        this.name = name;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setByte("Direction", (byte) this.direction.ordinal());
        compound.setByte("Rotation", (byte) this.rotation.ordinal());
        compound.setString("CustomName", this.customName);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.direction = Direction.getDirection(compound.getByte("Direction"));
        this.rotation = Rotation.getRotation(compound.getByte("Rotation"));
        this.customName = compound.getString("CustomName");
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet packet = PacketHandler.getPacket(1, this);
        return packet;
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        Util.resendTileData(this);
    }

    public boolean isInvNameLocalized() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public String getInvName() {
        return this.isInvNameLocalized() ? this.customName : "container." + this.name;
    }

    public abstract void writeNBT(NBTTagCompound compound);

    public abstract void readNBT(NBTTagCompound compound);

}
