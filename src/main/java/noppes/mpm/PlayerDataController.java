package noppes.mpm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class PlayerDataController {

	public static PlayerDataController instance;
	
	public PlayerDataController(File dir){
		instance = this;
	}

	public ModelData getPlayerData(EntityPlayer player) {
		ModelData data = (ModelData) player.getExtendedProperties("MPMData");
		if(data == null){
			player.registerExtendedProperties("MPMData", data = new ModelData());
			data.player = player;
			NBTTagCompound compound = loadPlayerData(player.getUniqueID());
			if(compound != null)
				data.readFromNBT(compound);
		}
		return data;
	}

	private NBTTagCompound loadPlayerData(UUID id){
		String filename = id.toString();
		if(filename.isEmpty())
			filename = "noplayername";
		filename += ".dat";
		try {
			File file = new File(MorePlayerModels.dir, filename);
			if(!file.exists()){
				return null;
			}
			return CompressedStreamTools.readCompressed(new FileInputStream(file));
		} catch (Exception e) {
			LogWriter.except(e);
		}
		try {
			File file = new File(MorePlayerModels.dir, filename+"_old");
			if(!file.exists()){
				return null;
			}
			return CompressedStreamTools.readCompressed(new FileInputStream(file));

		} catch (Exception e) {
			LogWriter.except(e);
		}
		return null;
	}

	public void savePlayerData(EntityPlayer player, ModelData data){
		data.save();
	}
}
