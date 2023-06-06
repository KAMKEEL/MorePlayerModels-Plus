package noppes.mpm;

import java.io.File;

import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import noppes.mpm.commands.CommandAngry;
import noppes.mpm.commands.CommandBow;
import noppes.mpm.commands.CommandCrawl;
import noppes.mpm.commands.CommandCry;
import noppes.mpm.commands.CommandDance;
import noppes.mpm.commands.CommandHug;
import noppes.mpm.commands.CommandLove;
import noppes.mpm.commands.CommandScale;
import noppes.mpm.commands.CommandSetModel;
import noppes.mpm.commands.CommandSetName;
import noppes.mpm.commands.CommandSetUrl;
import noppes.mpm.commands.CommandSing;
import noppes.mpm.commands.CommandSit;
import noppes.mpm.commands.CommandSleep;
import noppes.mpm.commands.CommandWag;
import noppes.mpm.commands.CommandWave;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.config.LoadConfiguration;
import noppes.mpm.config.legacy.LegacyConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import noppes.mpm.controllers.PermissionController;

@Mod(modid = "moreplayermodels", name = "MorePlayerModels+", version = "3.0")
public class MorePlayerModels {

	@SidedProxy(clientSide = "noppes.mpm.client.ClientProxy", serverSide = "noppes.mpm.CommonProxy")
	public static CommonProxy proxy;
	
	public static FMLEventChannel Channel;

	public static MorePlayerModels instance;

	public static int Revision = 5;
	
	public static File dir;
	
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
		
		new PlayerDataController(MorePlayerModels.dir);

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
		new PermissionController();
		PermissionController.Instance.reloadPermissionData();
	}

	@EventHandler
	public void serverstart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandLove());
		event.registerServerCommand(new CommandSing());
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
		event.registerServerCommand(new CommandScale());
		event.registerServerCommand(new CommandSetModel());
		event.registerServerCommand(new CommandSetName());

		GameRules rules = event.getServer().worldServerForDimension(0).getGameRules();
		if(!rules.hasRule("mpmAllowEntityModels"))
			rules.addGameRule("mpmAllowEntityModels", "true");
	}
}
