
package me.heldplayer.api.Smartestone.micro;

import me.heldplayer.api.Smartestone.micro.rendering.ReusableRenderFace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public interface IMicroBlockSubBlock {

    public abstract String getTypeName();

    public abstract boolean isMaterialApplicable(IMicroBlockMaterial material);

    public abstract double[] getRenderBounds();

    public abstract AxisAlignedBB getBoundsInBlock(MicroBlockInfo info);

    public abstract ReusableRenderFace[] getRenderFaces(MicroBlockInfo info);

    public abstract AxisAlignedBB getPlacementBounds(MicroBlockInfo info);

    public abstract void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info);

    public abstract int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ);

    public abstract boolean isSideSolid(MicroBlockInfo info, int side);

    public abstract int getRedstoneStrength(MicroBlockInfo info, int side);

    public abstract void onBlockUpdate(MicroBlockInfo info, MicroBlockInfo[] infos, World world, int x, int y, int z);

    public abstract int getPowerOutput(MicroBlockInfo info, int side);

}
