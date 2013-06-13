
package me.heldplayer.mods.Smartestone;

import java.util.Random;

import me.heldplayer.api.Smartestone.micro.IconProvider;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.impl.MicroBlockCentralWire;
import me.heldplayer.api.Smartestone.micro.impl.SimpleCornerMicroBlock;
import me.heldplayer.api.Smartestone.micro.impl.SimpleMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.impl.SimplePaneMicroBlock;
import me.heldplayer.api.Smartestone.micro.impl.SimpleStripMicroBlock;
import me.heldplayer.api.Smartestone.micro.impl.WireMaterial;
import me.heldplayer.mods.Smartestone.block.BlockMicro;
import me.heldplayer.mods.Smartestone.block.BlockMulti1;
import me.heldplayer.mods.Smartestone.block.BlockMulti2;
import me.heldplayer.mods.Smartestone.client.gui.CreativeTab;
import me.heldplayer.mods.Smartestone.client.gui.GuiCraftingChest;
import me.heldplayer.mods.Smartestone.client.gui.GuiInductionishFurnace;
import me.heldplayer.mods.Smartestone.client.gui.GuiItemStand;
import me.heldplayer.mods.Smartestone.inventory.ContainerInductionishFurnace;
import me.heldplayer.mods.Smartestone.inventory.ContainerItemStand;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.ContainerCraftingChest;
import me.heldplayer.mods.Smartestone.item.ItemBlockMulti;
import me.heldplayer.mods.Smartestone.item.ItemMicroBlock;
import me.heldplayer.mods.Smartestone.item.ItemRotator;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityInductionishFurnace;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityItemStand;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy implements IGuiHandler {

    public static Random rand;

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        Objects.log = event.getModLog();
        rand = new Random();

        MicroBlockAPI.microBlockId = ModSmartestone.blockMicroId.getValue();
        Objects.redstoneOnIcon = new IconProvider();
        Objects.redstoneOffIcon = new IconProvider();
    }

    public void init(FMLInitializationEvent event) {
        Objects.blockMulti1 = new BlockMulti1(ModSmartestone.blockMulti1Id.getValue(), Material.rock);
        Objects.blockMulti1.setUnlocalizedName("SSMulti1");
        GameRegistry.registerBlock(Objects.blockMulti1, ItemBlockMulti.class, "SSMulti1");
        GameRegistry.addRecipe(new ItemStack(Objects.blockMulti1, 1, 0), "obo", "scs", "sCs", 'o', new ItemStack(Block.cloth, 1, 1), 'b', new ItemStack(Block.cloth, 1, 12), 's', Block.stone, 'C', Block.workbench, 'c', Block.chest);

        Objects.blockMulti2 = new BlockMulti2(ModSmartestone.blockMulti2Id.getValue(), Material.rock);
        Objects.blockMulti2.setUnlocalizedName("SSMulti2");
        GameRegistry.registerBlock(Objects.blockMulti2, ItemBlockMulti.class, "SSMulti2");
        GameRegistry.addRecipe(new ItemStack(Objects.blockMulti2, 1, 0), "bbb", "bsb", 'b', new ItemStack(Block.cloth, 1, 15), 's', Block.stone);

        Objects.blockMicro = new BlockMicro(ModSmartestone.blockMicroId.getValue());
        Objects.blockMicro.setUnlocalizedName("SSMulti2");
        GameRegistry.registerBlock(Objects.blockMicro, "SSMicro");

        Objects.itemRotator = new ItemRotator(ModSmartestone.itemRotatorId.getValue());
        Objects.itemRotator.setUnlocalizedName("SSRotator");
        GameRegistry.registerItem(Objects.itemRotator, "SSRotator");

        Objects.itemMicroBlock = new ItemMicroBlock(ModSmartestone.itemMicroBlockId.getValue());
        GameRegistry.registerItem(Objects.itemMicroBlock, "SSMicroBlockItem");

        Objects.creativeTab = new CreativeTab("Smartestone", new ItemStack(Objects.blockMulti1, 1, 0));
        Objects.blockMulti1.setCreativeTab(Objects.creativeTab);
        Objects.blockMulti2.setCreativeTab(Objects.creativeTab);
        Objects.itemRotator.setCreativeTab(Objects.creativeTab);
        Objects.itemMicroBlock.setCreativeTab(Objects.creativeTab);

        this.registerLanguage();

        NetworkRegistry.instance().registerGuiHandler(ModSmartestone.instance, this);
        GameRegistry.registerTileEntity(TileEntityCraftingChest.class, "SSCraftingChest");
        GameRegistry.registerTileEntity(TileEntityInductionishFurnace.class, "SSInductionishFurnace");
        GameRegistry.registerTileEntity(TileEntityItemStand.class, "SSItemStand");
        GameRegistry.registerTileEntity(TileEntityMicro.class, "SSMicroBlock");
    }

    public void postInit(FMLPostInitializationEvent event) {
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Cover", 0.125D));
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Panel", 0.25D));
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Tricover", 0.375D));
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Slab", 0.5D));
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Anti-Tricover", 0.625D));
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Anti-Panel", 0.75D));
        MicroBlockAPI.registerSubBlock(new SimplePaneMicroBlock("Anti-Cover", 0.875D));

        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Cover Strip", 0.125D));
        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Panel Strip", 0.25D));
        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Tricover Strip", 0.375D));
        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Slab Strip", 0.5D));
        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Anti-Tricover Strip", 0.625D));
        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Anti-Panel Strip", 0.75D));
        MicroBlockAPI.registerSubBlock(new SimpleStripMicroBlock("Anti-Cover Strip", 0.875D));

        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Cover Corner", 0.125D));
        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Panel Corner", 0.25D));
        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Tricover Corner", 0.375D));
        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Slab Corner", 0.5D));
        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Anti-Tricover Corner", 0.625D));
        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Anti-Panel Corner", 0.75D));
        MicroBlockAPI.registerSubBlock(new SimpleCornerMicroBlock("Anti-Cover Corner", 0.875D));

        MicroBlockAPI.registerSubBlock(new MicroBlockCentralWire("Central Wire"));

        int[] blocks = new int[] { 1, 3, 4, 8, 12, 13, 19, 22, 41, 42, 45, 48, 49, 57, 79, 80, 82, 87, 88, 89, 98, 112, 121, 133, 152 };

        for (int block : blocks) {
            MicroBlockAPI.registerMaterial(new SimpleMicroBlockMaterial(new ItemStack(block, 1, 0)));
        }

        for (int meta = 0; meta < 4; meta++) {
            MicroBlockAPI.registerMaterial(new SimpleMicroBlockMaterial(new ItemStack(5, 1, meta)));
        }
        for (int meta = 0; meta < 4; meta++) {
            MicroBlockAPI.registerMaterial(new SimpleMicroBlockMaterial(new ItemStack(17, 1, meta)));
        }
        for (int meta = 0; meta < 16; meta++) {
            MicroBlockAPI.registerMaterial(new SimpleMicroBlockMaterial(new ItemStack(35, 1, meta)));
        }

        MicroBlockAPI.registerMaterial(new WireMaterial("WireRedstone", "Redstone Wire", Objects.redstoneOffIcon, Objects.redstoneOnIcon));
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

    public static void resendTileData(TileEntity tile) {
        if (!(tile.worldObj instanceof WorldServer)) {
            return;
        }

        WorldServer world = (WorldServer) tile.worldObj;

        world.getPlayerManager().flagChunkForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public void registerLanguage() {
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.0.name", "Crafting Chest");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.1.name", "Inductionish Furnace");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.2.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.3.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.4.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.5.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.6.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.7.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.8.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.9.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.10.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.11.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.12.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.13.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.14.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti1.15.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.0.name", "Item Stand");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.1.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.2.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.3.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.4.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.5.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.6.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.7.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.8.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.9.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.10.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.11.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.12.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.13.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.14.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("tile.SSMulti2.15.name", "Unknown Block");
        LanguageRegistry.instance().addStringLocalization("item.SSRotator.name", "Block Rotator");
        LanguageRegistry.instance().addStringLocalization("container.SSCraftingChest", "Crafting Chest");
        LanguageRegistry.instance().addStringLocalization("container.SSInductionishFurnace", "Inductionish Furnace");
        LanguageRegistry.instance().addStringLocalization("container.SSItemStand", "Item Stand");
        LanguageRegistry.instance().addStringLocalization("itemGroup.Smartestone", "Smartestone");
    }

}
