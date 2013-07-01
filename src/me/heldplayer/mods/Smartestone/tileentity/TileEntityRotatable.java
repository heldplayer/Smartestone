
package me.heldplayer.mods.Smartestone.tileentity;

import me.heldplayer.mods.Smartestone.packet.ISerializableTile;
import me.heldplayer.mods.Smartestone.packet.Packet2SerializeableTile;
import me.heldplayer.mods.Smartestone.packet.PacketHandler;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityRotatable extends TileEntity implements ISerializableTile {

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
        Packet2SerializeableTile packet = new Packet2SerializeableTile(this);
        return PacketHandler.instance.createPacket(packet);
    }

    public boolean isInvNameLocalized() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public String getInvName() {
        return this.isInvNameLocalized() ? this.customName : "container." + this.name;
    }

    @Override
    public void writeToTag(NBTTagCompound compound) {
        compound.setByte("Direction", (byte) this.direction.ordinal());
        compound.setByte("Rotation", (byte) this.rotation.ordinal());
        compound.setString("CustomName", this.customName);
    }

    @Override
    public void readFromTag(NBTTagCompound compound) {
        this.direction = Direction.getDirection(compound.getByte("Direction"));
        this.rotation = Rotation.getRotation(compound.getByte("Rotation"));
        this.customName = compound.getString("CustomName");
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public int getX() {
        return this.xCoord;
    }

    @Override
    public int getY() {
        return this.yCoord;
    }

    @Override
    public int getZ() {
        return this.zCoord;
    }

}
