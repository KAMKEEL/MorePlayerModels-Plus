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
import noppes.mpm.constants.EnumPacketClient;
import noppes.mpm.constants.EnumPacketServer;
import noppes.mpm.controllers.ModelDataController;

import java.io.IOException;

public class PacketHandlerServer{

	@SubscribeEvent
	public void onPacketData(ServerCustomPacketEvent event) {
		EntityPlayerMP player = ((NetHandlerPlayServer)event.handler).playerEntity;
		ByteBuf buf = event.packet.payload();
		try {
			handlePacket(buf, (EntityPlayerMP) player, EnumPacketServer.values()[buf.readInt()]);
		
		} catch (Exception e) {
			System.err.println(player + e.getMessage());
		}
	}

	private void handlePacket(ByteBuf buffer, EntityPlayerMP player, EnumPacketServer type) throws IOException {
		if(type == EnumPacketServer.UPDATE_PLAYER_DATA){
			ModelData data = ModelData.getData(player);
			data.setNBT(Server.readNBT(buffer));
			if(!player.worldObj.getGameRules().getGameRuleBooleanValue("mpmAllowEntityModels"))
				data.entityClass = null;

			data.save();
			Server.sendAssociatedData(player, EnumPacketClient.SEND_PLAYER_DATA, player.getCommandSenderName(), data.getNBT());
		}
		else if(type == EnumPacketServer.ANIMATION){

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
			ModelData data = ModelData.getData(player);
			if(data.animationEquals(animation))
				animation = EnumAnimation.NONE;

			Server.sendAssociatedData(player, EnumPacketClient.PLAY_ANIMATION, player.getCommandSenderName(), animation);
			data.setAnimation(animation);
		}
		else if(type == EnumPacketServer.REQUEST_PLAYER_DATA){
			EntityPlayer pl = player.worldObj.getPlayerEntityByName(Server.readString(buffer));
			if(pl == null)
				return;
			String hash = Server.readString(buffer);
			if(hash == null)
				return;

			ModelData data = ModelData.getData(player);
			if(data == null)
				return;

			if(!hash.equals(data.getHash()))
				Server.sendData(player, EnumPacketClient.SEND_PLAYER_DATA, pl.getCommandSenderName(), data.getNBT());
		}
	}
}
