
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IMicroBlockSubBlock;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.RenderFacePool;
import me.heldplayer.api.Smartestone.micro.rendering.ReusableRenderFace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public abstract class MicroBlockImpl implements IMicroBlockSubBlock {

    public String typeName;
    public double[] renderBounds;

    public MicroBlockImpl(String typeName) {
        this.typeName = typeName;
        this.renderBounds = new double[] { 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D };
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public boolean isMaterialApplicable(IMicroBlockMaterial material) {
        return material.isBlock();
    }

    @Override
    public double[] getRenderBounds() {
        return this.renderBounds;
    }

    @Override
    public abstract AxisAlignedBB getBoundsInBlock(MicroBlockInfo info);

    @Override
    public abstract void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info);

    @Override
    public abstract int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ);

    @Override
    public AxisAlignedBB getPlacementBounds(MicroBlockInfo info) {
        return this.getBoundsInBlock(info);
    }

    @Override
    public ReusableRenderFace[] getRenderFaces(MicroBlockInfo info) {
        AxisAlignedBB aabb = this.getBoundsInBlock(info);

        ReusableRenderFace[] faces = new ReusableRenderFace[6];

        for (int i = 0; i < faces.length; i++) {
            faces[i] = RenderFacePool.getAFace();
            faces[i].setValues(aabb, i);
        }

        return faces;
    }

    @Override
    public boolean isSideSolid(MicroBlockInfo info, int side) {
        return false;
    }

    @Override
    public int getRedstoneStrength(MicroBlockInfo info, int side) {
        return 0;
    }

    @Override
    public void onBlockUpdate(MicroBlockInfo info, World world, int x, int y, int z) {}

}
