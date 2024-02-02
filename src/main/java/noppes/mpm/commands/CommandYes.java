package noppes.mpm.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPackets;

public class CommandYes extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "yes";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(!(icommandsender instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		ModelData data = PlayerDataController.instance.getPlayerData(player);
		EnumAnimation ani = data.animation == EnumAnimation.YES?EnumAnimation.NONE:EnumAnimation.YES;
		Server.sendAssociatedData(player, EnumPackets.ANIMATION, player.getUniqueID().toString(), ani);
		data.animation = ani;
		data.animationTime = player.ticksExisted;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/yes to nod head";
	}
}
