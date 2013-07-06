
package me.heldplayer.mods.Smartestone.tileentity;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import me.heldplayer.api.Smartestone.micro.IMicroBlock;
import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IMicroBlockSubBlock;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfoSorter;
import me.heldplayer.mods.Smartestone.packet.ISerializableTile;
import me.heldplayer.mods.Smartestone.packet.Packet2SerializeableTile;
import me.heldplayer.mods.Smartestone.packet.Packet3AddMicroblock;
import me.heldplayer.mods.Smartestone.packet.Packet4RemoveMicroblock;
import me.heldplayer.mods.Smartestone.packet.Packet5ModifyMicroblock;
import me.heldplayer.mods.Smartestone.packet.PacketHandler;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TileEntityMicro extends TileEntity implements IMicroBlock, ISerializableTile {

    public Set<MicroBlockInfo> infos;
    public boolean[] usedIndices;

    public TileEntityMicro() {
        this.infos = new TreeSet<MicroBlockInfo>(new MicroBlockInfoSorter());
        this.usedIndices = new boolean[64];
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList list = new NBTTagList("Info");

        MicroBlockInfo[] infos = this.infos.toArray(new MicroBlockInfo[this.infos.size()]);

        for (int i = 0; i < infos.length; i++) {
            MicroBlockInfo info = infos[i];
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Type", info.getType() != null ? info.getType().getTypeName() : "null");
            tag.setString("Material", info.getMaterial() != null ? info.getMaterial().getIdentifier() : "null");
            tag.setInteger("Data", info.getData());
            tag.setInteger("Index", i);

            list.appendTag(tag);
        }

        compound.setTag("Info", list);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Info");

        this.infos.clear();

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);

            IMicroBlockMaterial material = MicroBlockAPI.getMaterial(tag.getString("Material"));
            IMicroBlockSubBlock type = MicroBlockAPI.getSubBlock(tag.getString("Type"));
            int data = tag.getInteger("Data");

            int index = this.getNextAvailableIndex();

            if (index < 0) {
                Objects.log.log(Level.WARNING, "Skipping a MicroBlock component because there are no available slots");
            }
            else {
                MicroBlockInfo info = new MicroBlockInfo(material, type, data);
                info.index = index;
                this.usedIndices[index] = true;

                this.infos.add(info);
            }
        }
    }

    @Override
    public void writeToTag(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList("Info");

        for (MicroBlockInfo info : this.infos) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Type", info.getType() != null ? info.getType().getTypeName() : "null");
            tag.setString("Material", info.getMaterial() != null ? info.getMaterial().getIdentifier() : "null");
            tag.setInteger("Data", info.getData());
            tag.setInteger("Index", info.index);

            list.appendTag(tag);
        }

        compound.setTag("Info", list);
    }

    @Override
    public void readFromTag(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList("Info");

        this.infos.clear();

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);

            IMicroBlockMaterial material = MicroBlockAPI.getMaterial(tag.getString("Material"));
            IMicroBlockSubBlock type = MicroBlockAPI.getSubBlock(tag.getString("Type"));
            int data = tag.getInteger("Data");
            int index = tag.getInteger("Index");

            MicroBlockInfo info = new MicroBlockInfo(material, type, data);
            info.index = index;

            this.infos.add(info);
        }
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

    private int getNextAvailableIndex() {
        int index = 0;

        boolean flag = this.usedIndices[index];
        while (flag) {
            index++;
            if (index >= this.usedIndices.length) {
                flag = false;
                index = -1;
            }
            else {
                flag = this.usedIndices[index];
            }
        }

        return index;
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet2SerializeableTile packet = new Packet2SerializeableTile(this);
        return PacketHandler.instance.createPacket(packet);
    }

    @Override
    public Set<MicroBlockInfo> getSubBlocks() {
        return this.infos;
    }

    @Override
    public void addInfo(MicroBlockInfo info) {
        if (this.worldObj.isRemote) {
            return;
        }

        //Objects.log.log(Level.INFO, "Info was added at (" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")");

        int index = this.getNextAvailableIndex();

        if (index >= 0) {
            info.index = index;
            this.usedIndices[index] = true;
            this.infos.add(info);
            Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
            Packet3AddMicroblock packet = new Packet3AddMicroblock(this, info);
            me.heldplayer.util.HeldCore.packet.PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
        }
    }

    @Override
    public void modifyInfo(MicroBlockInfo info) {
        if (this.worldObj.isRemote) {
            return;
        }

        //Objects.log.log(Level.INFO, "Info was modified at (" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")");

        Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
        Packet5ModifyMicroblock packet = new Packet5ModifyMicroblock(this, info);
        me.heldplayer.util.HeldCore.packet.PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
    }

    @Override
    public void removeInfo(MicroBlockInfo info) {
        if (this.worldObj.isRemote) {
            return;
        }

        //Objects.log.log(Level.INFO, "Info was removed at (" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")");

        this.infos.remove(info);
        this.usedIndices[info.index] = false;

        Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
        Packet4RemoveMicroblock packet = new Packet4RemoveMicroblock(this, info);
        me.heldplayer.util.HeldCore.packet.PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getDimension(), chunk.xPosition, chunk.zPosition);
    }

}
