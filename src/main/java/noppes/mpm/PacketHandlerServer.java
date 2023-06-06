package noppes.mpm;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPackets;
import noppes.mpm.controllers.PermissionController;

import java.io.IOException;

public class PacketHandlerServer{

	@SubscribeEvent
	public void onPacketData(ServerCustomPacketEvent event) {
		EntityPlayerMP player = ((NetHandlerPlayServer)event.handler).playerEntity;
		ByteBuf buf = event.packet.payload();
		try {
			handlePacket(buf, (EntityPlayerMP) player, EnumPackets.values()[buf.readInt()]);
		
		} catch (Exception e) {
			System.err.println(player + e.getMessage());
		}
	}

	private void handlePacket(ByteBuf buffer, EntityPlayerMP player, EnumPackets type) throws IOException {
		if(type == EnumPackets.PING){
			int version = buffer.readInt();
			if(version == MorePlayerModels.Revision){
				ModelData data = PlayerDataController.instance.getPlayerData(player);
				data.readFromNBT(Server.readNBT(buffer));

				if(!player.worldObj.getGameRules().getGameRuleBooleanValue("mpmAllowEntityModels"))
					data.entityClass = null;

				data.save();
				Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID(), data.writeToNBT());
			}
			ItemStack back = player.inventory.mainInventory[0];
			if(back != null)
				Server.sendAssociatedData(player, EnumPackets.BACK_ITEM_UPDATE, player.getUniqueID(), back.writeToNBT(new NBTTagCompound()));

			Server.sendData(player, EnumPackets.PING, MorePlayerModels.Revision);
		}
		else if(type == EnumPackets.REQUEST_PLAYER_DATA){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			String hash = Server.readString(buffer);
			ModelData data = PlayerDataController.instance.getPlayerData(pl);
			if(!hash.equals(data.getHash()))
				Server.sendData(player, EnumPackets.SEND_PLAYER_DATA, pl.getCommandSenderName(), data.writeToNBT());

			ItemStack back = pl.inventory.mainInventory[0];
			if(back != null)
				Server.sendData(player, EnumPackets.BACK_ITEM_UPDATE, pl.getCommandSenderName(), back.writeToNBT(new NBTTagCompound()));
			else
				Server.sendData(player, EnumPackets.BACK_ITEM_REMOVE, pl.getCommandSenderName());
		}
		else if(type == EnumPackets.UPDATE_PLAYER_DATA){
			ModelData data = PlayerDataController.instance.getPlayerData(player);
			data.readFromNBT(Server.readNBT(buffer));
			
			if(!player.worldObj.getGameRules().getGameRuleBooleanValue("mpmAllowEntityModels"))
				data.entityClass = null;
			
			PlayerDataController.instance.savePlayerData(player, data);
			Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getCommandSenderName(), data.writeToNBT());
		}
		else if(type == EnumPackets.GET_PERMISSION){
			long lastRequest = System.currentTimeMillis();
			String uuid = player.getUniqueID().toString();
			if(PermissionController.Instance.lastRequest.containsKey(uuid)){
				lastRequest = PermissionController.Instance.lastRequest.get(uuid);
			}

			if(System.currentTimeMillis() - lastRequest > 30 * 60 * 1000){
				NBTTagCompound nbtTagCompound =  PermissionController.Instance.writeNBT(player);
				PermissionController.Instance.lastRequest.put(uuid, System.currentTimeMillis());
				Server.sendData(player, EnumPackets.RECEIVE_PERMISSION, nbtTagCompound);
			}
		}
		else if(type == EnumPackets.ANIMATION){

			EnumAnimation animation = EnumAnimation.values()[buffer.readInt()];
			if(animation == EnumAnimation.SLEEPING_SOUTH){
				float rotation = player.rotationYaw;
				while(rotation < 0)
					rotation += 360;
				while(rotation > 360)
					rotation -= 360;
				int rotate = (int) ((rotation + 45) / 90);
				if(rotate == 1)
					animation = EnumAnimation.SLEEPING_WEST;
				if(rotate == 2)
					animation = EnumAnimation.SLEEPING_NORTH;
				if(rotate == 3)
					animation = EnumAnimation.SLEEPING_EAST;
			}
			ModelData data = PlayerDataController.instance.getPlayerData(player);
			if(data.animationEquals(animation))
				animation = EnumAnimation.NONE;

			Server.sendAssociatedData(player, EnumPackets.ANIMATION, player.getCommandSenderName(), animation);
			data.animation = animation;
		}
	}
}
