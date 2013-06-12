
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

    public ReusableRenderFace() {
        this.side = 0;
        this.offset = 0.0D;
        this.startU = 0.0D;
        this.endU = 1.0D;
        this.startV = 0.0D;
        this.endV = 1.0D;
    }

    public void setValues(int side, double offset, double startU, double endU, double startV, double endV) {
        this.side = 0;
        this.offset = offset;
        this.startU = startU;
        this.endU = endU;
        this.startV = startV;
        this.endV = endV;
    }

    public void setValues(AxisAlignedBB aabb, int side) {
        this.side = side;
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

}
