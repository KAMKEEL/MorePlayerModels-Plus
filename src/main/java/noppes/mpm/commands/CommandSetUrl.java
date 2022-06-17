package noppes.mpm.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPackets;

public class CommandSetUrl extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "seturl";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(var2.length < 1){
			icommandsender.addChatMessage(new ChatComponentTranslation("Not enough arguments given"));
			return;
		}
		EntityPlayer player = null;
		int i = 0;
		if(var2.length > 1 && isPlayerOp(icommandsender)){
			try {
				player = getPlayer(icommandsender, var2[0]);
				i = 1;
			} catch (PlayerNotFoundException e) {
				
			}
		}
		if(player == null && icommandsender instanceof EntityPlayer)
			player = (EntityPlayer) icommandsender;

		if(player == null)
			throw new PlayerNotFoundException();
		
		String url = var2[i];
		i++;
		for(; i < var2.length; i++){
			url += " " + var2[i];
		}
		if(url.equalsIgnoreCase("clear"))
			url = "";

		ModelData data = PlayerDataController.instance.getPlayerData(player);
		data.url = url;
		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getCommandSenderName(), data.writeToNBT());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/seturl [@p] url (to go back to default /seturl clear)";
	}

	@Override
	public boolean opsOnly(){
		return true;
	}

    @Override
	public List addTabCompletionOptions(ICommandSender par1, String[] args) {
    	if(args.length == 1)
    		return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    	return super.addTabCompletionOptions(par1, args);
    }
}
