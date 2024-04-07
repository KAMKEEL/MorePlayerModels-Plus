package noppes.mpm.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.mpm.ModelData;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPacketClient;

public class CommandSleep extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "sleep";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(!(icommandsender instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		ModelData data = ModelData.getData(player);

		float rotation = player.rotationYaw;
		while(rotation < 0)
			rotation += 360;
		while(rotation > 360)
			rotation -= 360;

		int rotate = (int) ((rotation + 45) / 90);
		EnumAnimation ani = EnumAnimation.SLEEPING_SOUTH;
		if(rotate == 1)
			ani = EnumAnimation.SLEEPING_WEST;
		if(rotate == 2)
			ani = EnumAnimation.SLEEPING_NORTH;
		if(rotate == 3)
			ani = EnumAnimation.SLEEPING_EAST;

		if(data.animation == EnumAnimation.SLEEPING_EAST || data.animation == EnumAnimation.SLEEPING_NORTH || data.animation == EnumAnimation.SLEEPING_WEST || data.animation == EnumAnimation.SLEEPING_SOUTH)
			ani = EnumAnimation.NONE;

		Server.sendAssociatedData(player, EnumPacketClient.PLAY_ANIMATION, player.getCommandSenderName(), ani);
		data.animation = ani;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/sleep to lie down";
	}

}
