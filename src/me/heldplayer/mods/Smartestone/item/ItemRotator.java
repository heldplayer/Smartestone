
package me.heldplayer.mods.Smartestone.item;

import me.heldplayer.mods.Smartestone.block.BlockMulti;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRotator extends Item {

    @SideOnly(Side.CLIENT)
    private Icon icon;

    public ItemRotator(int itemId) {
        super(itemId);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon(Objects.TEXTURE_PREFIX + ":rotator");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        return this.icon;
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        if (player.isSneaking()) {
            Block block = Block.blocksList[world.getBlockId(x, y, z)];

            if (block.hasTileEntity(world.getBlockMetadata(x, y, z)) && block instanceof BlockMulti) {
                return true;
            }
        }

        return super.onItemUse(stack, player, world, x, y, z, side, posX, posY, posZ);
    }
}
