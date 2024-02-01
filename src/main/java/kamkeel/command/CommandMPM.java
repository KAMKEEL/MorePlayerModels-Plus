package kamkeel.command;

import net.minecraft.command.*;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.Server;
import noppes.mpm.commands.MpmCommandInterface;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPackets;

import java.lang.reflect.Modifier;
import java.util.*;

public class CommandMPM extends MpmCommandInterface {
	private HashMap<String,Class<? extends EntityLivingBase>> entities = new HashMap<String, Class<? extends EntityLivingBase>>();
	private List<String> sub = Arrays.asList("url", "name", "entity", "scale", "animation", "sendmodel");
		
	public CommandMPM(){
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
		return "mpm";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] args) throws CommandException {
		if(args.length < 1){
			throw new CommandException("Not enough arguments given" + "Add more");
		}
		String type = args[0].toLowerCase();
		if(!sub.contains(type)){
			throw new CommandException("Unknown subcommand" + type);
		}		
		args = Arrays.copyOfRange(args, 1, args.length);
		
		EntityPlayer player = null;
		if(args.length > 1 && isPlayerOp(icommandsender)){
			try {
				player = getPlayer(icommandsender, args[0]);
				args = Arrays.copyOfRange(args, 1, args.length);
			} catch (PlayerNotFoundException e) {
				
			}
		}
		if(player == null && icommandsender instanceof EntityPlayer)
			player = (EntityPlayer) icommandsender;
		
		if(player == null)
            throw new PlayerNotFoundException("commands.generic.player.notFound", icommandsender);

		ModelData data = PlayerDataController.instance.getPlayerData(player);
		
		if(type.equals("url")){
			url(player, args, data);
		}
		else if(type.equals("scale")){
			scale(player, args, data);
		}		
		else if(type.equals("name")){
			name(player, args, data);
		}	
		else if(type.equals("entity")){
			entity(player, args, data);
		}
		else if(type.equals("animation")){
			animation(player, args, data);
		}
		else if(type.equals("sendmodel")){
			sendmodel(player, args, data);
		}
	}
	
	private void animation(EntityPlayer player, String[] args, ModelData data) throws WrongUsageException {
		if(args.length <= 0)
			throw new WrongUsageException("/mpm animation [@p] <animation>");
		
		String type = args[0];
		EnumAnimation animation = null;

		for(EnumAnimation ani : EnumAnimation.values()){
			if(ani.name().equalsIgnoreCase(type)){
				animation = ani;
				break;
			}
		}
		
		if(animation == null){
			throw new WrongUsageException("Unknown animation " + type);
		}
		
		if(data.animation == animation){
			animation = EnumAnimation.NONE;
		}
		
		Server.sendAssociatedData(player, EnumPackets.ANIMATION, player.getUniqueID().toString(), animation);
		data.setAnimation(animation.ordinal());
	}
	
	private void entity(EntityPlayer player, String[] args, ModelData data) throws WrongUsageException {
		if(args.length <= 0)
			throw new WrongUsageException("/mpm entity [@p] <entity> (to go back to default /mpm entity [@p] clear)");
		
		String arg = args[0].toLowerCase();
		if(!entities.containsKey(arg)){
			throw new WrongUsageException("Unknown entity: " + args[0]);
		}
		data.setEntityClass(entities.get(arg));
		int i = 1;
		if(args.length > i){
			while(i < args.length){
		    	EntityLivingBase entity = data.getEntity(player);
		    	String[] split = args[i].split(":");
		    	if(split.length == 2)
		    		data.setExtra(entity, split[0], split[1]);
		    	i++;
			}
		}
		
		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID().toString(), data.writeToNBT());
	}
	
	private void name(EntityPlayer player, String[] args, ModelData data) throws WrongUsageException {
		if(args.length <= 0)
			throw new WrongUsageException("/mpm name [@p] <name>");
		if(args.length > 1 && isPlayerOp(player)){
			try{
				player = getPlayer(player, args[0]);
				args = Arrays.copyOfRange(args, 1, args.length);
			}
			catch(PlayerNotFoundException ex){

			}
		}
		data.displayName = args[0];
		for(int i = 1; i < args.length; i++)
			data.displayName += " " + args[i];

		data.displayName = data.displayName.replace('&', Character.toChars(167)[0]);
		if(data.displayName.equalsIgnoreCase("clear"))
			data.displayName = "";
		player.refreshDisplayName();
		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID().toString(), data.writeToNBT());
	}

	private void url(EntityPlayer player, String[] args, ModelData data) throws WrongUsageException{
		if(args.length <= 0)
			throw new WrongUsageException("/mpm url [@p] <url> (to go back to default /mpm url [@p] clear)");
		String url = args[0];
		for(int i = 1; i < args.length; i++){
			url += " " + args[i];
		}
		if(url.equalsIgnoreCase("clear"))
			url = "";
		data.url = url;
		Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID().toString(), data.writeToNBT());
	}
	
	private void sendmodel(EntityPlayer fromPlayer, String[] args, ModelData fromData) throws WrongUsageException{
		if(args.length < 1){
			throw new WrongUsageException("/mpm sendmodel [@from_player] <@to_player> (to go back to default /mpm sendmodel [@p] clear)");
		}
		
		EntityPlayer toPlayer = null;
		ModelData toData = null;

		try {
			toPlayer = getPlayer(fromPlayer, args[0]);
		} catch (CommandException e) {}
				
		if(toPlayer == null || toPlayer == fromPlayer){
			if(args[0].equalsIgnoreCase("clear")){
				fromData = new ModelData();
			}
			else
				throw new WrongUsageException("/mpm sendmodel [@from_player] <@to_player> (to go back to default /mpm sendmodel [@p] clear)");
		}
		else
			toData = PlayerDataController.instance.getPlayerData(toPlayer);

		if(toData == null){
			return;
		}

		NBTTagCompound compound = fromData.writeToNBT();
		toData.readFromNBT(compound);
		toData.save();
		Server.sendAssociatedData(toPlayer, EnumPackets.SEND_PLAYER_DATA, toPlayer.getUniqueID().toString(), compound);
	}
	
	private void scale(EntityPlayer player, String[] args, ModelData data) throws WrongUsageException{
		try{
			if(args.length == 1){
				Scale scale = Scale.Parse(args[0]);
				data.head.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				data.body.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				data.arms.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				data.legs.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID().toString(), data.writeToNBT());
			}
			else if(args.length == 4){
				Scale scale = Scale.Parse(args[0]);
				data.head.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				scale = Scale.Parse(args[1]);
				data.body.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);

				scale = Scale.Parse(args[2]);
				data.arms.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);

				scale = Scale.Parse(args[3]);
				data.legs.setScale(scale.scaleX, scale.scaleY, scale.scaleZ);
				Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID().toString(), data.writeToNBT());
			}
			else{
				throw new WrongUsageException("/mpm scale [@p] [head x,y,z] [body x,y,z] [arms x,y,z] [legs x,y,z]. Examples: /mpm scale @p 1, /mpm scale @p 1 1 1 1, /mpm scale 1,1,1 1,1,1 1,1,1 1,1,1");
			}
		}
		catch(NumberFormatException ex){
			throw new WrongUsageException("None number given");
		}
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/mpm <url/model/scale/name/animation> [@p]";
	}
	
	@Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

    @Override
	public List addTabCompletionOptions(ICommandSender par1, String[] args) {
    	if(args.length == 1)
			return CommandBase.getListOfStringsMatchingLastWord(args, sub.toArray(new String[sub.size()]));
    	if(args.length >= 2){
    		String type = args[0].toLowerCase();
    		List<String> list = new ArrayList<String>();
        	if(args.length == 2)
        		list.addAll(Arrays.asList(MinecraftServer.getServer().getAllUsernames()));
	    	if(type.equals("model")){
	    		list.addAll(entities.keySet());
	    	}
	    	if(type.equals("animation")){
	    		for(EnumAnimation ani : EnumAnimation.values()){
	    			list.add(ani.name().toLowerCase());
	    		}
	    	}
	    	return CommandBase.getListOfStringsMatchingLastWord(args, list.toArray(new String[list.size()]));
    	}
    	return super.addTabCompletionOptions(par1, args);
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
}
