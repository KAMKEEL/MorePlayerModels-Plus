package noppes.mpm.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPacketClient;

public class CommandLove extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "love";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(!(icommandsender instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		Server.sendAssociatedData(player, EnumPacketClient.PARTICLE, 0, player.getCommandSenderName());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/love to show your love";
	}
}
