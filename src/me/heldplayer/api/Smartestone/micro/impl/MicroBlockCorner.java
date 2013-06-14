
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.MicroBlockRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

public class MicroBlockCorner extends MicroBlockImpl {

    public final double width;

    public MicroBlockCorner(String typeName, double width) {
        super(typeName);
        this.renderBounds = new double[] { 0.5D - width / 2.0D, 0.5D - width / 2.0D, 0.5D - width / 2.0D, 0.5D + width / 2.0D, 0.5D + width / 2.0D, 0.5D + width / 2.0D };
        this.width = width;
    }

    @Override
    public AxisAlignedBB getBoundsInBlock(MicroBlockInfo info) {
        switch (info.getData()) {
        case 0:
            return AxisAlignedBB.getAABBPool().getAABB(1.0D - this.width, 1.0D - this.width, 1.0D - this.width, 1.0D, 1.0D, 1.0D); // ---111
        case 1:
            return AxisAlignedBB.getAABBPool().getAABB(1.0D - this.width, 0.0D, 0.0D, 1.0D, this.width, this.width); // -001++
        case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 1.0D - this.width, 0.0D, this.width, 1.0D, this.width); // 0-0+1+
        case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 1.0D - this.width, 1.0D - this.width, this.width, 1.0D, 1.0D); // 0--+11
        case 4:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, this.width, this.width, this.width); // 000+++
        case 5:
            return AxisAlignedBB.getAABBPool().getAABB(1.0D - this.width, 1.0D - this.width, 0.0D, 1.0D, 1.0D, this.width); // --011+
        case 6:
            return AxisAlignedBB.getAABBPool().getAABB(1.0D - this.width, 0.0D, 1.0D - this.width, 1.0D, this.width, 1.0D); // -0-1+1
        case 7:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 1.0D - this.width, this.width, this.width, 1.0D); // 00-++1
        }

        return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    }

    @Override
    public void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.8F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        Tessellator tessellator = Tessellator.instance;

        MovingObjectPosition pos = event.target;

        MicroBlockInfo usedInfo = new MicroBlockInfo(info.getMaterial(), info.getType(), 0);
        usedInfo.setData(this.onItemUse(event.player, pos.sideHit, (float) pos.hitVec.xCoord - (float) pos.blockX, (float) pos.hitVec.yCoord - (float) pos.blockY, (float) pos.hitVec.zCoord - (float) pos.blockZ));

        float outset = 0.003F;
        double x = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * (double) event.partialTicks;
        double y = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * (double) event.partialTicks;
        double z = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * (double) event.partialTicks;

        Block block = Block.blocksList[event.player.worldObj.getBlockId(pos.blockX, pos.blockY, pos.blockZ)];
        AxisAlignedBB bAabb = block.getSelectedBoundingBoxFromPool(event.player.worldObj, pos.blockX, pos.blockY, pos.blockZ).expand((double) outset, (double) outset, (double) outset).getOffsetBoundingBox(-x, -y, -z);
        AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D).offset(event.target.blockX, event.target.blockY, event.target.blockZ).expand((double) outset, (double) outset, (double) outset).getOffsetBoundingBox(-x, -y, -z);

        double minX = aabb.minX;
        double minY = aabb.minY;
        double minZ = aabb.minZ;
        double maxX = aabb.maxX;
        double maxY = aabb.maxY;
        double maxZ = aabb.maxZ;

        double midX = (minX + maxX) / 2.0D;
        double midY = (minY + maxY) / 2.0D;
        double midZ = (minZ + maxZ) / 2.0D;

        boolean swapped = false;

        tessellator.startDrawing(1);
        switch (pos.sideHit) {
        case 0:
            maxY = minY = bAabb.minY;
            tessellator.addVertex(midX, minY, minZ);
            tessellator.addVertex(midX, minY, maxZ);

            tessellator.addVertex(minX, minY, midZ);
            tessellator.addVertex(maxX, minY, midZ);
        break;
        case 1:
            minY = maxY = bAabb.maxY;
            tessellator.addVertex(midX, maxY, minZ);
            tessellator.addVertex(midX, maxY, maxZ);

            tessellator.addVertex(minX, maxY, midZ);
            tessellator.addVertex(maxX, maxY, midZ);
        break;
        case 2:
            maxZ = minZ = bAabb.minZ;
            tessellator.addVertex(minX, midY, maxZ);
            tessellator.addVertex(maxX, midY, maxZ);

            tessellator.addVertex(midX, minY, maxZ);
            tessellator.addVertex(midX, maxY, maxZ);
        break;
        case 3:
            minZ = maxZ = bAabb.maxZ;
            tessellator.addVertex(minX, midY, minZ);
            tessellator.addVertex(maxX, midY, minZ);

            tessellator.addVertex(midX, minY, minZ);
            tessellator.addVertex(midX, maxY, minZ);
        break;
        case 4:
            maxX = minX = bAabb.minX;
            tessellator.addVertex(maxX, midY, minZ);
            tessellator.addVertex(maxX, midY, maxZ);

            tessellator.addVertex(maxX, minY, midZ);
            tessellator.addVertex(maxX, maxY, midZ);

            swapped = true;
        break;
        case 5:
            minX = maxX = bAabb.maxX;
            tessellator.addVertex(minX, midY, minZ);
            tessellator.addVertex(minX, midY, maxZ);

            tessellator.addVertex(minX, minY, midZ);
            tessellator.addVertex(minX, maxY, midZ);

            swapped = true;
        break;
        }
        tessellator.draw();

        tessellator.startDrawing(3);
        tessellator.addVertex(minX, minY, minZ);
        if (swapped) {
            tessellator.addVertex(maxX, maxY, minZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(minX, minY, maxZ);
        }
        else {
            tessellator.addVertex(maxX, minY, minZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(minX, maxY, maxZ);
        }
        tessellator.addVertex(minX, minY, minZ);

        tessellator.draw();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        MicroBlockRenderHelper.renderMicroBlock(usedInfo, event);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        float u = MicroBlockAPI.getU(side, hitX, hitY, hitZ);
        float v = MicroBlockAPI.getV(side, hitX, hitY, hitZ);

        int vector = 0;

        if (u > 0.5F) {
            if (v > 0.5F) {
                vector = 1;
            }
            else {
                vector = 3;
            }
        }
        else {
            if (v > 0.5F) {
                vector = 0;
            }
            else {
                vector = 2;
            }
        }

        int data = -1;

        switch (vector) {
        case 0:
            switch (side) {
            case 0:
                data = 0;
            break;
            case 1:
                data = 1;
            break;
            case 2:
                data = 0;
            break;
            case 3:
                data = 2;
            break;
            case 4:
                data = 0;
            break;
            case 5:
                data = 2;
            break;
            }
        break;
        case 1:
            switch (side) {
            case 0:
                data = 3;
            break;
            case 1:
                data = 4;
            break;
            case 2:
                data = 3;
            break;
            case 3:
                data = 5;
            break;
            case 4:
                data = 5;
            break;
            case 5:
                data = 3;
            break;
            }
        break;
        case 2:
            switch (side) {
            case 0:
                data = 5;
            break;
            case 1:
                data = 6;
            break;
            case 2:
                data = 6;
            break;
            case 3:
                data = 4;
            break;
            case 4:
                data = 6;
            break;
            case 5:
                data = 4;
            break;
            }
        break;
        case 3:
            switch (side) {
            case 0:
                data = 2;
            break;
            case 1:
                data = 7;
            break;
            case 2:
                data = 7;
            break;
            case 3:
                data = 1;
            break;
            case 4:
                data = 1;
            break;
            case 5:
                data = 7;
            break;
            }
        break;
        }

        return data;
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
        MicroBlockCorner other = (MicroBlockCorner) obj;
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
