
package me.heldplayer.mods.Smartestone.sync;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.util.HeldCore.sync.ISyncable;
import me.heldplayer.util.HeldCore.sync.ISyncableObjectOwner;
import me.heldplayer.util.HeldCore.sync.SyncHandler;

import com.google.common.io.ByteArrayDataInput;

public class SDirection implements ISyncable {

    private ISyncableObjectOwner owner;
    protected boolean hasChanged;
    protected int id;

    private Direction value;

    public SDirection(ISyncableObjectOwner owner, Direction value) {
        this.owner = owner;
        this.id = -1;
        this.value = value;
    }

    @Override
    public ISyncableObjectOwner getOwner() {
        return this.owner;
    }

    @Override
    public boolean hasChanged() {
        return this.hasChanged;
    }

    @Override
    public void setChanged(boolean value) {
        this.hasChanged = value;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        if (this.id == -1) {
            this.id = SyncHandler.lastSyncId++;
        }
        return this.id;
    }

    public void setValue(Direction value) {
        this.value = value;
        this.hasChanged = true;
    }

    public Direction getValue() {
        return this.value;
    }

    @Override
    public void read(ByteArrayDataInput in) throws IOException {
        this.value = Direction.DIRECTIONS[in.readUnsignedByte()];
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(this.value.ordinal());
    }

    @Override
    public String toString() {
        return "Direction:" + this.value;
    }

}
