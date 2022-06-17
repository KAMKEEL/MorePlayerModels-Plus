package noppes.mpm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataController {

	
	private File dir;
	public static PlayerDataController instance;
	
	public PlayerDataController(File dir){
		instance = this;
		this.dir = new File(dir, "playerdata");
		if(!this.dir.exists())
			this.dir.mkdir();
	}

	public ModelData getPlayerData(EntityPlayer player) {
		ModelData data = (ModelData) player.getExtendedProperties("MPMData");
		if(data == null){
			player.registerExtendedProperties("MPMData", data = new ModelData());
			data.player = player;
			NBTTagCompound compound = loadPlayerData(player.getCommandSenderName());
			if(compound != null)
				data.readFromNBT(compound);
		}
		return data;
	}

	private NBTTagCompound loadPlayerData(String player){
		String filename = player;
		if(filename.isEmpty())
			filename = "noplayername";
		filename += ".dat";
		try {
	        File file = new File(dir, filename);
	        if(!file.exists()){
				return null;
	        }
	        return CompressedStreamTools.readCompressed(new FileInputStream(file));
		} catch (Exception e) {
			LogWriter.except(e);
		}
		try {
	        File file = new File(dir, filename+"_old");
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
		String filename = player.getCommandSenderName();
		if(filename.isEmpty())
			filename = "noplayername";
		filename += ".dat";
		try {
            File file = new File(dir, filename+"_new");
            File file1 = new File(dir, filename+"_old");
            File file2 = new File(dir, filename);
            CompressedStreamTools.writeCompressed(data.writeToNBT(), new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file2.renameTo(file1);
            if(file2.exists())
            {
                file2.delete();
            }
            file.renameTo(file2);
            if(file.exists())
            {
                file.delete();
            }
		} catch (Exception e) {
			LogWriter.except(e);
		}
	}
}
