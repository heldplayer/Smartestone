
package me.heldplayer.mods.Smartestone.packet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface ISerializableTile {

    public abstract void writeToTag(NBTTagCompound compound);

    public abstract void readFromTag(NBTTagCompound compound);

    public abstract World getWorld();

    public abstract int getX();

    public abstract int getY();

    public abstract int getZ();

}
