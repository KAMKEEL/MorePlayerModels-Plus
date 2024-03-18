package noppes.mpm.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPacketClient;

public class CommandAngry extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "angry";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(!(icommandsender instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		Server.sendAssociatedData(player, EnumPacketClient.PARTICLE, 2, player.getCommandSenderName());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/angry to show your angry";
	}

}
