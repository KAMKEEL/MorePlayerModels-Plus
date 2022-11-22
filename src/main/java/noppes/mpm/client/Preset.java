package noppes.mpm.client;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.constants.EnumParts;

public class Preset {
	
	public ModelData data = new ModelData();
	public String name;

	public NBTTagCompound writeToNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("PresetName", name);
		compound.setTag("PresetData", data.writeToNBT());
		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound){
		name = compound.getString("PresetName");
		data.readFromNBT(compound.getCompoundTag("PresetData"));
	}

	public static void FillDefault(HashMap<String,Preset> presets){
		ModelData data = new ModelData();
		Preset preset = new Preset();
		preset.name = "Elf Male";
		preset.data = data;
		data.getPartConfig(EnumParts.LEGS).setScale(0.85f,1.15f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.85f,1.15f);
		data.getPartConfig(EnumParts.BODY).setScale(0.85f,1.15f);
		data.getPartConfig(EnumParts.HEAD).setScale(0.85f,0.95f);
		presets.put("elf male", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Elf Female";
		preset.data = data;
		data.getOrCreatePart(EnumParts.BREASTS).type = 2;
		data.getPartConfig(EnumParts.LEGS).setScale(0.8f,1.05f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.8f,1.05f);
		data.getPartConfig(EnumParts.BODY).setScale(0.8f,1.05f);
		data.getPartConfig(EnumParts.HEAD).setScale(0.8f,0.85f);
		presets.put("elf female", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Dwarf Male";
		preset.data = data;
		data.getPartConfig(EnumParts.LEGS).setScale(1.1f,0.7f, 0.9f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.9f,0.7f);
		data.getPartConfig(EnumParts.BODY).setScale(1.2f, 0.7f, 1.5f);
		data.getPartConfig(EnumParts.HEAD).setScale(0.85f,0.85f);
		presets.put("dwarf male", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Dwarf Female";
		preset.data = data;
		data.getOrCreatePart(EnumParts.BREASTS).type = 2;
		data.getPartConfig(EnumParts.LEGS).setScale(0.9f,0.65f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.9f,0.65f);
		data.getPartConfig(EnumParts.BODY).setScale(1f, 0.65f, 1.1f);
		data.getPartConfig(EnumParts.HEAD).setScale(0.85f,0.85f);
		presets.put("dwarf female", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Orc Male";
		preset.data = data;
		data.getPartConfig(EnumParts.LEGS).setScale(1.2f,1.05f);
		data.getPartConfig(EnumParts.ARMS).setScale(1.2f,1.05f);
		data.getPartConfig(EnumParts.BODY).setScale(1.4f, 1.1f, 1.5f);
		data.getPartConfig(EnumParts.HEAD).setScale(1.2f,1.1f);
		presets.put("orc male", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Orc Female";
		preset.data = data;
		data.getOrCreatePart(EnumParts.BREASTS).type = 2;
		data.getPartConfig(EnumParts.LEGS).setScale(1.1f,1);
		data.getPartConfig(EnumParts.ARMS).setScale(1.1f,1);
		data.getPartConfig(EnumParts.BODY).setScale(1.1f, 1f, 1.25f);
		presets.put("orc female", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Human Male";
		preset.data = data;
		presets.put("human male", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Human Female";
		preset.data = data;
		data.getOrCreatePart(EnumParts.BREASTS).type = 2;
		data.getPartConfig(EnumParts.LEGS).setScale(0.92f,0.92f);
		data.getPartConfig(EnumParts.HEAD).setScale(0.95f,0.95f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.80f,0.92f);
		data.getPartConfig(EnumParts.BODY).setScale(0.92f, 0.92f);
		presets.put("human female", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Cat Male";
		preset.data = data;
		ModelPartData ears = data.getOrCreatePart(EnumParts.EARS);
		ears.type = 0;
		ears.color = 0xD9A64E;
		ModelPartData snout = data.getOrCreatePart(EnumParts.SNOUT);
		snout.type = 0;
		snout.color = 0xD9A64E;
		ModelPartData tail = data.getOrCreatePart(EnumParts.TAIL);
		tail.type = 0;
		tail.color = 0xD9A64E;
		presets.put("cat male", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Cat Female";
		preset.data = data;
		ears = data.getOrCreatePart(EnumParts.EARS);
		ears.type = 0;
		ears.color = 0xD9A64E;
		snout = data.getOrCreatePart(EnumParts.SNOUT);
		snout.type = 0;
		snout.color = 0xD9A64E;
		tail = data.getOrCreatePart(EnumParts.TAIL);
		tail.type = 0;
		tail.color = 0xD9A64E;
		data.getOrCreatePart(EnumParts.BREASTS).type = 2;
		data.getPartConfig(EnumParts.HEAD).setScale(0.95f,0.95f);
		data.getPartConfig(EnumParts.LEGS).setScale(0.92f,0.92f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.80f,0.92f);
		data.getPartConfig(EnumParts.BODY).setScale(0.92f, 0.92f);
		presets.put("cat female", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Wolf Male";
		preset.data = data;
		ears = data.getOrCreatePart(EnumParts.EARS);
		ears.type = 0;
		ears.color = 0x5E5855;
		snout = data.getOrCreatePart(EnumParts.SNOUT);
		snout.type = 2;
		snout.color = 0x5E5855;
		tail = data.getOrCreatePart(EnumParts.TAIL);
		tail.type = 0;
		tail.color = 0x5E5855;
		presets.put("wolf male", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Wolf Female";
		preset.data = data;
		ears = data.getOrCreatePart(EnumParts.EARS);
		ears.type = 0;
		ears.color = 0x5E5855;
		snout = data.getOrCreatePart(EnumParts.SNOUT);
		snout.type = 2;
		snout.color = 0x5E5855;
		tail = data.getOrCreatePart(EnumParts.TAIL);
		tail.type = 0;
		tail.color = 0x5E5855;
		data.getOrCreatePart(EnumParts.BREASTS).type = 2;
		data.getPartConfig(EnumParts.HEAD).setScale(0.95f,0.95f);
		data.getPartConfig(EnumParts.LEGS).setScale(0.92f,0.92f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.80f,0.92f);
		data.getPartConfig(EnumParts.BODY).setScale(0.92f, 0.92f);
		presets.put("wolf female", preset);

		data = new ModelData();
		preset = new Preset();
		preset.name = "Enderchibi";
		preset.data = data;
		data.getPartConfig(EnumParts.LEGS).setScale(0.65f,0.75f);
		data.getPartConfig(EnumParts.ARMS).setScale(0.50f,1.45f);
		ModelPartData part = data.getOrCreatePart(EnumParts.PARTICLES);
		part.type = 1;
		part.color = 0xFF0000;
		presets.put("enderchibi", preset);

	}
}
