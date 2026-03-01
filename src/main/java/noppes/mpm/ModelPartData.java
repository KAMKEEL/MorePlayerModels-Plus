package noppes.mpm;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModelPartData {
	private static Map<String, ResourceLocation> resources = new HashMap<String, ResourceLocation>();
	public int color = 0xFFFFFF;
	public byte type = 0;
	public byte pattern = 0;
	public boolean playerTexture;
	public String name;
	public NBTTagCompound customData = new NBTTagCompound();
	private ResourceLocation location;
	private String custom = "";

	public static final ResourceLocation defaultCape = new ResourceLocation("moreplayermodels:textures/cloak/cloak.png");

	public ModelPartData(String name) {
		this.name = name;
	}

	public NBTTagCompound writeToNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setByte("Type", type);
		compound.setInteger("Color", color);
		compound.setBoolean("PlayerTexture", playerTexture);
		compound.setByte("Pattern", pattern);
		if(!custom.isEmpty())
			compound.setString("CustomTexture", custom);
		compound.setTag("CustomData", customData.copy());

		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound){
		type = compound.getByte("Type");
		color = compound.getInteger("Color");
		playerTexture = compound.getBoolean("PlayerTexture");
		pattern = compound.getByte("Pattern");
		custom = compound.getString("CustomTexture");
		customData = compound.getCompoundTag("CustomData");
		location = null;
	}

	public ResourceLocation getResource(){
		if(location != null)
			return location;
		String texture = name + "/";
		if(!custom.equals("")){
			texture += custom;
		} else {
			texture += type;
		}
		if((location = resources.get(texture)) != null)
			return location;

		location = new ResourceLocation("moreplayermodels:textures/" + texture + ".png");
		resources.put(texture, location);
		return location;
	}

	public void updateTextureLocation(){
		String texture = name + "/";
		if(!custom.equals("")){
			texture += custom;
		} else {
			texture += type;
		}

		location = new ResourceLocation("moreplayermodels:textures/" + texture + ".png");
		resources.put(texture, location);
	}

	public void setCustomResource(String texture){
		custom = texture;
	}

	public String toString(){
		return "Color: " + color + " Type: " + type;
	}

	public void setType(int type){
		this.type = (byte) type;
		location = null;
	}
}
