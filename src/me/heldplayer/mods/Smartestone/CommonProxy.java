
package me.heldplayer.mods.Smartestone;

import static me.heldplayer.mods.Smartestone.util.Objects.*;

import java.util.Random;

import me.heldplayer.api.Smartestone.micro.IconProvider;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.impl.MaterialBlock;
import me.heldplayer.api.Smartestone.micro.impl.MaterialWire;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockCentralWire;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockCorner;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockPane;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockPillar;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockStrip;
import me.heldplayer.api.Smartestone.micro.placement.PlacementRuleCentralWire;
import me.heldplayer.api.Smartestone.micro.placement.PlacementRuleSameType;
import me.heldplayer.mods.Smartestone.block.BlockMicro;
import me.heldplayer.mods.Smartestone.block.BlockMulti1;
import me.heldplayer.mods.Smartestone.block.BlockMulti2;
import me.heldplayer.mods.Smartestone.client.gui.CreativeTab;
import me.heldplayer.mods.Smartestone.client.gui.GuiCraftingChest;
import me.heldplayer.mods.Smartestone.client.gui.GuiInductionishFurnace;
import me.heldplayer.mods.Smartestone.client.gui.GuiItemStand;
import me.heldplayer.mods.Smartestone.enchantment.EnchantmentDurabilityExt;
import me.heldplayer.mods.Smartestone.inventory.ContainerInductionishFurnace;
import me.heldplayer.mods.Smartestone.inventory.ContainerItemStand;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.ContainerCraftingChest;
import me.heldplayer.mods.Smartestone.item.ItemBlockMulti;
import me.heldplayer.mods.Smartestone.item.ItemMicroBlock;
import me.heldplayer.mods.Smartestone.item.ItemRotator;
import me.heldplayer.mods.Smartestone.item.ItemWaterCore;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityInductionishFurnace;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityItemStand;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.util.HeldCore.HeldCoreProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy extends HeldCoreProxy implements IGuiHandler {

    public static Random rand;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        log = event.getModLog();
        rand = new Random();

        MicroBlockAPI.microBlockId = ModSmartestone.blockMicroId.getValue();
        redstoneIcon = new IconProvider();
        bluestoneIcon = new IconProvider();
        greenstoneIcon = new IconProvider();
        yellowstoneIcon = new IconProvider();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        blockMulti1 = new BlockMulti1(ModSmartestone.blockMulti1Id.getValue(), Material.rock);
        blockMulti1.setUnlocalizedName("SSMulti1");
        GameRegistry.registerBlock(blockMulti1, ItemBlockMulti.class, "SSMulti1");
        GameRegistry.addRecipe(new ItemStack(blockMulti1, 1, 0), "obo", "scs", "sCs", 'o', new ItemStack(Block.cloth, 1, 1), 'b', new ItemStack(Block.cloth, 1, 12), 's', Block.stone, 'C', Block.workbench, 'c', Block.chest);

        blockMulti2 = new BlockMulti2(ModSmartestone.blockMulti2Id.getValue(), Material.rock);
        blockMulti2.setUnlocalizedName("SSMulti2");
        GameRegistry.registerBlock(blockMulti2, ItemBlockMulti.class, "SSMulti2");
        GameRegistry.addRecipe(new ItemStack(blockMulti2, 1, 0), "bbb", "bsb", 'b', new ItemStack(Block.cloth, 1, 15), 's', Block.stone);

        blockMicro = new BlockMicro(ModSmartestone.blockMicroId.getValue());
        blockMicro.setUnlocalizedName("SSMulti2");
        GameRegistry.registerBlock(blockMicro, "SSMicro");

        itemRotator = new ItemRotator(ModSmartestone.itemRotatorId.getValue());
        itemRotator.setUnlocalizedName("SSRotator");
        GameRegistry.registerItem(itemRotator, "SSRotator");

        itemMicroBlock = new ItemMicroBlock(ModSmartestone.itemMicroBlockId.getValue());
        GameRegistry.registerItem(itemMicroBlock, "SSMicroBlockItem");

        itemWaterCore = new ItemWaterCore(ModSmartestone.itemWaterCoreId.getValue());
        itemWaterCore.setUnlocalizedName("SSWaterCore");
        GameRegistry.registerItem(itemWaterCore, "SSWaterCore");
        GameRegistry.addRecipe(new ItemStack(itemWaterCore, 1, 0), "iIi", "IbI", "iIi", 'i', Item.ingotIron, 'I', Block.ice, 'b', Item.bucketWater);

        creativeTab = new CreativeTab("Smartestone", new ItemStack(blockMulti1, 1, 0));
        creativeTabMicroblocks = new CreativeTab("SmartestoneMicroblocks", new ItemStack(itemRotator, 1, 0));
        blockMulti1.setCreativeTab(creativeTab);
        blockMulti2.setCreativeTab(creativeTab);
        itemRotator.setCreativeTab(creativeTab);
        itemMicroBlock.setCreativeTab(creativeTabMicroblocks);
        itemWaterCore.setCreativeTab(creativeTab);

        NetworkRegistry.instance().registerGuiHandler(ModSmartestone.instance, this);
        GameRegistry.registerTileEntity(TileEntityCraftingChest.class, "SSCraftingChest");
        GameRegistry.registerTileEntity(TileEntityInductionishFurnace.class, "SSInductionishFurnace");
        GameRegistry.registerTileEntity(TileEntityItemStand.class, "SSItemStand");
        GameRegistry.registerTileEntity(TileEntityMicro.class, "SSMicroBlock");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Cover", 0.125D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Panel", 0.25D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Tricover", 0.375D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Slab", 0.5D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Anti-Tricover", 0.625D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Anti-Panel", 0.75D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPane("Anti-Cover", 0.875D));

        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Cover Strip", 0.125D));
        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Panel Strip", 0.25D));
        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Tricover Strip", 0.375D));
        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Slab Strip", 0.5D));
        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Anti-Tricover Strip", 0.625D));
        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Anti-Panel Strip", 0.75D));
        MicroBlockAPI.registerSubBlock(new MicroBlockStrip("Anti-Cover Strip", 0.875D));

        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Cover Pillar", 0.125D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Panel Pillar", 0.25D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Tricover Pillar", 0.375D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Slab Pillar", 0.5D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Anti-Tricover Pillar", 0.625D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Anti-Panel Pillar", 0.75D));
        MicroBlockAPI.registerSubBlock(new MicroBlockPillar("Anti-Cover Pillar", 0.875D));

        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Cover Corner", 0.125D));
        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Panel Corner", 0.25D));
        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Tricover Corner", 0.375D));
        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Slab Corner", 0.5D));
        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Anti-Tricover Corner", 0.625D));
        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Anti-Panel Corner", 0.75D));
        MicroBlockAPI.registerSubBlock(new MicroBlockCorner("Anti-Cover Corner", 0.875D));

        MicroBlockAPI.registerSubBlock(new MicroBlockCentralWire("Central Wire"));

        int[] blocks = new int[] { 1, 3, 4, 12, 13, 22, 41, 42, 45, 48, 49, 57, 79, 80, 82, 87, 88, 89, 98, 112, 121, 133, 152 };

        for (int block : blocks) {
            MicroBlockAPI.registerMaterial(new MaterialBlock(new ItemStack(block, 1, 0)));
        }

        for (int meta = 0; meta < 4; meta++) {
            MicroBlockAPI.registerMaterial(new MaterialBlock(new ItemStack(5, 1, meta)));
        }
        for (int meta = 0; meta < 4; meta++) {
            MicroBlockAPI.registerMaterial(new MaterialBlock(new ItemStack(17, 1, meta)));
        }
        for (int meta = 0; meta < 16; meta++) {
            MicroBlockAPI.registerMaterial(new MaterialBlock(new ItemStack(35, 1, meta)));
        }

        MicroBlockAPI.registerMaterial(new MaterialWire("WireRedstone", "Redstone Wire", redstoneIcon));
        MicroBlockAPI.registerMaterial(new MaterialWire("WireBluestone", "Bluestone Wire", bluestoneIcon));
        MicroBlockAPI.registerMaterial(new MaterialWire("WireGreenstone", "Greenstone Wire", greenstoneIcon));
        MicroBlockAPI.registerMaterial(new MaterialWire("WireYellowstone", "Yellowstone Wire", yellowstoneIcon));

        EnchantmentDurability enchant = (EnchantmentDurability) Enchantment.enchantmentsList[Enchantment.unbreaking.effectId];
        Enchantment.enchantmentsList[Enchantment.unbreaking.effectId] = null;
        new EnchantmentDurabilityExt(enchant);

        MicroBlockAPI.registerPlacementRule(new PlacementRuleSameType());
        MicroBlockAPI.registerPlacementRule(new PlacementRuleCentralWire());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ID == 0) {
            if (tileEntity != null && (tileEntity instanceof TileEntityCraftingChest)) {
                return new GuiCraftingChest(player.inventory, world, x, y, z, ((TileEntityCraftingChest) tileEntity));
            }
            else {
                return null;
            }
        }
        if (ID == 1) {
            if (tileEntity != null && (tileEntity instanceof TileEntityInductionishFurnace)) {
                return new GuiInductionishFurnace(player.inventory, ((TileEntityInductionishFurnace) tileEntity));
            }
            else {
                return null;
            }
        }
        if (ID == 16) {
            if (tileEntity != null && (tileEntity instanceof TileEntityItemStand)) {
                return new GuiItemStand(player.inventory, ((TileEntityItemStand) tileEntity));
            }
            else {
                return null;
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ID == 0) {
            if (tileEntity != null && (tileEntity instanceof TileEntityCraftingChest)) {
                return new ContainerCraftingChest(player.inventory, world, x, y, z, ((TileEntityCraftingChest) tileEntity));
            }
            else {
                return null;
            }
        }
        if (ID == 1) {
            if (tileEntity != null && (tileEntity instanceof TileEntityInductionishFurnace)) {
                return new ContainerInductionishFurnace(player.inventory, ((TileEntityInductionishFurnace) tileEntity));
            }
            else {
                return null;
            }
        }
        if (ID == 16) {
            if (tileEntity != null && (tileEntity instanceof TileEntityItemStand)) {
                return new ContainerItemStand(player.inventory, ((TileEntityItemStand) tileEntity));
            }
            else {
                return null;
            }
        }

        return null;
    }

}
