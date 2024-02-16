package noppes.mpm.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.*;
import noppes.mpm.client.controller.ClientCacheController;
import noppes.mpm.client.controller.ClientDataController;
import noppes.mpm.constants.EnumPacketClient;

import java.io.IOException;
import java.util.List;

public class PacketHandlerClient extends PacketHandlerServer{

	@SubscribeEvent
	public void onPacketData(ClientCustomPacketEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ByteBuf buf = event.packet.payload();
		try {
			handlePacket(buf, player, EnumPacketClient.values()[buf.readInt()]);
		
		} catch (Exception e) {
			LogWriter.except(e);
		}
	}

	private void handlePacket(ByteBuf buffer, EntityPlayer player, EnumPacketClient type) throws IOException {
		if(type == EnumPacketClient.SERVER_PING){
			MorePlayerModels.HasServerSide = true;
		}
		else if(type == EnumPacketClient.LOGOUT){
			ClientCacheController.clearDataCache();
		}
		else if(type == EnumPacketClient.RELOAD_SKINS) {
			Minecraft mc = Minecraft.getMinecraft();
			List<EntityPlayer> players = mc.theWorld.playerEntities;
			for(EntityPlayer p : players){
				ModelData data = ClientDataController.Instance().getPlayerData(p);
				data.textureLocation = null;
			}
		}
		else if(type == EnumPacketClient.LOGIN){
			NBTTagCompound compound = Server.readNBT(buffer);
			ClientCacheController.createCache();
			ModelData playersData = ClientDataController.Instance().getPlayerData(player);
			playersData.player = player;
			playersData.setNBT(compound);
		}
		else if(type == EnumPacketClient.PERMISSION_RECEIVE){
			NBTTagCompound compound = Server.readNBT(buffer);
			ClientCacheController.createCache();
			ModelData playersData = ClientDataController.Instance().getPlayerData(player);
			playersData.player = player;
			playersData.setNBT(compound);
		}
		else if(type == EnumPacketClient.SEND_PLAYER_DATA){
			ModelData data = null;
			String playerName = Server.readString(buffer);
			EntityPlayer sendingPlayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(playerName);
			if (sendingPlayer != null) {
				data = ModelData.getData(sendingPlayer);
			}

			if (data == null) {
				return;
			}

			NBTTagCompound compound = Server.readNBT(buffer);
			data.setNBT(compound);
		}
		else if(type == EnumPacketClient.CHAT_EVENT){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			String message = Server.readString(buffer);
			ChatMessages.getChatMessages(pl.getCommandSenderName()).addMessage(message);
		}
		else if(type == EnumPacketClient.BACK_ITEM_REMOVE){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;

			ModelData data = ClientDataController.Instance().getPlayerData(pl);
			data.backItem = null;
		}
		else if(type == EnumPacketClient.BACK_ITEM_UPDATE){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			NBTTagCompound compound = Server.readNBT(buffer);
			ItemStack item = ItemStack.loadItemStackFromNBT(compound);
			ModelData data = ModelData.getData(pl);
			data.backItem = item;
		}
		else if(type == EnumPacketClient.PARTICLE){
			int animation = buffer.readInt();
			if(animation == 0){
				EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
				if(pl == null)
					return;
				ModelData data = ClientDataController.Instance().getPlayerData(pl);
				data.inLove = 40;
			}
			else if(animation == 1){
				player.worldObj.spawnParticle("note", buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), 0, 0);
			}
			else if(animation == 2){
				EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
				if(pl == null)
					return;

				ModelData data = ClientDataController.Instance().getPlayerData(pl);
		        for (int i = 0; i < 5; ++i){
		            double d0 = player.getRNG().nextGaussian() * 0.02D;
		            double d1 = player.getRNG().nextGaussian() * 0.02D;
		            double d2 = player.getRNG().nextGaussian() * 0.02D;
		            double x = player.posX + ((player.getRNG().nextFloat() - 0.5f) * player.width) * 2;
		            double z = player.posZ + ((player.getRNG().nextFloat() - 0.5f) * player.width) * 2;
		            player.worldObj.spawnParticle("angryVillager",x , player.posY + 0.8f + (double)(player.getRNG().nextFloat() * player.height / 2) - player.getYOffset() - data.getBodyY(), z, d0, d1, d2);
		        }
			}
		}
		else if(type == EnumPacketClient.PLAY_ANIMATION){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;

			ModelData data = ClientDataController.Instance().getPlayerData(pl);
			data.setAnimation(buffer.readInt());
			data.animationStart = pl.ticksExisted;
		}

	}

}
