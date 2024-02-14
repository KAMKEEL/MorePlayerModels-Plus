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

import java.util.Arrays;
import java.util.List;

public class CommandScale extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "scale";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		if(var2.length < 1){
			icommandsender.addChatMessage(new ChatComponentTranslation("Not enough arguments given"));
			return;
		}
		EntityPlayer player = null;

		if(var2.length > 1 && isPlayerOp(icommandsender)){
			try {
				player = getPlayer(icommandsender, var2[0]);
				var2 = Arrays.copyOfRange(var2, 1, var2.length);
			} catch (PlayerNotFoundException e) {
				
			}
		}
		if(player == null && icommandsender instanceof EntityPlayer)
			player = (EntityPlayer) icommandsender;

		if(player == null)
			throw new PlayerNotFoundException();

		ModelData data = ModelDataController.Instance.getModelData(player);
		try{
			if(var2.length == 1){
				Scale scale = Scale.Parse(var2[0]);
				data.head.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				data.body.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				data.arms.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				data.legs.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
			}
			else if(var2.length == 4){
				Scale scale = Scale.Parse(var2[0]);
				data.head.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				scale = Scale.Parse(var2[1]);
				data.body.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);

				scale = Scale.Parse(var2[2]);
				data.arms.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);

				scale = Scale.Parse(var2[3]);
				data.legs.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
			}
			else{
				icommandsender.addChatMessage(new ChatComponentTranslation("Not enough arguments given"));
				return;
			}
			Server.sendAssociatedData(player, EnumPacketClient.SEND_PLAYER_DATA, player.getCommandSenderName(), data.getNBT());
		}
		catch(NumberFormatException ex){
			icommandsender.addChatMessage(new ChatComponentTranslation("None number given"));
		}
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/scale [@p] [head x,y,z] [body x,y,z] [arms x,y,z] [legs x,y,z]. Examples: /scale @p 1, /scale @p 1 1 1 1, /scale 1,1,1 1,1,1 1,1,1 1,1,1";
	}
	static class Scale{
		float scaleX, scaleY, scaleZ;
		
		private static Scale Parse(String s) throws NumberFormatException{
			Scale scale = new Scale();
			if(s.contains(",")){
				String[] split = s.split(",");
				if(split.length != 3)
					throw new NumberFormatException("Not enough args given");
				scale.scaleX = Float.parseFloat(split[0]);
				scale.scaleY = Float.parseFloat(split[1]);
				scale.scaleZ = Float.parseFloat(split[2]);
			}
			else{
				scale.scaleZ = scale.scaleY = scale.scaleX = Float.parseFloat(s);
			}
			return scale;
		}
	}

    @Override
	public List addTabCompletionOptions(ICommandSender par1, String[] args) {
    	if(args.length == 1)
    		return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    	return super.addTabCompletionOptions(par1, args);
    }

	@Override
	public boolean opsOnly(){
		return true;
	}
}
