package noppes.mpm;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.mpm.client.AnalyticsTracking;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ServerTickHandler {

	private String serverName = null;
	@SubscribeEvent
	public void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
		if(serverName == null){
			String e = "local";
			MinecraftServer server = MinecraftServer.getServer();
			if(server.isDedicatedServer()){
				try {
					e = InetAddress.getByName(server.getServerHostname()).getCanonicalHostName();
				} catch (UnknownHostException e1) {
					e = MinecraftServer.getServer().getServerHostname();
				}
				if(server.getPort() != 25565)
					e += ":" + server.getPort();
			}
			if(e == null || e.startsWith("192.168") || e.contains("127.0.0.1") || e.startsWith("localhost"))
				e = "local";
			serverName = e;
		}
		AnalyticsTracking.sendData(event.player, "join", serverName);
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		if(event.side == Side.CLIENT) {
			if (event.player.ticksExisted == 20) {
				reloadClientSkins();
			}
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		ModelData data = PlayerDataController.instance.getPlayerData(player);
		ItemStack item = player.inventory.mainInventory[0];
		if(data.backItem != item){
			if(item.getItem() == null){
				Server.sendAssociatedData(player, EnumPackets.BACK_ITEM_REMOVE, player.getUniqueID().toString());
			}
			else{
				NBTTagCompound tag = item.writeToNBT(new NBTTagCompound());
				Server.sendAssociatedData(player, EnumPackets.BACK_ITEM_UPDATE, player.getUniqueID().toString(), tag);
			}
			data.backItem = item;
		}
		if(data.animation != EnumAnimation.NONE)
			checkAnimation(player, data);
		data.prevPosX = player.posX;
		data.prevPosY = player.posY;
		data.prevPosZ = player.posZ;
	}

	@SideOnly(Side.CLIENT)
	public void reloadClientSkins() {
		Minecraft mc = Minecraft.getMinecraft();
		List<EntityPlayer> players = mc.theWorld.playerEntities;
		for(EntityPlayer p : players){
			ModelData data = PlayerDataController.instance.getPlayerData(p);
			data.resourceInit = false;
			data.resourceLoaded = false;
			data.cloakInnit = false;
			data.cloakLoaded = false;
		}
	}

	public static void checkAnimation(EntityPlayer player, ModelData data){
		if(data.prevPosY <= 0 || player.ticksExisted < 40)
			return;
		double motionX = data.prevPosX - player.posX;
		double motionY = data.prevPosY - player.posY;
		double motionZ = data.prevPosZ - player.posZ;

		double speed = motionX * motionX +  motionZ * motionZ;
		boolean isJumping = motionY * motionY > 0.08;

		if(data.animationTime > 0)
			data.animationTime--;

		if(player.isPlayerSleeping() || player.isRiding() || data.animationTime == 0  || data.animation == EnumAnimation.BOW && player.isSneaking())
			data.setAnimation(EnumAnimation.NONE);

		if(!isJumping && player.isSneaking() && (data.animation == EnumAnimation.HUG || data.animation == EnumAnimation.CRAWLING ||
				data.animation == EnumAnimation.SITTING || data.animation == EnumAnimation.DANCING))
			return;
		if(speed > 0.01 || isJumping || player.isPlayerSleeping() || player.isRiding() || data.isSleeping() && speed > 0.001)
			data.animation = EnumAnimation.NONE;
	}
}
