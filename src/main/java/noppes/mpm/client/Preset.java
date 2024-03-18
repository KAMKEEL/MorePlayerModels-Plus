package noppes.mpm.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.LogWriter;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.constants.EnumParts;
import noppes.mpm.controllers.ModelDataController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class Preset {

	public static EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	public ModelData data = new ModelData();
	public String fileName;
	public String name;

	public NBTTagCompound writeToNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("Filename", fileName);
		compound.setString("PresetName", name);
		compound.setTag("PresetData", data.getNBT());
		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound){
		fileName = compound.getString("Filename");
		name = compound.getString("PresetName");
		data.setNBT(compound.getCompoundTag("PresetData"));
	}

	public synchronized void save() {
		final NBTTagCompound compound = writeToNBT();
		final String filename = fileName + ".dat";

		// Remove Existing
		PresetController.Instance.removePreset(fileName);
		PresetController.Instance.addPreset(this);
		try {
			File saveDir = MorePlayerModels.presetDir;
			File file = new File(saveDir, filename);

			// Save the new file
			CompressedStreamTools.writeCompressed(compound, new FileOutputStream(file));
		} catch (Exception e) {
			LogWriter.except(e);
		}
	}
}
