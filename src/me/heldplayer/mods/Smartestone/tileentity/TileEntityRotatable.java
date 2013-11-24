
package me.heldplayer.mods.Smartestone.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import me.heldplayer.mods.Smartestone.sync.SDirection;
import me.heldplayer.mods.Smartestone.sync.SRotation;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import me.heldplayer.util.HeldCore.sync.ISyncable;
import me.heldplayer.util.HeldCore.sync.ISyncableObjectOwner;
import me.heldplayer.util.HeldCore.sync.SString;
import me.heldplayer.util.HeldCore.sync.packet.Packet4InitiateClientTracking;
import me.heldplayer.util.HeldCore.sync.packet.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

public abstract class TileEntityRotatable extends TileEntity implements ISyncableObjectOwner {

    //public Direction direction = Direction.UNKNOWN;
    //public Rotation rotation = Rotation.UNKNOWN;
    //public String customName = "";
    private final String name;

    public SDirection direction;
    public SRotation rotation;
    public SString customName;

    protected List<ISyncable> syncables;

    public TileEntityRotatable(String name) {
        this.name = name;
        this.direction = new SDirection(this, Direction.UNKNOWN);
        this.rotation = new SRotation(this, Rotation.UNKNOWN);
        this.customName = new SString(this, "");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setByte("Direction", (byte) this.direction.getValue().ordinal());
        compound.setByte("Rotation", (byte) this.rotation.getValue().ordinal());
        compound.setString("CustomName", this.customName.getValue());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.direction.setValue(Direction.getDirection(compound.getByte("Direction")));
        this.rotation.setValue(Rotation.getRotation(compound.getByte("Rotation")));
        this.customName.setValue(compound.getString("CustomName"));
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.instance.createPacket(new Packet4InitiateClientTracking(xCoord, yCoord, zCoord));
    }

    public boolean isInvNameLocalized() {
        return this.customName != null && !this.customName.getValue().isEmpty();
    }

    public String getInvName() {
        return this.isInvNameLocalized() ? this.customName.getValue() : "container." + this.name;
    }

    // ISyncableObjectOwner

    @Override
    public boolean isNotValid() {
        return super.isInvalid();
    }

    @Override
    public List<ISyncable> getSyncables() {
        return this.syncables;
    }

    @Override
    public void readSetup(ByteArrayDataInput in) throws IOException {
        for (int i = 0; i < this.syncables.size(); i++) {
            ISyncable syncable = this.syncables.get(i);
            syncable.setId(in.readInt());
            syncable.read(in);
        }

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void writeSetup(DataOutputStream out) throws IOException {
        for (int i = 0; i < this.syncables.size(); i++) {
            ISyncable syncable = this.syncables.get(i);
            out.writeInt(syncable.getId());
            syncable.write(out);
        }
    }

    @Override
    public int getPosX() {
        return this.xCoord;
    }

    @Override
    public int getPosY() {
        return this.yCoord;
    }

    @Override
    public int getPosZ() {
        return this.zCoord;
    }

    @Override
    public void onDataChanged(ISyncable syncable) {
        if (syncable == this.direction || syncable == this.rotation) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

}
