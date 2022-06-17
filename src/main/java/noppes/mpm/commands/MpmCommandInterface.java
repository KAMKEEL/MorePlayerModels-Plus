package noppes.mpm.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public abstract class MpmCommandInterface extends CommandBase {

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		return true;
	}

	@Override
    public int getRequiredPermissionLevel(){
        return opsOnly()?2:0;
    }
	
	public boolean opsOnly(){
		return false;
	}
	
	public boolean isPlayerOp(ICommandSender player){
		return player.canCommandSenderUseCommand(2, "mpm");
	}
}
