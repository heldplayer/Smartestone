
package net.minecraft.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.Icon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRedstoneWire extends Block {
    /**
     * When false, power transmission methods do not look at other redstone
     * wires. Used internally during
     * updateCurrentStrength.
     */
    private boolean wiresProvidePower = true;
    private Set blocksNeedingUpdate = new HashSet();
    @SideOnly(Side.CLIENT)
    private Icon cross;
    @SideOnly(Side.CLIENT)
    private Icon line;
    @SideOnly(Side.CLIENT)
    private Icon crossOverlay;
    @SideOnly(Side.CLIENT)
    private Icon lineOverlay;

    public BlockRedstoneWire(int blockId) {
        super(blockId, Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this
     * box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone
     * wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 5;
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return 0x800000;
    }

    /**
     * Checks to see if its valid to put this block at the specified
     * coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(x, y - 1, z) || world.getBlockId(x, y - 1, z) == Block.glowStone.blockID;
    }

    /**
     * Sets the strength of the wire current (0-15) for this block based on
     * neighboring blocks and propagates to
     * neighboring redstone wires
     */
    private void updateAndPropagateCurrentStrength(World world, int x, int y, int z) {
        this.calculateCurrentChanges(world, x, y, z, x, y, z);
        ArrayList list = new ArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (int i = 0; i < list.size(); ++i) {
            ChunkPosition chunkposition = (ChunkPosition) list.get(i);
            world.notifyBlocksOfNeighborChange(chunkposition.x, chunkposition.y, chunkposition.z, this.blockID);
        }
    }

    private void calculateCurrentChanges(World world, int x, int y, int z, int originX, int originY, int originZ) {
        int blockMeta = world.getBlockMetadata(x, y, z);
        int strength = this.getMaxCurrentStrength(world, originX, originY, originZ, 0);
        this.wiresProvidePower = false;
        int indirectPower = world.getStrongestIndirectPower(x, y, z);
        this.wiresProvidePower = true;

        if (indirectPower > 0 && indirectPower > strength - 1) {
            strength = indirectPower;
        }

        int surroundingStrength = 0;

        for (int direction = 0; direction < 4; ++direction) {
            int currentX = x;
            int currentZ = z;

            if (direction == 0) {
                currentX = x - 1;
            }

            if (direction == 1) {
                ++currentX;
            }

            if (direction == 2) {
                currentZ = z - 1;
            }

            if (direction == 3) {
                ++currentZ;
            }

            if (currentX != originX || currentZ != originZ) {
                surroundingStrength = this.getMaxCurrentStrength(world, currentX, y, currentZ, surroundingStrength);
            }

            if (world.isBlockNormalCube(currentX, y, currentZ) && !world.isBlockNormalCube(x, y + 1, z)) {
                if ((currentX != originX || currentZ != originZ) && y >= originY) {
                    surroundingStrength = this.getMaxCurrentStrength(world, currentX, y + 1, currentZ, surroundingStrength);
                }
            }
            else if (!world.isBlockNormalCube(currentX, y, currentZ) && (currentX != originX || currentZ != originZ) && y <= originY) {
                surroundingStrength = this.getMaxCurrentStrength(world, currentX, y - 1, currentZ, surroundingStrength);
            }
        }

        if (surroundingStrength > strength) {
            strength = surroundingStrength - 1;
        }
        else if (strength > 0) {
            --strength;
        }
        else {
            strength = 0;
        }

        if (indirectPower > strength - 1) {
            strength = indirectPower;
        }

        if (blockMeta != strength) {
            world.setBlockMetadataWithNotify(x, y, z, strength, 2);
            this.blocksNeedingUpdate.add(new ChunkPosition(x, y, z));
            this.blocksNeedingUpdate.add(new ChunkPosition(x - 1, y, z));
            this.blocksNeedingUpdate.add(new ChunkPosition(x + 1, y, z));
            this.blocksNeedingUpdate.add(new ChunkPosition(x, y - 1, z));
            this.blocksNeedingUpdate.add(new ChunkPosition(x, y + 1, z));
            this.blocksNeedingUpdate.add(new ChunkPosition(x, y, z - 1));
            this.blocksNeedingUpdate.add(new ChunkPosition(x, y, z + 1));
        }
    }

    /**
     * Calls World.notifyBlocksOfNeighborChange() for all neighboring blocks,
     * but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfNeighborChange(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) == this.blockID) {
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            this.updateAndPropagateCurrentStrength(world, x, y, z);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);

            if (world.isBlockNormalCube(x - 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x + 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x, y, z - 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
            }

            if (world.isBlockNormalCube(x, y, z + 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
            }
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an
     * update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, int blockId, int blockMeta) {
        super.breakBlock(world, x, y, z, blockId, blockMeta);

        if (!world.isRemote) {
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            this.updateAndPropagateCurrentStrength(world, x, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);

            if (world.isBlockNormalCube(x - 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x + 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x, y, z - 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
            }

            if (world.isBlockNormalCube(x, y, z + 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
            }
            else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
            }
        }
    }

    /**
     * Returns the current strength at the specified block if it is greater than
     * the passed value, or the passed value
     * otherwise. Signature: (world, x, y, z, strength)
     */
    private int getMaxCurrentStrength(World world, int x, int y, int z, int strength) {
        if (world.getBlockId(x, y, z) != this.blockID) {
            return strength;
        }
        else {
            int i1 = world.getBlockMetadata(x, y, z);
            return i1 > strength ? i1 : strength;
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (!world.isRemote) {
            boolean canStay = this.canPlaceBlockAt(world, x, y, z);

            if (canStay) {
                this.updateAndPropagateCurrentStrength(world, x, y, z);
            }
            else {
                this.dropBlockAsItem(world, x, y, z, 0, 0);
                world.setBlockToAir(x, y, z);
            }

            super.onNeighborBlockChange(world, x, y, z, blockId);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int meta, Random rand, int fortune) {
        return Item.redstone.itemID;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the
     * specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the
     * bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        return !this.wiresProvidePower ? 0 : this.isProvidingWeakPower(world, x, y, z, side);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the
     * specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and
     * this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when
     * checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        if (!this.wiresProvidePower) {
            return 0;
        }
        else {
            int blockMeta = world.getBlockMetadata(x, y, z);

            if (blockMeta == 0) {
                return 0;
            }
            else if (side == 1) {
                return blockMeta;
            }
            else {
                boolean east = isPoweredOrRepeater(world, x - 1, y, z, 1) || !world.isBlockNormalCube(x - 1, y, z) && isPoweredOrRepeater(world, x - 1, y - 1, z, -1);
                boolean west = isPoweredOrRepeater(world, x + 1, y, z, 3) || !world.isBlockNormalCube(x + 1, y, z) && isPoweredOrRepeater(world, x + 1, y - 1, z, -1);
                boolean south = isPoweredOrRepeater(world, x, y, z - 1, 2) || !world.isBlockNormalCube(x, y, z - 1) && isPoweredOrRepeater(world, x, y - 1, z - 1, -1);
                boolean north = isPoweredOrRepeater(world, x, y, z + 1, 0) || !world.isBlockNormalCube(x, y, z + 1) && isPoweredOrRepeater(world, x, y - 1, z + 1, -1);

                if (!world.isBlockNormalCube(x, y + 1, z)) {
                    if (world.isBlockNormalCube(x - 1, y, z) && isPoweredOrRepeater(world, x - 1, y + 1, z, -1)) {
                        east = true;
                    }

                    if (world.isBlockNormalCube(x + 1, y, z) && isPoweredOrRepeater(world, x + 1, y + 1, z, -1)) {
                        west = true;
                    }

                    if (world.isBlockNormalCube(x, y, z - 1) && isPoweredOrRepeater(world, x, y + 1, z - 1, -1)) {
                        south = true;
                    }

                    if (world.isBlockNormalCube(x, y, z + 1) && isPoweredOrRepeater(world, x, y + 1, z + 1, -1)) {
                        north = true;
                    }
                }

                return !south && !west && !east && !north && side >= 2 && side <= 5 ? blockMeta : (side == 2 && south && !east && !west ? blockMeta : (side == 3 && north && !east && !west ? blockMeta : (side == 4 && east && !south && !north ? blockMeta : (side == 5 && west && !south && !north ? blockMeta : 0))));
            }
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    public boolean canProvidePower() {
        return this.wiresProvidePower;
    }

    /**
     * Returns true if redstone wire can connect to the specified block. Params:
     * World, X, Y, Z, side (not a normal
     * notch-side, this can be 0, 1, 2, 3 or -1)
     */
    public static boolean isPowerProviderOrWire(IBlockAccess world, int x, int y, int z, int direction) {
        int blockId = world.getBlockId(x, y, z);

        if (blockId == Block.redstoneWire.blockID) {
            return true;
        }
        else if (blockId == 0) {
            return false;
        }
        else if (!Block.redstoneRepeaterIdle.func_94487_f(blockId)) {
            return (Block.blocksList[blockId] != null && Block.blocksList[blockId].canConnectRedstone(world, x, y, z, direction));
        }
        else {
            int j1 = world.getBlockMetadata(x, y, z);
            return direction == (j1 & 3) || direction == Direction.rotateOpposite[j1 & 3];
        }
    }

    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int blockMeta = world.getBlockMetadata(x, y, z);

        if (blockMeta > 0) {
            double posX = (double) x + 0.5D + ((double) random.nextFloat() - 0.5D) * 0.2D;
            double posY = (double) ((float) y + 0.0625F);
            double posZ = (double) z + 0.5D + ((double) random.nextFloat() - 0.5D) * 0.2D;
            float strenght = (float) blockMeta / 15.0F;
            float red = strenght * 0.6F + 0.4F;

            if (blockMeta == 0) {
                red = 0.0F;
            }

            float green = strenght * strenght * 0.7F - 0.5F;
            float blue = strenght * strenght * 0.6F - 0.7F;

            if (green < 0.0F) {
                green = 0.0F;
            }

            if (blue < 0.0F) {
                blue = 0.0F;
            }

            world.spawnParticle("reddust", posX, posY, posZ, (double) red, (double) green, (double) blue);
        }
    }

    /**
     * Returns true if the block coordinate passed can provide power, or is a
     * redstone wire, or if its a repeater that
     * is powered.
     */
    public static boolean isPoweredOrRepeater(IBlockAccess world, int x, int y, int z, int direction) {
        if (isPowerProviderOrWire(world, x, y, z, direction)) {
            return true;
        }
        else {
            int blockId = world.getBlockId(x, y, z);

            if (blockId == Block.redstoneRepeaterActive.blockID) {
                int blockMeta = world.getBlockMetadata(x, y, z);
                return direction == (blockMeta & 3);
            }
            else {
                return false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World world, int x, int y, int z) {
        return Item.redstone.itemID;
    }

    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister register) {
        this.cross = register.registerIcon("redstoneDust_cross");
        this.line = register.registerIcon("redstoneDust_line");
        this.crossOverlay = register.registerIcon("redstoneDust_cross_overlay");
        this.lineOverlay = register.registerIcon("redstoneDust_line_overlay");
        this.blockIcon = this.cross;
    }

    @SideOnly(Side.CLIENT)
    public static Icon func_94409_b(String par0Str) {
        return par0Str == "redstoneDust_cross" ? Block.redstoneWire.cross : (par0Str == "redstoneDust_line" ? Block.redstoneWire.line : (par0Str == "redstoneDust_cross_overlay" ? Block.redstoneWire.crossOverlay : (par0Str == "redstoneDust_line_overlay" ? Block.redstoneWire.lineOverlay : null)));
    }
}
