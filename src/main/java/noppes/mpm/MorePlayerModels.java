package noppes.mpm;

import java.io.File;

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
import noppes.mpm.config.ConfigLoader;
import noppes.mpm.config.ConfigProp;
import noppes.mpm.constants.EnumAnimation;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "moreplayermodels", name = "MorePlayerModels", version = "1.2")
public class MorePlayerModels {
	@ConfigProp
	public static int Tooltips = 2;

	@SidedProxy(clientSide = "noppes.mpm.client.ClientProxy", serverSide = "noppes.mpm.CommonProxy")
	public static CommonProxy proxy;
	
	public static FMLEventChannel Channel;

	public static MorePlayerModels instance;
	
	public static int Revision = 5;
	
	public File dir;
	
	public static boolean HasServerSide = false;
	
	@ConfigProp(info="Enable different perspective heights for different model sizes") 
	public static boolean EnablePOV = true;

	@ConfigProp(info="Enables the item on your back") 
	public static boolean EnableBackItem = true;

	@ConfigProp(info="Enables chat bubbles")
	public static boolean EnableChatBubbles = true;

	@ConfigProp(info="Enables MorePlayerModels startup update message")
	public static boolean EnableUpdateChecker = true;

	@ConfigProp(info="Used to register buttons to animations")
	public static int button1 = EnumAnimation.SLEEPING_SOUTH.ordinal();
	@ConfigProp(info="Used to register buttons to animations")
	public static int button2 = EnumAnimation.SITTING.ordinal();
	@ConfigProp(info="Used to register buttons to animations")
	public static int button3 = EnumAnimation.CRAWLING.ordinal();
	@ConfigProp(info="Used to register buttons to animations")
	public static int button4 = EnumAnimation.HUG.ordinal();
	@ConfigProp(info="Used to register buttons to animations")
	public static int button5 = EnumAnimation.DANCING.ordinal();
	
	public ConfigLoader configLoader;
	
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
		
		this.dir = new File(dir,"moreplayermodels");
		if(!this.dir.exists())
			this.dir.mkdir();
		
		new PlayerDataController(this.dir);

		configLoader = new ConfigLoader(this.getClass(), new File(dir, "config"), "MorePlayerModels");
		configLoader.loadConfig();

		if(Loader.isModLoaded("Morph"))
			EnablePOV = false;
				
		proxy.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		
		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
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
