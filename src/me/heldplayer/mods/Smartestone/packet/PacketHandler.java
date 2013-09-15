
package me.heldplayer.mods.Smartestone.packet;

import me.heldplayer.mods.Smartestone.util.Objects;

public class PacketHandler extends me.heldplayer.util.HeldCore.packet.PacketHandler {

    public static PacketHandler instance;

    public PacketHandler() {
        super(Objects.MOD_CHANNEL);
        this.registerPacket(2, Packet2SerializeableTile.class);
        this.registerPacket(3, Packet3AddMicroblock.class);
        this.registerPacket(4, Packet4RemoveMicroblock.class);
        this.registerPacket(5, Packet5ModifyMicroblock.class);
        instance = this;
    }

}
