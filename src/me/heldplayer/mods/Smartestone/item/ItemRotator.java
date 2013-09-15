
package me.heldplayer.mods.Smartestone.item;

import me.heldplayer.mods.Smartestone.block.BlockMulti;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRotator extends Item {

    public ItemRotator(int itemId) {
        super(itemId);
        this.setMaxStackSize(1);
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
