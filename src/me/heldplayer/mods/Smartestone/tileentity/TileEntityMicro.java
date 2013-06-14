
package me.heldplayer.mods.Smartestone.tileentity;

import java.util.ArrayList;
import java.util.List;

import me.heldplayer.api.Smartestone.micro.IMicroBlock;
import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IMicroBlockSubBlock;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.mods.Smartestone.CommonProxy;
import me.heldplayer.mods.Smartestone.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMicro extends TileEntity implements IMicroBlock {

    public List<MicroBlockInfo> info;

    public TileEntityMicro() {
        this.info = new ArrayList<MicroBlockInfo>();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList list = new NBTTagList("Info");

        for (MicroBlockInfo info : this.info) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Type", info.getType() != null ? info.getType().getTypeName() : "null");
            tag.setString("Material", info.getMaterial() != null ? info.getMaterial().getIdentifier() : "null");
            tag.setInteger("Data", info.getData());

            list.appendTag(tag);
        }

        compound.setTag("Info", list);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Info");

        this.info.clear();

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);

            IMicroBlockMaterial material = MicroBlockAPI.getMaterial(tag.getString("Material"));
            IMicroBlockSubBlock type = MicroBlockAPI.getSubBlock(tag.getString("Type"));
            int data = tag.getInteger("Data");

            MicroBlockInfo info = new MicroBlockInfo(material, type, data);

            this.info.add(info);
        }
    }

    public void writeNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList("Info");

        for (MicroBlockInfo info : this.info) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Type", info.getType() != null ? info.getType().getTypeName() : "null");
            tag.setString("Material", info.getMaterial() != null ? info.getMaterial().getIdentifier() : "null");
            tag.setInteger("Data", info.getData());

            list.appendTag(tag);
        }

        compound.setTag("Info", list);
    }

    public void readNBT(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList("Info");

        this.info.clear();

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);

            IMicroBlockMaterial material = MicroBlockAPI.getMaterial(tag.getString("Material"));
            IMicroBlockSubBlock type = MicroBlockAPI.getSubBlock(tag.getString("Type"));
            int data = tag.getInteger("Data");

            MicroBlockInfo info = new MicroBlockInfo(material, type, data);

            this.info.add(info);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet packet = PacketHandler.getPacket(1, this);
        return packet;
    }

    @Override
    public List<MicroBlockInfo> getSubBlocks() {
        return this.info;
    }

    @Override
    public void resendTileData() {
        CommonProxy.resendTileData(this);
    }

}
