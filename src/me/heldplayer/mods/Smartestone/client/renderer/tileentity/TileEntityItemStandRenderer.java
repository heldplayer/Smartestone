
package me.heldplayer.mods.Smartestone.client.renderer.tileentity;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityItemStand;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.storage.MapData;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityItemStandRenderer extends TileEntitySpecialRenderer {

    private final EntityItem entityitem = new EntityItem(null);

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTicks) {
        if (tileentity == null || !(tileentity instanceof TileEntityItemStand)) {
            return;
        }

        TileEntityItemStand tile = (TileEntityItemStand) tileentity;
        Direction direction = tile.direction;
        Rotation rotation = tile.rotation;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        this.entityitem.worldObj = tile.worldObj;
        this.entityitem.hoverStart = tile.prevHover + (tile.hover - tile.prevHover) * partialTicks;

        ItemStack stack = tile.getStackInSlot(0);
        if (stack != null) {
            this.entityitem.setEntityItemStack(stack.copy());
            this.entityitem.getEntityItem().stackSize = 1;

            if (stack.getItem() == Item.map) {
                switch (direction.ordinal()) {
                case 1:
                    GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                break;
                case 2:
                    GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
                break;
                case 3:
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
                case 4:
                    GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                break;
                case 5:
                    GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
                break;
                }
                switch (rotation.ordinal()) {
                case 1:
                    GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
                break;
                case 2:
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                break;
                case 3:
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                break;
                }

                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(0.004F, 0.004F, 0.004F);

                RenderManager manager = RenderManager.instance;

                manager.renderEngine.bindTexture("/misc/mapbg.png");
                Tessellator tessellator = Tessellator.instance;
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-64.0F, -64.0F, 80.0F);
                GL11.glNormal3f(0.0F, 0.0F, -1.0F);
                tessellator.startDrawingQuads();
                double size = 7.0D;
                tessellator.addVertexWithUV(0.0D - size, 128.0D + size, 0.0D, 0.0D, 1.0D);
                tessellator.addVertexWithUV(128.0D + size, 128.0D + size, 0.0D, 1.0D, 1.0D);
                tessellator.addVertexWithUV(128.0D + size, 0.0D - size, 0.0D, 1.0D, 0.0D);
                tessellator.addVertexWithUV(0.0D - size, 0.0D - size, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                MapData mapdata = Item.map.getMapData(entityitem.getEntityItem(), this.entityitem.worldObj);
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);

                if (mapdata != null) {
                    manager.itemRenderer.mapItemRenderer.renderMap((EntityPlayer) null, manager.renderEngine, mapdata);
                }
            }
            else {
                GL11.glTranslatef(0.0F, -0.1F, 0.0F);

                GL11.glPushMatrix();
                //GL11.glTranslated(0.0D, 0.4D, 0.0D);
                //GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);

                RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                GL11.glPopMatrix();
            }
        }
        GL11.glPopMatrix();
    }

}
