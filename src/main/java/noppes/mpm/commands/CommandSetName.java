package noppes.mpm.commands;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPackets;

public class CommandSetName extends MpmCommandInterface {
	
	@Override
	public String getCommandName() {
		return "setname";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(var2.length < 1){
			icommandsender.addChatMessage(new ChatComponentTranslation("Not enough arguments given"));
			return;
		}
		
		EntityPlayer player = null;

		if(var2.length > 1 && isPlayerOp(icommandsender)){
			try{
				player = getPlayer(icommandsender, var2[0]);
				var2 = Arrays.copyOfRange(var2, 1, var2.length);
			}
			catch(PlayerNotFoundException ex){
				
			}
		}
		
		if(player == null && icommandsender instanceof EntityPlayer)
			player = (EntityPlayer) icommandsender;

		
		if(player == null)
			throw new PlayerNotFoundException();
		
		ModelData data = PlayerDataController.instance.getPlayerData(player);
		data.displayName = var2[0];
		for(int i = 1; i < var2.length; i++)
			data.displayName += " " + var2[i];
		
		if(data.displayName.equalsIgnoreCase("clear"))
			data.displayName = "";
		player.refreshDisplayName();
		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getCommandSenderName(), data.writeToNBT());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/setname [@p] name";
	}
	

    @Override
	public List addTabCompletionOptions(ICommandSender par1, String[] args) {
    	if(args.length == 1)
    		return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    	return super.addTabCompletionOptions(par1, args);
    }

}
