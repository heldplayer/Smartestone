
package me.heldplayer.api.Smartestone.micro.rendering;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHighlightEventDelegate {

    private DrawBlockHighlightEvent event;
    private int sideHit;
    private int offsetX;
    private int offsetY;
    private int offsetZ;

    public void setEvent(DrawBlockHighlightEvent event) {
        this.event = event;
        this.sideHit = event.target.sideHit;
        this.offsetX = this.offsetY = this.offsetZ = 0;
    }

    public double interpolateX() {
        return (double) this.offsetX + this.event.player.lastTickPosX + (this.event.player.posX - this.event.player.lastTickPosX) * (double) this.event.partialTicks;
    }

    public double interpolateY() {
        return (double) this.offsetY + this.event.player.lastTickPosY + (this.event.player.posY - this.event.player.lastTickPosY) * (double) this.event.partialTicks;
    }

    public double interpolateZ() {
        return (double) this.offsetZ + this.event.player.lastTickPosZ + (this.event.player.posZ - this.event.player.lastTickPosZ) * (double) this.event.partialTicks;
    }

    public float getRelHitX() {
        return (float) (this.event.target.hitVec.xCoord - this.event.target.blockX + this.offsetX);
    }

    public float getRelHitY() {
        return (float) (this.event.target.hitVec.yCoord - this.event.target.blockY + this.offsetY);
    }

    public float getRelHitZ() {
        return (float) (this.event.target.hitVec.zCoord - this.event.target.blockZ + this.offsetZ);
    }

    public int getBlockX() {
        return this.offsetX + this.event.target.blockX;
    }

    public int getBlockY() {
        return this.offsetY + this.event.target.blockY;
    }

    public int getBlockZ() {
        return this.offsetZ + this.event.target.blockZ;
    }

    public EntityPlayer getPlayer() {
        return this.event.player;
    }

    public World getWorld() {
        return this.event.player.worldObj;
    }

    public RenderBlocks getRenderBlocks() {
        return this.event.context.globalRenderBlocks;
    }

    public int getSideHit() {
        return this.sideHit;
    }

    public int getHitBlockId() {
        World world = this.getWorld();
        MovingObjectPosition pos = this.event.target;
        return world.getBlockId(pos.blockX + this.offsetX, pos.blockY + this.offsetY, pos.blockZ + this.offsetZ);
    }

    public void setSideHit(int side) {
        this.sideHit = side;
    }

    public void setOffset(int offsetX, int offsetY, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

}
