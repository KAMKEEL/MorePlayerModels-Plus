package noppes.mpm.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.*;
import noppes.mpm.client.gui.GuiCreationScreenInterface;
import noppes.mpm.constants.EnumPackets;
import noppes.mpm.controllers.PermissionController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PacketHandlerClient extends PacketHandlerServer{

	@SubscribeEvent
	public void onPacketData(ClientCustomPacketEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ByteBuf buf = event.packet.payload();
		try {
			handlePacket(buf, player, EnumPackets.values()[buf.readInt()]);
		
		} catch (Exception e) {
			LogWriter.except(e);
		}
	}

	private void handlePacket(ByteBuf buffer, EntityPlayer player, EnumPackets type) throws IOException {
		if(type == EnumPackets.PING){
			int version = buffer.readInt();
			if(version == MorePlayerModels.Revision){
				MorePlayerModels.HasServerSide = true;
				GuiCreationScreenInterface.Message = "";
			}
			else if(version < MorePlayerModels.Revision){
				MorePlayerModels.HasServerSide = false;
				GuiCreationScreenInterface.Message = "message.lowerversion";
			}
			else if(version > MorePlayerModels.Revision){
				MorePlayerModels.HasServerSide = false;
				GuiCreationScreenInterface.Message = "message.higherversion";
			}
		}
		else if(type == EnumPackets.RELOAD_SKINS) {
			Minecraft mc = Minecraft.getMinecraft();
			List<EntityPlayer> players = mc.theWorld.playerEntities;
			for(EntityPlayer p : players){
				ModelData data = PlayerDataController.instance.getPlayerData(p);
				data.resourceLoaded = false;
				data.resourceInit = false;
				data.cloakInnit = false;
				data.cloakLoaded = false;
			}
		}
		else if(type == EnumPackets.SEND_PLAYER_DATA){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			ModelData data = PlayerDataController.instance.getPlayerData(pl);
			NBTTagCompound compound = Server.readNBT(buffer);
			data.readFromNBT(compound);
			PlayerDataController.instance.savePlayerData(pl, data);
			if(pl == Minecraft.getMinecraft().thePlayer){
				data.lastEdited = System.currentTimeMillis();
			}
		}
		else if(type == EnumPackets.CHAT_EVENT){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			String message = Server.readString(buffer);
			ChatMessages.getChatMessages(pl.getCommandSenderName()).addMessage(message);
		}
		else if(type == EnumPackets.BACK_ITEM_REMOVE){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			ModelData data = PlayerDataController.instance.getPlayerData(pl);
			data.backItem = null;
		}
		else if(type == EnumPackets.BACK_ITEM_UPDATE){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			NBTTagCompound compound = Server.readNBT(buffer);
			ItemStack item = ItemStack.loadItemStackFromNBT(compound);
			ModelData data = PlayerDataController.instance.getPlayerData(pl);
			data.backItem = item;
		}
		else if(type == EnumPackets.RECEIVE_PERMISSION){
			NBTTagCompound compound = Server.readNBT(buffer);
			ClientCacheHandler.clientPerms = PermissionController.readNBT(compound);
		}
		else if(type == EnumPackets.PARTICLE){
			int animation = buffer.readInt();
			if(animation == 0){
				EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
				if(pl == null)
					return;
				ModelData data = PlayerDataController.instance.getPlayerData(pl);
				data.inLove = 40;
			}
			else if(animation == 1){
				player.worldObj.spawnParticle("note", buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), 0, 0);
			}
			else if(animation == 2){
				EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
				if(pl == null)
					return;
				ModelData data = PlayerDataController.instance.getPlayerData(pl);
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
		else if(type == EnumPackets.ANIMATION){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			ModelData data = PlayerDataController.instance.getPlayerData(pl);
			data.setAnimation(buffer.readInt());
			data.animationStart = pl.ticksExisted;
		}
		
	}

}
