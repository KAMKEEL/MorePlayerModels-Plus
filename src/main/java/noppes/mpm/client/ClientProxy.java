package noppes.mpm.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import noppes.mpm.CommonProxy;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.config.ConfigMain;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy{

	public static KeyBinding Screen;
	public static KeyBinding Sleep;
	public static KeyBinding Sit;
	public static KeyBinding Dance;
	public static KeyBinding Hug;
	public static KeyBinding Crawl;

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load() {
		MorePlayerModels.Channel.register(new PacketHandlerClient());
		new PresetController(MorePlayerModels.dir);

		ClientRegistry.registerKeyBinding(Screen = new KeyBinding("CharacterScreen", Keyboard.KEY_F12, "key.categories.gameplay"));
		ClientRegistry.registerKeyBinding(Sleep = new KeyBinding("MPM 1",Keyboard.KEY_Z, "key.categories.gameplay"));
		ClientRegistry.registerKeyBinding(Sit = new KeyBinding("MPM 2",Keyboard.KEY_X, "key.categories.gameplay"));
		ClientRegistry.registerKeyBinding(Dance = new KeyBinding("MPM 3",Keyboard.KEY_C, "key.categories.gameplay"));
		ClientRegistry.registerKeyBinding(Hug = new KeyBinding("MPM 4",Keyboard.KEY_V, "key.categories.gameplay"));
		ClientRegistry.registerKeyBinding(Crawl = new KeyBinding("MPM 5",Keyboard.KEY_B, "key.categories.gameplay"));

		
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, RenderEvent.renderer);
		//RenderingRegistry.registerEntityRenderingHandler(EntityPlayerTemp.class, RenderEvent.renderer);
		
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
		
		if(ConfigMain.EnableUpdateChecker){
			VersionChecker checker = new VersionChecker();
			checker.start();
		}
	}

	public static void bindTexture(ResourceLocation location) {
		try{
			if(location == null)
				return;
			TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
			if(location != null)
				texturemanager.bindTexture((ResourceLocation) location);
		}
		catch(NullPointerException ex){

		}
		catch(ReportedException ex){

		}
	}
}
