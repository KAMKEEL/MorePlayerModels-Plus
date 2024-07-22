package noppes.mpm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import kamkeel.MorePlayerModelsPermissions;
import kamkeel.command.CommandMPM;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import noppes.mpm.commands.*;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.config.LoadConfiguration;
import noppes.mpm.config.legacy.LegacyConfig;
import noppes.mpm.controllers.ModelDataController;
import noppes.mpm.controllers.PermissionController;

import java.io.File;

import static noppes.mpm.MorePlayerModels.VERSION;

@Mod(modid = "moreplayermodels", name = "MorePlayerModels+", version = VERSION)
public class MorePlayerModels {

	@SidedProxy(clientSide = "noppes.mpm.client.ClientProxy", serverSide = "noppes.mpm.CommonProxy")
	public static CommonProxy proxy;
	public final static String VERSION = "4.2";

	public static FMLEventChannel Channel;
	public static MorePlayerModels instance;
	public static int Revision = 7;
	public static File dir;
	public static File presetDir;
	
	public static boolean HasServerSide = false;

	public static String configPath;
	public static String legacyPath;
	public static boolean legacyExist;

	public static LegacyConfig legacyConfig;

	public MorePlayerModels(){
		instance = this;
	}

	@EventHandler
	public void load(FMLPreInitializationEvent ev) {
		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("MorePlayerModels");

		MinecraftServer server = MinecraftServer.getServer();
		String dir = "";
		if (server != null)
			dir = new File(".").getAbsolutePath();
		else
			dir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();

		MorePlayerModels.dir = new File(dir,"moreplayermodels");
		if(!MorePlayerModels.dir.exists())
			MorePlayerModels.dir.mkdir();

		MorePlayerModels.presetDir = new File(MorePlayerModels.dir,"presets");
		if(!MorePlayerModels.presetDir.exists())
			MorePlayerModels.presetDir.mkdir();

		configPath = ev.getModConfigurationDirectory() + File.separator + "MorePlayerModelsPlus";
		legacyPath = ev.getModConfigurationDirectory() + "/MorePlayerModels.cfg";

		File configDir = new File(configPath);
		if(!configDir.exists()){
			// Convert Legacy Config to New Config if NO Config Folder Exists
			File legacyFile = new File(legacyPath);
			if(legacyFile.exists()){
				System.out.println("Loading Legacy Config");
				legacyExist = true;
				legacyConfig = new LegacyConfig();
				legacyConfig.init();
			}
		}
		configPath += File.separator;
		LoadConfiguration.init(configPath);

		if(Loader.isModLoaded("Morph"))
			ConfigClient.EnablePOV = false;
				
		proxy.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());

		new MorePlayerModelsPermissions();
	}

	@EventHandler
	public void setAboutToStart(FMLServerAboutToStartEvent event) {
		new ModelDataController();
		new PermissionController();
		PermissionController.Instance.reloadPermissionData();
		ModelDataController.Instance.clearCache();
	}

	@EventHandler
	public void serverstart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandLove());
		event.registerServerCommand(new CommandSing());
		event.registerServerCommand(new CommandDeath());
		event.registerServerCommand(new CommandYes());
		event.registerServerCommand(new CommandNo());
		event.registerServerCommand(new CommandPoint());
		event.registerServerCommand(new CommandSleep());
		event.registerServerCommand(new CommandHug());
		event.registerServerCommand(new CommandCrawl());
		event.registerServerCommand(new CommandSit());
		event.registerServerCommand(new CommandDance());
		event.registerServerCommand(new CommandWave());
		event.registerServerCommand(new CommandWag());
		event.registerServerCommand(new CommandBow());
		event.registerServerCommand(new CommandCry());
		event.registerServerCommand(new CommandAngry());
		event.registerServerCommand(new CommandSetUrl());
		event.registerServerCommand(new CommandSetCloak());
		event.registerServerCommand(new CommandScale());
		event.registerServerCommand(new CommandSetModel());
		event.registerServerCommand(new CommandSetName());
		event.registerServerCommand(new CommandMPM());
		event.registerServerCommand(new CommandSize());
		event.registerServerCommand(new CommandReload());

		GameRules rules = event.getServer().worldServerForDimension(0).getGameRules();
		if(!rules.hasRule("mpmAllowEntityModels"))
			rules.addGameRule("mpmAllowEntityModels", "true");
	}
}
