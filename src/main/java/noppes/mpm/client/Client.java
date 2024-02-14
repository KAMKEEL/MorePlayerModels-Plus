package noppes.mpm.client;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPacketServer;

import java.io.IOException;

public class Client {

	public static void sendData(EnumPacketServer enu, Object... obs) {
		ByteBuf buffer = Unpooled.buffer();
		try {
			if(!Server.fillBuffer(buffer, enu, obs))
				return;
			MorePlayerModels.Channel.sendToServer(new FMLProxyPacket(buffer, "MorePlayerModels"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
