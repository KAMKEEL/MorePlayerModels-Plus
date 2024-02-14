package noppes.mpm.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import noppes.mpm.ModelData;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPacketClient;
import noppes.mpm.controllers.ModelDataController;

import java.util.List;

public class CommandSize extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "setsize";
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

		int val = 5;
		try {
			val = Integer.parseInt(var2[i]);
		} catch (Exception ignored){}

		if(val > 10 || val <= 0)
			val = 5;

		ModelData data = ModelDataController.Instance.getModelData(player);
		data.size = val;
		Server.sendAssociatedData(player, EnumPacketClient.SEND_PLAYER_DATA, player.getCommandSenderName(), data.getNBT());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/setcloak [@p] url (to go back to default /setcloak clear)";
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