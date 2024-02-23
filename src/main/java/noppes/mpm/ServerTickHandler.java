package noppes.mpm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.mpm.client.AnalyticsTracking;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPacketClient;
import noppes.mpm.controllers.ModelDataController;
import noppes.mpm.controllers.PermissionController;
import noppes.mpm.controllers.data.PermissionData;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ServerTickHandler {

	@SubscribeEvent
	public void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
		if(event.player == null || event.player.worldObj == null || event.player.worldObj.isRemote)
			return;

		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT)
			return;

		Server.sendData((EntityPlayerMP) event.player, EnumPacketClient.SERVER_PING);
		ModelData data = ModelData.getData(event.player);
		data.resourceInit = false;
		data.textureLocation = null;

		if(data != null){
			if(!event.player.worldObj.getGameRules().getGameRuleBooleanValue("mpmAllowEntityModels"))
				data.entityClass = null;

			ItemStack back = event.player.inventory.mainInventory[0];
			if(back != null)
				Server.sendAssociatedData(event.player, EnumPacketClient.BACK_ITEM_UPDATE, event.player.getCommandSenderName(), back.writeToNBT(new NBTTagCompound()));

			Server.sendData((EntityPlayerMP) event.player, EnumPacketClient.LOGIN, data.getNBT());
		}

		if(!PermissionController.Instance.hasPlayer(event.player.getUniqueID())){
			PermissionData permissionData = new PermissionData(event.player);
			permissionData.updatePermissions();
			PermissionController.Instance.addPlayer(event.player.getUniqueID(), permissionData);

			NBTTagCompound permissionCompound = PermissionController.Instance.writeNBT(event.player);
			Server.sendData((EntityPlayerMP) event.player, EnumPacketClient.PERMISSION_RECEIVE, permissionCompound);
		}
		else {
			PermissionData permissionData = PermissionController.Instance.getPermissionData(event.player.getUniqueID());
			if(permissionData.lastUpdated < PermissionData.reloadedTime){
				permissionData.updatePermissions();
				NBTTagCompound permissionCompound = PermissionController.Instance.writeNBT(event.player);
				Server.sendData((EntityPlayerMP) event.player, EnumPacketClient.PERMISSION_RECEIVE, permissionCompound);
			}
		}
	}

	@SubscribeEvent
	public void invoke(PlayerEvent.PlayerLoggedOutEvent event) {
		if(event.player == null || event.player.worldObj == null || event.player.worldObj.isRemote)
			return;

		PermissionController.Instance.removePlayer(event.player.getUniqueID());
		Server.sendData((EntityPlayerMP) event.player, EnumPacketClient.LOGOUT, MorePlayerModels.Revision);
	}

	@SubscribeEvent
	public void world(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event){
		ModelData.getData(event.entityPlayer); // Load Data if Null : ty louis
	}

	@SubscribeEvent
	public void world(net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile event){
		ModelData data = ModelData.getData(event.entityPlayer);
		if (data != null) {
			data.save();
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		if(event.side == Side.CLIENT)
			return;

		EntityPlayerMP player = (EntityPlayerMP) event.player;
		ModelData data = ModelData.getData(player);
		ItemStack item = player.inventory.mainInventory[0];
		if(data.backItem != item){
			if(item == null){
				Server.sendAssociatedData(player, EnumPacketClient.BACK_ITEM_REMOVE, player.getCommandSenderName());
			}
			else {
				NBTTagCompound tag = item.writeToNBT(new NBTTagCompound());
				Server.sendAssociatedData(player, EnumPacketClient.BACK_ITEM_UPDATE, player.getCommandSenderName(), tag);
			}
			data.backItem = item;
		}

		if(data.animation != EnumAnimation.NONE)
			checkAnimation(player, data);

		data.prevPosX = player.posX;
		data.prevPosY = player.posY;
		data.prevPosZ = player.posZ;
	}

	public static void checkAnimation(EntityPlayer player, ModelData data){
		if(data.prevPosY <= 0 || player.ticksExisted < 40)
			return;
		double motionX = data.prevPosX - player.posX;
		double motionY = data.prevPosY - player.posY;
		double motionZ = data.prevPosZ - player.posZ;

		double speed = motionX * motionX +  motionZ * motionZ;
		boolean isJumping = motionY * motionY > 0.08;

		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			System.out.println("Okay!");

		if(data.animationTime > 0)
			data.animationTime--;

		if(player.isPlayerSleeping() || player.isRiding() || data.animationTime == 0  || data.animation == EnumAnimation.BOW && player.isSneaking())
			data.setAnimation(EnumAnimation.NONE);

		if(!isJumping && player.isSneaking() && (data.animation == EnumAnimation.HUG || data.animation == EnumAnimation.CRAWLING ||
				data.animation == EnumAnimation.SITTING || data.animation == EnumAnimation.DANCING))
			return;
		if(speed > 0.01 || isJumping || player.isPlayerSleeping() || player.isRiding() || data.isSleeping() && speed > 0.001)
			data.setAnimation(EnumAnimation.NONE);
	}
}
