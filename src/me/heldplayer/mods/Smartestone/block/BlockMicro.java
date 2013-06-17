
package me.heldplayer.mods.Smartestone.block;

import java.util.List;
import java.util.Random;
import java.util.Set;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockCentralWire;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.mods.Smartestone.util.Objects;
import me.heldplayer.mods.Smartestone.util.RayTrace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMicro extends Block {

    public static int renderId = 0;
    private static Random rnd = new Random();
    @SideOnly(Side.CLIENT)
    private Icon icon;
    public int renderPass;

    public BlockMicro(int blockId) {
        super(blockId, Material.rock);

        this.setHardness(1.0F);
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        this.renderPass = pass;
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2) {
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon("stone");
        Objects.redstoneIcon.icon = register.registerIcon(Objects.TEXTURE_PREFIX + ":redstone");
        Objects.bluestoneIcon.icon = register.registerIcon(Objects.TEXTURE_PREFIX + ":bluestone");
        Objects.greenstoneIcon.icon = register.registerIcon(Objects.TEXTURE_PREFIX + ":greenstone");
        Objects.yellowstoneIcon.icon = register.registerIcon(Objects.TEXTURE_PREFIX + ":yellowstone");
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityMicro();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB boundingBox, List list, Entity entity) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            super.addCollisionBoxesToList(world, x, y, z, boundingBox, list, entity);
            return;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        for (MicroBlockInfo info : infos) {
            AxisAlignedBB aabb = info.getType().getBoundsInBlock(info);

            if (aabb == null) {
                continue;
            }

            aabb.offset(x, y, z);

            if (aabb != null && boundingBox.intersectsWith(aabb)) {
                list.add(aabb);
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        // TODO: Find a way for items AND buckets to be happy about this
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int blockId, int eventId) {
        if (world.isRemote) {
            return false;
        }

        super.onBlockEventReceived(world, x, y, z, blockId, eventId);
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            tileEntity.receiveClientEvent(blockId, eventId);
        }

        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.setBlockTileEntity(x, y, z, this.createTileEntity(world, world.getBlockMetadata(x, y, z)));
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return super.collisionRayTrace(world, x, y, z, start, end);
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        if (infos.size() == 0) {
            return super.collisionRayTrace(world, x, y, z, start, end);
        }

        MovingObjectPosition pos = null;

        for (MicroBlockInfo info : infos) {
            AxisAlignedBB aabb = info.getType().getBoundsInBlock(info);

            if (aabb == null) {
                continue;
            }

            this.setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);

            MovingObjectPosition currentPos = super.collisionRayTrace(world, x, y, z, start, end);

            if (pos == null || (currentPos != null && start.distanceTo(currentPos.hitVec) < start.distanceTo(pos.hitVec))) {
                pos = currentPos;
            }
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

        return pos;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);
        }

        if (tile.getSubBlocks().size() == 0) {
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);
        }

        if (RayTrace.aabb == null) {
            return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        }

        return RayTrace.aabb.copy().offset(x, y, z);
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return true;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        RayTrace.rayTrace(world, player, x, y, z);

        MicroBlockInfo targetted = null;

        for (MicroBlockInfo info : infos) {
            if (info.equals(RayTrace.info)) {
                targetted = info;
                break;
            }
        }

        if (targetted != null && !world.isRemote && player.capabilities.allowEdit) {
            tile.removeInfo(targetted);
            if (!world.isRemote && !player.capabilities.isCreativeMode) {
                ItemStack stack = new ItemStack(Objects.itemMicroBlock.itemID, 1, 0);
                NBTTagCompound compound = new NBTTagCompound("tag");
                compound.setString("Material", targetted.getMaterial().getIdentifier());
                compound.setString("Type", targetted.getType().getTypeName());
                stack.setTagCompound(compound);

                float xMotion = rnd.nextFloat() * 0.8F + 0.1F;
                float yMotion = rnd.nextFloat() * 0.8F + 0.1F;
                float zMotion = rnd.nextFloat() * 0.8F + 0.1F;

                EntityItem item = new EntityItem(world, (x + xMotion), (y + yMotion), (z + zMotion), stack);

                if (stack.hasTagCompound()) {
                    item.setEntityItemStack(stack);
                }

                item.motionX = ((float) rnd.nextGaussian() * 0.05F);
                item.motionY = ((float) rnd.nextGaussian() * 0.05F + 0.2F);
                item.motionZ = ((float) rnd.nextGaussian() * 0.05F);
                item.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(item);
            }
        }

        int id = this.blockID;

        if (infos.size() == 0) {
            world.setBlockToAir(x, y, z);
            id = 0;
        }

        world.notifyBlockChange(x, y, z, id);

        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return null;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        MicroBlockInfo targetted = null;

        Vec3 hit = target.hitVec.addVector(-x, -y, -z);

        for (MicroBlockInfo info : infos) {
            AxisAlignedBB aabb = info.getType().getBoundsInBlock(info);

            if (aabb == null) {
                continue;
            }

            if (hit.xCoord < aabb.minX || hit.xCoord > aabb.maxX) {
                continue;
            }
            if (hit.yCoord < aabb.minY || hit.yCoord > aabb.maxY) {
                continue;
            }
            if (hit.zCoord < aabb.minZ || hit.zCoord > aabb.maxZ) {
                continue;
            }

            targetted = info;

            break;
        }

        if (targetted != null) {
            ItemStack stack = new ItemStack(Objects.itemMicroBlock.itemID, 1, 0);
            NBTTagCompound compound = new NBTTagCompound("tag");
            compound.setString("Material", targetted.getMaterial() != null ? targetted.getMaterial().getIdentifier() : "null");
            compound.setString("Type", targetted.getType() != null ? targetted.getType().getTypeName() : "null");
            stack.setTagCompound(compound);

            return stack;
        }

        return null;
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return true;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        for (MicroBlockInfo info : infos) {
            if (info.getType().isSideSolid(info, side.ordinal())) {
                return true;
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
        int x = target.blockX;
        int y = target.blockY;
        int z = target.blockZ;
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return true;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        MicroBlockInfo targetted = null;

        Vec3 hit = target.hitVec.addVector(-x, -y, -z);

        for (MicroBlockInfo info : infos) {
            AxisAlignedBB aabb = info.getType().getBoundsInBlock(info);

            if (aabb == null) {
                continue;
            }

            if (hit.xCoord < aabb.minX || hit.xCoord > aabb.maxX) {
                continue;
            }
            if (hit.yCoord < aabb.minY || hit.yCoord > aabb.maxY) {
                continue;
            }
            if (hit.zCoord < aabb.minZ || hit.zCoord > aabb.maxZ) {
                continue;
            }

            targetted = info;

            break;
        }

        if (targetted != null) {
            RenderEngine engine = RenderManager.instance.renderEngine;

            AxisAlignedBB aabb = targetted.getType().getPlacementBounds(targetted);
            float offset = 0.1F;
            double pX = (double) x + rnd.nextDouble() * (aabb.maxX - aabb.minX - (double) (offset * 2.0F)) + (double) offset + aabb.minX;
            double pY = (double) y + rnd.nextDouble() * (aabb.maxY - aabb.minY - (double) (offset * 2.0F)) + (double) offset + aabb.minY;
            double pZ = (double) z + rnd.nextDouble() * (aabb.maxZ - aabb.minZ - (double) (offset * 2.0F)) + (double) offset + aabb.minZ;

            if (target.sideHit == 0) {
                pY = (double) y + aabb.minY - (double) offset;
            }

            if (target.sideHit == 1) {
                pY = (double) y + aabb.maxY + (double) offset;
            }

            if (target.sideHit == 2) {
                pZ = (double) z + aabb.minZ - (double) offset;
            }

            if (target.sideHit == 3) {
                pZ = (double) z + aabb.maxZ + (double) offset;
            }

            if (target.sideHit == 4) {
                pX = (double) x + aabb.minX - (double) offset;
            }

            if (target.sideHit == 5) {
                pX = (double) x + aabb.maxX + (double) offset;
            }

            EntityDiggingFX fx = (new EntityDiggingFX(world, pX, pY, pZ, 0.0D, 0.0D, 0.0D, this, target.sideHit, world.getBlockMetadata(x, y, z), engine));
            fx.func_70596_a(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
            if (targetted.getMaterial() != null) {
                fx.setParticleIcon(engine, targetted.getMaterial().getIcon(target.sideHit));
            }
            effectRenderer.addEffect(fx);

            return true;
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return true;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        RenderEngine renderEngine = RenderManager.instance.renderEngine;

        for (MicroBlockInfo info : infos) {
            byte pps = 2;

            for (int tX = 0; tX < pps; ++tX) {
                for (int tY = 0; tY < pps; ++tY) {
                    for (int tZ = 0; tZ < pps; ++tZ) {
                        double pX = (double) x + ((double) tX + 0.5D) / (double) pps;
                        double pY = (double) y + ((double) tY + 0.5D) / (double) pps;
                        double pZ = (double) z + ((double) tZ + 0.5D) / (double) pps;
                        int side = rnd.nextInt(6);

                        EntityDiggingFX fx = (new EntityDiggingFX(world, pX, pY, pZ, pX - (double) x - 0.5D, pY - (double) y - 0.5D, pZ - (double) z - 0.5D, this, side, meta, renderEngine));
                        fx.func_70596_a(x, y, z);
                        if (info.getMaterial() != null) {
                            fx.setParticleIcon(renderEngine, info.getMaterial().getIcon(0));
                        }
                        effectRenderer.addEffect(fx);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbour) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return;
        }

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        for (MicroBlockInfo info : infos) {
            info.getType().onBlockUpdate(info, world, x, y, z);
        }
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();

        if (world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == this.blockID) {
            TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            if (tile != null) {
                return 0;
            }
        }

        if (world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == Block.redstoneWire.blockID) {
            return 0;
        }

        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return 0;
        }

        int power = 0;

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        for (MicroBlockInfo info : infos) {
            int other = info.getType().getPowerOutput(info, side);
            if (other > power) {
                power = other;
            }
        }

        if (power > 15) {
            power = 15;
        }

        return power;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();

        if (!MicroBlockCentralWire.canConnectTo((World) world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, null)) {
            return 0;
        }

        int blockID = world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

        if (Block.blocksList[blockID] != null && Block.blocksList[blockID].isOpaqueCube()) {
            return 0;
        }

        if (blockID == this.blockID) {
            TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            if (tile != null) {
                return 0;
            }
        }

        if (blockID == Block.redstoneWire.blockID) {
            return 0;
        }

        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return 0;
        }

        int power = 0;

        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        for (MicroBlockInfo info : infos) {
            int other = info.getType().getPowerOutput(info, side);
            if (other > power) {
                power = other;
            }
        }

        if (power > 15) {
            power = 15;
        }

        return power;
    }

    @Override
    public boolean isAirBlock(World world, int x, int y, int z) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return true;
        }

        return tile.getSubBlocks().size() == 0;
    }

}
