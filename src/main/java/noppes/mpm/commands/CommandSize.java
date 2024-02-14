package noppes.mpm.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPackets;

public class CommandSize extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "mpmsize";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(!(icommandsender instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		ModelData data = PlayerDataController.instance.getPlayerData(player);
		data.size = 5;
		if(var2.length > 0){
			data.size = Integer.parseInt(var2[0]);
		}

		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getCommandSenderName(), data.writeToNBT());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/mpmsize [size]";
	}
}

