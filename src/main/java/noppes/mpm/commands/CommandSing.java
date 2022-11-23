package noppes.mpm.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPackets;

public class CommandSing extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "sing";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(!(icommandsender instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		
		int note = player.getRNG().nextInt(25);
		if(var2.length > 0){
			try{
				int n = Integer.parseInt(var2[0]);
				if(n >= 0 && n < 25)
					note = n;
			}
			catch(NumberFormatException ex){}
		}
        float var7 = (float)Math.pow(2.0D, (double)(note - 12) / 12.0D);
        player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "note.harp", 3.0F, var7);

		Server.sendAssociatedData(player, EnumPackets.PARTICLE, 1, player.posX, player.posY + 2, player.posZ, note / 24d);

	
	}
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/sing [0-24] to sing";
	}

}
