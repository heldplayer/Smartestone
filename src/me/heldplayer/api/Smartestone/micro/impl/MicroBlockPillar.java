
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.MicroBlockRenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MicroBlockPillar extends MicroBlockImpl {

    public final double width;

    public MicroBlockPillar(String typeName, double width) {
        super(typeName);
        this.typeName = typeName;
        this.renderBounds = new double[] { 0.5D - width / 2.0D, 0.0D, 0.5D - width / 2.0D, 0.5D + width / 2.0D, 1.0D, 0.5D + width / 2.0D };
        this.width = width;
    }

    @Override
    public AxisAlignedBB getBoundsInBlock(MicroBlockInfo info) {
        switch (info.getData()) {
        case 0:
            return AxisAlignedBB.getAABBPool().getAABB((1.0D - this.width) / 2.0D, 0.0D, (1.0D - this.width) / 2.0D, (1.0D + this.width) / 2.0D, 1.0D, (1.0D + this.width) / 2.0D);
        case 1:
            return AxisAlignedBB.getAABBPool().getAABB((1.0D - this.width) / 2.0D, (1.0D - this.width) / 2.0D, 0.0D, (1.0D + this.width) / 2.0D, (1.0D + this.width) / 2.0D, 1.0D);
        case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, (1.0D - this.width) / 2.0D, (1.0D - this.width) / 2.0D, 1.0D, (1.0D + this.width) / 2.0D, (1.0D + this.width) / 2.0D);
        }

        return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.8F);
        GL11.glLineWidth(2.0F);

        MovingObjectPosition pos = event.target;

        MicroBlockInfo usedInfo = new MicroBlockInfo(info.getMaterial(), info.getType(), 0);
        usedInfo.setData(this.onItemUse(event.player, pos.sideHit, (float) pos.hitVec.xCoord - (float) pos.blockX, (float) pos.hitVec.yCoord - (float) pos.blockY, (float) pos.hitVec.zCoord - (float) pos.blockZ));

        MicroBlockRenderHelper.renderMicroBlock(usedInfo, event);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        switch (side) {
        case 0:
        case 1:
            return 0;
        case 2:
        case 3:
            return 1;
        case 4:
        case 5:
            return 2;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.typeName == null) ? 0 : this.typeName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(this.width);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        MicroBlockPillar other = (MicroBlockPillar) obj;
        if (this.typeName == null) {
            if (other.typeName != null) {
                return false;
            }
        }
        else if (!this.typeName.equals(other.typeName)) {
            return false;
        }
        if (Double.doubleToLongBits(this.width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        return true;
    }

}
