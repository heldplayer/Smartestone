
package me.heldplayer.mods.Smartestone.item;

import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWaterCore extends Item implements IFluidContainerItem {

    @SideOnly(Side.CLIENT)
    private Icon icon;

    public ItemWaterCore(int itemId) {
        super(itemId);
        this.setMaxStackSize(1);
        this.setMaxDamage(256);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        // ForgeDirection dir = ForgeDirection.getOrientation(side);
        // x += dir.offsetX;
        // y += dir.offsetY;
        // z += dir.offsetZ;
        // if (stack.getItemDamage() < this.getMaxDamage() && (world.isAirBlock(x, y, z) || !world.getBlockMaterial(x, y, z).isSolid())) {
        // world.setBlock(x, y, z, Block.waterMoving.blockID, 0, 3);
        // stack.damageItem(1, player);
        // return true;
        // }
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useTime) {
        MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, false);

        useTime = this.getMaxItemUseDuration(stack) - useTime;

        if (useTime < 5) {
            if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE && stack.getItemDamage() < this.getMaxDamage()) {
                ForgeDirection side = ForgeDirection.getOrientation(pos.sideHit);
                int x = pos.blockX + side.offsetX;
                int y = pos.blockY + side.offsetY;
                int z = pos.blockZ + side.offsetZ;

                if (world.isAirBlock(x, y, z) || !world.getBlockMaterial(x, y, z).isSolid()) {
                    world.setBlock(x, y, z, Block.waterMoving.blockID, 0, 3);

                    stack.damageItem(1, player);
                }
            }
        }
        else {
            int reduction = useTime / 5;
            stack.setItemDamage(stack.getItemDamage() - reduction);
        }
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        stack.setItemDamage(0);
        return stack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (this.getMaxItemUseDuration(stack) > 0) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }
        else {
            MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, false);

            if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE && stack.getItemDamage() < this.getMaxDamage()) {
                ForgeDirection side = ForgeDirection.getOrientation(pos.sideHit);
                int x = pos.blockX + side.offsetX;
                int y = pos.blockY + side.offsetY;
                int z = pos.blockZ + side.offsetZ;

                if (world.isAirBlock(x, y, z) || !world.getBlockMaterial(x, y, z).isSolid()) {
                    world.setBlock(x, y, z, Block.waterMoving.blockID, 0, 3);

                    stack.damageItem(1, player);
                }
            }
        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon(Objects.TEXTURE_PREFIX + ":water_core");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        return this.icon;
    }

    @Override
    public int getItemEnchantability() {
        return 2;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return stack.getItemDamage() * 5;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    // FIXME: Test if this works

    @Override
    public FluidStack getFluid(ItemStack container) {
        FluidStack result = new FluidStack(FluidRegistry.WATER, container.getItemDamage() * 1000);
        return result;
    }

    @Override
    public int getCapacity(ItemStack container) {
        return 255000;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        int meta = container.getItemDamage();
        int inserted = resource.amount;
        int result = 0;
        while (inserted >= 1000 && meta < 0) {
            meta--;
            inserted -= 1000;
            result += 1000;
        }
        if (doFill) {
            container.setItemDamage(meta);
            resource.amount = inserted;
        }
        return result;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return null;
    }

}
