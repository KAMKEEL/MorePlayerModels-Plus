package noppes.mpm.commands;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import noppes.mpm.ModelData;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPacketClient;
import noppes.mpm.controllers.PermissionController;
import noppes.mpm.controllers.data.PermissionData;

import java.util.List;

public class CommandReload extends MpmCommandInterface {

	@Override
	public String getCommandName() {
		return "mpmreload";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] var2) {
		String[] usernames = MinecraftServer.getServer().getAllUsernames();
		for (String username : usernames) {
			EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(username);
			if(player != null){
				PermissionData permissionData = PermissionController.Instance.getPermissionData(player.getUniqueID());
				permissionData.updatePermissions();
				NBTTagCompound permissionCompound = PermissionController.Instance.writeNBT(player);
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.PERMISSION_RECEIVE, permissionCompound);
			}
		}

		PermissionData.reloadedTime = System.currentTimeMillis();
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/mpmreload -- Reloads all Permissions";
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
