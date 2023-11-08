package noppes.mpm.commands;

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

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandSetModel extends MpmCommandInterface {
	public HashMap<String,Class<? extends EntityLivingBase>> entities = new HashMap<String, Class<? extends EntityLivingBase>>();

	public CommandSetModel(){
        Map<?,?> mapping = EntityList.stringToClassMapping;
        for(Object name : mapping.keySet()){
        	Class<?> c = (Class<?>) mapping.get(name);
        	try {
        		if(EntityLiving.class.isAssignableFrom(c) && c.getConstructor(new Class[] {World.class}) != null && !Modifier.isAbstract(c.getModifiers())){
        			entities.put(name.toString().toLowerCase(), c.asSubclass(EntityLivingBase.class));
        		}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
			}
        } 
        entities.put("clear", null);
	}
	
	@Override
	public String getCommandName() {
		return "setmodel";
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
		
		String arg = var2[i].toLowerCase();
		if(!entities.containsKey(arg)){
			icommandsender.addChatMessage(new ChatComponentTranslation("Unknown entity: " + var2[i]));
			return;
		}

		ModelData data = PlayerDataController.instance.getPlayerData(player);
		data.setEntityClass(entities.get(arg));
		i++;
		if(var2.length > i){
			while(i < var2.length){
		    	EntityLivingBase entity = data.getEntity(player.worldObj, player);
		    	String[] split = var2[i].split(":");
		    	if(split.length == 2)
		    		data.setExtra(entity, split[0], split[1]);
		    	i++;
			}
		}
		
		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getCommandSenderName(), data.writeToNBT());
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/setmodel [@p] model (to go back to default /setmodel clear)";
	}

	@Override
	public boolean opsOnly(){
		return true;
	}

    @Override
	public List addTabCompletionOptions(ICommandSender par1, String[] args) {
    	if(args.length == 1)
    		return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    	if(args.length == 2){
    		return CommandBase.getListOfStringsMatchingLastWord(args, entities.keySet().toArray(new String[entities.size()]));
    	}
    	return super.addTabCompletionOptions(par1, args);
    }

}
