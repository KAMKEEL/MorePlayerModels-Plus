package noppes.mpm;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.constants.EnumAnimation;


public class ModelData extends ModelDataShared implements IExtendedEntityProperties{
	public boolean loaded = false;
	public boolean playerLoaded = false;
	public EntityPlayer player = null;
	
	public int rev = MorePlayerModels.Revision;
	
	public ItemStack backItem;
	
	public int inLove = 0;
	public int animationTime = 0;
	
	public EnumAnimation animation = EnumAnimation.NONE;
	public int animationStart = 0;
	
	public short soundType = 0;

	public byte urlType = 0;	//	0:url, 1:url64
	public String url= "";
	public String displayName = "";
	public int modelType = 0; 	// For Alex / Steve 3D Layers

	public ModelData(){

	}
	public NBTTagCompound writeToNBT(){
		NBTTagCompound compound = super.writeToNBT();
		compound.setInteger("Revision", rev);
		
		compound.setInteger("Animation", animation.ordinal());
		
		compound.setShort("SoundType", soundType);
		compound.setString("DisplayName", displayName);
		compound.setInteger("ModelType", modelType);

		compound.setString("CustomSkinUrl", url);
		compound.setByte("UrlType", urlType);


		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		rev = compound.getInteger("Revision");
		
		soundType = compound.getShort("SoundType");
		displayName = compound.getString("DisplayName");
		if(player != null)
			player.refreshDisplayName();
		
		String prevUrl = url;
		url = compound.getString("CustomSkinUrl");
		urlType = compound.getByte("UrlType");
		modelType = compound.getInteger("ModelType");

		setAnimation(compound.getInteger("Animation"));
		
		if(loaded && !prevUrl.equals(url))
			loaded = false;
	}

	public void setAnimation(int i) {
		if(i < EnumAnimation.values().length)
			animation = EnumAnimation.values()[i];
		else
			animation = EnumAnimation.NONE;
		
		if(animation == EnumAnimation.WAVING)
			animationTime = 80;
	}

	public EntityLivingBase getEntity(World world, EntityPlayer player){
		if(entityClass == null)
			return null;
		if(entity == null){
			try {
				entity = entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
				entity.readEntityFromNBT(extra);
				if(entity instanceof EntityLiving){
					EntityLiving living = (EntityLiving)entity;
					living.setCurrentItemOrArmor(0, player.getHeldItem());
					living.setCurrentItemOrArmor(1, player.inventory.armorItemInSlot(3));
					living.setCurrentItemOrArmor(2, player.inventory.armorItemInSlot(2));
					living.setCurrentItemOrArmor(3, player.inventory.armorItemInSlot(1));
					living.setCurrentItemOrArmor(4, player.inventory.armorItemInSlot(0));
				}
			} catch (Exception e) {
			}
		}
		return entity;
	}

	public EntityLivingBase getEntity(EntityPlayer player){
		if(entityClass == null)
			return null;
		if(entity == null){
			try {
				entity = entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {player.worldObj});

				entity.readEntityFromNBT(extra);
				if(entity instanceof EntityLiving){
					EntityLiving living = (EntityLiving)entity;
					living.setCurrentItemOrArmor(0, player.getHeldItem());
					living.setCurrentItemOrArmor(1, player.inventory.armorItemInSlot(3));
					living.setCurrentItemOrArmor(2, player.inventory.armorItemInSlot(2));
					living.setCurrentItemOrArmor(3, player.inventory.armorItemInSlot(1));
					living.setCurrentItemOrArmor(4, player.inventory.armorItemInSlot(0));
				}
			} catch (Exception e) {
			}
		}
		return entity;
	}


	public String getHash(){
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String toHash = arms.toString() + legs.toString() + body.toString() + head.toString();

			if(entityClass != null)
				toHash += entityClass.getCanonicalName();

			toHash += legParts.toString() + headwear + breasts + soundType + url;
			
			for(String name : parts.keySet()){
				toHash += name + ":" + parts.get(name).toString();
			}
			byte[] hash = digest.digest(toHash.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash){
				sb.append(String.format("%02x", b&0xff));
			}
          
			return sb.toString();
		} catch (Exception e) {
			
		}
		return "";
	}
	public ModelData copy(){
		ModelData data = new ModelData();
		data.readFromNBT(this.writeToNBT());
		data.playerLoaded = playerLoaded;
		data.player = player;
		return data;
	}

	public boolean isSleeping() {
		return isSleeping(animation);
	}
	private boolean isSleeping(EnumAnimation animation) {
		return animation == EnumAnimation.SLEEPING_EAST || animation == EnumAnimation.SLEEPING_NORTH ||
				animation == EnumAnimation.SLEEPING_SOUTH || animation == EnumAnimation.SLEEPING_WEST;
	}

	public boolean animationEquals(EnumAnimation animation2) {
		return animation2 == animation || isSleeping() && isSleeping(animation2);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		
	}

	@Override
	public void init(Entity entity, World world) {
		
	}

	public float getOffsetCamera(EntityPlayer player){
		if(!MorePlayerModels.EnablePOV)
			return 0;
		float offset = -offsetY();
		if(animation == EnumAnimation.SITTING){
			offset += 0.5f - getLegsY();
		}
		if(isSleeping())
			offset = 1.18f;
		if(animation == EnumAnimation.CRAWLING)
			offset = 0.8f;
		if(offset < -0.2f && isBlocked(player))
			offset = -0.2f;
		return offset;
	}

	private boolean isBlocked(EntityPlayer player) {
		return !player.worldObj.isAirBlock((int)player.posX, (int)player.posY + 2, (int)player.posZ);
	}



	public void setExtra(EntityLivingBase entity, String key, String value){
		key = key.toLowerCase();
		
		if(key.equals("breed") && EntityList.getEntityString(entity).equals("doggystyle.Dog")){
			try {
				Method method = entity.getClass().getMethod("setBreedID", int.class);
				method.invoke(entity, Integer.parseInt(value));
				NBTTagCompound comp = new NBTTagCompound();
				entity.writeEntityToNBT(comp);
				extra.setString("EntityData21", comp.getString("EntityData21"));
	    		clearEntity();
			} catch (Exception e) {
				
			}
		}
	}
}
