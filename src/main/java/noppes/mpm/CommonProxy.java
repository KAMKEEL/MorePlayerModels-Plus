package noppes.mpm;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z){
		return null;
	}

	public ModelData getClientPlayerData(EntityPlayer player) {
		return null;
	}
	
	public void load(){
		MorePlayerModels.Channel.register(new PacketHandlerServer());
	};

	public void postLoad() {

	}

}
