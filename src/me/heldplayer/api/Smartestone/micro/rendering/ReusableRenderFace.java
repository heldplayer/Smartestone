
package me.heldplayer.api.Smartestone.micro.rendering;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;

public class ReusableRenderFace {

    public int side;
    public double offset;
    public double startU;
    public double endU;
    public double startV;
    public double endV;

    public Icon icon;
    public int renderPass;
    public boolean renders;
    public int color;

    public ReusableRenderFace() {
        this.side = 0;
        this.offset = 0.0D;
        this.startU = 0.0D;
        this.endU = 1.0D;
        this.startV = 0.0D;
        this.endV = 1.0D;
        this.color = 0xFFFFFF;

        this.renders = true;
    }

    public void copy(ReusableRenderFace other) {
        this.side = other.side;
        this.offset = other.offset;
        this.startU = other.startU;
        this.endU = other.endU;
        this.startV = other.startV;
        this.endV = other.endV;
        this.icon = other.icon;
        this.renderPass = other.renderPass;
        this.renders = other.renders;
        this.color = other.color;
    }

    public int getColor() {
        int red = (this.color >> 16) & 0xFF;
        int green = (this.color >> 8) & 0xFF;
        int blue = this.color & 0xFF;

        if (this.side == 0) {
            red *= 0.5F;
            green *= 0.5F;
            blue *= 0.5F;
        }
        if (this.side == 2 || this.side == 3) {
            red *= 0.8F;
            green *= 0.8F;
            blue *= 0.8F;
        }
        if (this.side == 4 || this.side == 5) {
            red *= 0.6F;
            green *= 0.6F;
            blue *= 0.6F;
        }

        return red << 16 | green << 8 | blue;
    }

    public void setValues(int side, double offset, double startU, double endU, double startV, double endV) {
        this.side = side;
        this.offset = offset;
        this.startU = startU;
        this.endU = endU;
        this.startV = startV;
        this.endV = endV;

        this.renders = true;
    }

    public void setValues(AxisAlignedBB aabb, int side) {
        this.side = side;

        if (aabb == null) {
            this.renders = false;
            return;
        }

        this.renders = true;

        switch (this.side) {
        case 0:
            this.offset = aabb.minY;
            this.startU = aabb.minX;
            this.endU = aabb.maxX;
            this.startV = aabb.minZ;
            this.endV = aabb.maxZ;
        break;
        case 1:
            this.offset = aabb.maxY;
            this.startU = aabb.minX;
            this.endU = aabb.maxX;
            this.startV = aabb.minZ;
            this.endV = aabb.maxZ;
        break;
        case 2:
            this.offset = aabb.minZ;
            this.startU = aabb.minX;
            this.endU = aabb.maxX;
            this.startV = aabb.minY;
            this.endV = aabb.maxY;
        break;
        case 3:
            this.offset = aabb.maxZ;
            this.startU = aabb.minX;
            this.endU = aabb.maxX;
            this.startV = aabb.minY;
            this.endV = aabb.maxY;
        break;
        case 4:
            this.offset = aabb.minX;
            this.startU = aabb.minZ;
            this.endU = aabb.maxZ;
            this.startV = aabb.minY;
            this.endV = aabb.maxY;
        break;
        case 5:
            this.offset = aabb.maxX;
            this.startU = aabb.minZ;
            this.endU = aabb.maxZ;
            this.startV = aabb.minY;
            this.endV = aabb.maxY;
        break;
        }
    }

    @Override
    public String toString() {
        return "Face[side=" + this.side + ";offset=" + this.offset + ";renderPass=" + this.renderPass + ";color=" + this.color + "]";
    }

}
