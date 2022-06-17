package noppes.mpm.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.LogWriter;
import noppes.mpm.ModelData;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.PacketHandlerServer;
import noppes.mpm.PlayerDataController;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPackets;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;

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
			MorePlayerModels.HasServerSide = true;
		}
		else if(type == EnumPackets.SEND_PLAYER_DATA){
			String username = Server.readString(buffer);
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(username);
			if(pl == null)
				return;
			ModelData data = PlayerDataController.instance.getPlayerData(pl);
			NBTTagCompound compound = Server.readNBT(buffer);
			data.readFromNBT(compound);
			PlayerDataController.instance.savePlayerData(pl, data);
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
