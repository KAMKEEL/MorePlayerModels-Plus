package noppes.mpm;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.mpm.constants.EnumParts;

import java.util.HashMap;


public class ModelDataShared{
	public ModelPartConfig arms = new ModelPartConfig();
	public ModelPartConfig body = new ModelPartConfig();
	public ModelPartConfig legs = new ModelPartConfig();
	public ModelPartConfig head = new ModelPartConfig();

	public ModelPartData legParts = new ModelPartData("legs");
	
	public Class<? extends EntityLivingBase> entityClass;
	protected EntityLivingBase entity;
	
	public NBTTagCompound extra = new NBTTagCompound();

	protected HashMap<EnumParts, ModelPartData> parts = new HashMap<EnumParts, ModelPartData>();
	public byte breasts = 0;
	public byte cloak = 0;

	// 3D Layers
	public byte headwear = 2;
	public byte bodywear = 0;
	public byte legwear = 0;
	public byte armwear = 0;

	// Solid or normal arm/legwear [0: None, 1: Both, 2: Right, 3: Left]
	public byte solidArmwear = 0;
	public byte solidLegwear = 0;

	// Hide Body Parts [0: None, 1: Both, 2: Right, 3: Left],
	public byte hideHead = 0;
	public byte hideBody = 0;
	public byte hideArms = 0;
	public byte hideLegs = 0;
			
	public NBTTagCompound getNBT(){
		NBTTagCompound compound = new NBTTagCompound();

		if(entityClass != null)
			compound.setString("EntityClass", entityClass.getCanonicalName());

		compound.setTag("ArmsConfig", arms.writeToNBT());
		compound.setTag("BodyConfig", body.writeToNBT());
		compound.setTag("LegsConfig", legs.writeToNBT());
		compound.setTag("HeadConfig", head.writeToNBT());

		compound.setTag("LegParts", legParts.writeToNBT());

		compound.setByte("Headwear", headwear);
		compound.setByte("Bodywear", bodywear);
		compound.setByte("Armwear", armwear);
		compound.setByte("Legwear", legwear);

		compound.setByte("SolidArmwear", solidArmwear);
		compound.setByte("SolidLegwear", solidLegwear);

		compound.setByte("hideHead", hideHead);
		compound.setByte("hideBody", hideBody);
		compound.setByte("hideArms", hideArms);
		compound.setByte("hideLegs", hideLegs);

		compound.setByte("Cloak", cloak);
		compound.setByte("Breasts", breasts);
		compound.setTag("ExtraData", extra);

		NBTTagList list = new NBTTagList();
		for(EnumParts e : parts.keySet()){
			NBTTagCompound item = parts.get(e).writeToNBT();
			item.setString("PartName", e.name);
			list.appendTag(item);
		}
		compound.setTag("Parts", list);
		
		return compound;
	}
	
	public void setNBT(NBTTagCompound compound){
		setEntityClass(compound.getString("EntityClass"));
		
		arms.readFromNBT(compound.getCompoundTag("ArmsConfig"));
		body.readFromNBT(compound.getCompoundTag("BodyConfig"));
		legs.readFromNBT(compound.getCompoundTag("LegsConfig"));
		head.readFromNBT(compound.getCompoundTag("HeadConfig"));

		legParts.readFromNBT(compound.getCompoundTag("LegParts"));

		headwear = compound.getByte("Headwear");
		bodywear = compound.getByte("Bodywear");
		armwear = compound.getByte("Armwear");
		legwear = compound.getByte("Legwear");

		solidArmwear = compound.getByte("SolidArmwear");
		solidLegwear = compound.getByte("SolidLegwear");

		hideHead = compound.getByte("hideHead");
		hideBody = compound.getByte("hideBody");
		hideArms = compound.getByte("hideArms");
		hideLegs = compound.getByte("hideLegs");

		breasts = compound.getByte("Breasts");
		cloak = compound.getByte("Cloak");
		extra = compound.getCompoundTag("ExtraData");

		HashMap<EnumParts,ModelPartData> parts = new HashMap<EnumParts,ModelPartData>();
		NBTTagList list = compound.getTagList("Parts", 10);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound item = list.getCompoundTagAt(i);
			String name = item.getString("PartName");
			ModelPartData part = new ModelPartData(name);
			part.readFromNBT(item);
			EnumParts e = EnumParts.FromName(name);
			if(e != null)
				parts.put(e, part);
		}
		this.parts = parts;
	}

	private void setEntityClass(String string) {
		entityClass = null;
		entity = null;
		try {
			Class<?> cls = Class.forName(string);
            if (EntityLivingBase.class.isAssignableFrom(cls)) 
            	entityClass = cls.asSubclass(EntityLivingBase.class);
            
		} catch (ClassNotFoundException e) {
			
		}
	}


	public void setEntityClass(Class<? extends EntityLivingBase> entityClass){
		this.entityClass = entityClass;
		entity = null;
		extra = new NBTTagCompound();
	}
	
	public Class<? extends EntityLivingBase> getEntityClass(){
		return entityClass;
	}

	public float offsetY() {
		if(entity == null)
			return -getBodyY();
		return entity.height - 1.8f;
	}
	
	public void clearEntity() {
		entity = null;
	}

	public ModelPartData getPartData(EnumParts type){
		if(type == EnumParts.LEGS)
			return legParts;
		return parts.get(type);
	}

	public void removePart(EnumParts type) {
		parts.remove(type);
	}

	public ModelPartData getOrCreatePart(EnumParts type) {
		if(type == null)
			return null;
		ModelPartData part = getPartData(type);
		if(part == null)
			parts.put(type, part = new ModelPartData(type.name));
		return part;
	}

	public float getBodyY(){
		return (1 - body.scaleY) * 0.75f + getLegsY();
	}


	public float getLegsY() {
		if(legParts.type == 3)
			return (0.87f - legs.scaleY);
		return (1 - legs.scaleY) * 0.75f;
	}

	public ModelPartConfig getPartConfig(EnumParts type){
		if(type == EnumParts.BODY)
			return body;
		if(type == EnumParts.ARMS)
			return arms;
		if(type == EnumParts.LEGS)
			return legs;

		return head;
	}
}
