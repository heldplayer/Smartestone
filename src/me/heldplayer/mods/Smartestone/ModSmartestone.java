
package me.heldplayer.mods.Smartestone;

import java.io.File;

import me.heldplayer.mods.Smartestone.util.Objects;
import me.heldplayer.util.HeldCore.Updater;
import me.heldplayer.util.HeldCore.UsageReporter;
import me.heldplayer.util.HeldCore.config.Config;
import me.heldplayer.util.HeldCore.config.ConfigValue;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;

@Mod(name = Objects.MOD_NAME, modid = Objects.MOD_ID, version = Objects.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { Objects.MOD_CHANNEL }, packetHandler = PacketHandler.class)
public class ModSmartestone {

    @Instance(value = Objects.MOD_ID)
    public static ModSmartestone instance;

    @SidedProxy(clientSide = Objects.CLIENT_PROXY, serverSide = Objects.SERVER_PROXY)
    public static CommonProxy proxy;

    // HeldCore Objects
    private UsageReporter reporter;
    private Config config;
    public static ConfigValue<Integer> blockMulti1Id;
    public static ConfigValue<Integer> blockMulti2Id;
    public static ConfigValue<Integer> blockMicroId;
    public static ConfigValue<Integer> itemRotatorId;
    public static ConfigValue<Integer> itemMicroBlockId;
    public static ConfigValue<Integer> itemWaterCoreId;
    public static ConfigValue<Boolean> HDTextures;
    // Config values for HeldCore
    public static ConfigValue<Boolean> silentUpdates;
    public static ConfigValue<Boolean> optOut;
    public static ConfigValue<String> modPack;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        File file = new File(event.getModConfigurationDirectory(), "HeldCore");

        if (!file.exists()) {
            file.mkdirs();
        }

        Objects.log = event.getModLog();

        // Config
        blockMulti1Id = new ConfigValue<Integer>("MultiBlock1", Configuration.CATEGORY_BLOCK, null, 2100, "");
        blockMulti2Id = new ConfigValue<Integer>("MultiBlock2", Configuration.CATEGORY_BLOCK, null, 2101, "");
        blockMicroId = new ConfigValue<Integer>("MicroId", Configuration.CATEGORY_BLOCK, null, 2102, "");
        itemRotatorId = new ConfigValue<Integer>("Rotator", Configuration.CATEGORY_ITEM, null, 5240, "");
        itemMicroBlockId = new ConfigValue<Integer>("MicroBlockItem", Configuration.CATEGORY_ITEM, null, 5241, "");
        itemWaterCoreId = new ConfigValue<Integer>("WaterCore", Configuration.CATEGORY_ITEM, null, 5242, "");
        HDTextures = new ConfigValue<Boolean>("HD-Textures", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.FALSE, "");
        silentUpdates = new ConfigValue<Boolean>("silentUpdates", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Set this to true to hide update messages in the main menu");
        optOut = new ConfigValue<Boolean>("optOut", Configuration.CATEGORY_GENERAL, null, Boolean.FALSE, "Set this to true to opt-out from statistics gathering. If you are configuring this mod for a modpack, please leave it set to false");
        modPack = new ConfigValue<String>("modPack", Configuration.CATEGORY_GENERAL, null, "", "If this mod is running in a modpack, please set this config value to the name of the modpack");
        config = new Config(event.getSuggestedConfigurationFile());
        this.config.addConfigKey(blockMulti1Id);
        this.config.addConfigKey(blockMulti2Id);
        this.config.addConfigKey(blockMicroId);
        this.config.addConfigKey(itemRotatorId);
        this.config.addConfigKey(itemMicroBlockId);
        this.config.addConfigKey(itemWaterCoreId);
        this.config.addConfigKey(HDTextures);
        this.config.addConfigKey(silentUpdates);
        this.config.addConfigKey(optOut);
        this.config.addConfigKey(modPack);
        this.config.load();
        this.config.saveOnChange();

        this.reporter = new UsageReporter(Objects.MOD_ID, Objects.MOD_VERSION, modPack.getValue(), FMLCommonHandler.instance().getSide(), file);

        Updater.initializeUpdater(Objects.MOD_ID, Objects.MOD_VERSION, silentUpdates.getValue());

        proxy.preInit(event);
    }

    @Init
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        if (optOut.getValue()) {
            Thread thread = new Thread(this.reporter, Objects.MOD_ID + " usage reporter");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        proxy.postInit(event);
    }

}
