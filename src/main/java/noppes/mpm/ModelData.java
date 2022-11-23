package noppes.mpm;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.concurrent.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumParts;


public class ModelData extends ModelDataShared implements IExtendedEntityProperties{
	public static ExecutorService saveExecutor = Executors.newSingleThreadExecutor();
	public boolean loaded = false;
	public boolean playerLoaded = false;
	public boolean cloakLoaded = false;
	public EntityPlayer player = null;
	
	public int rev = MorePlayerModels.Revision;

	public ItemStack backItem;
	
	public int inLove = 0;
	public int animationTime = 0;
	
	public EnumAnimation animation = EnumAnimation.NONE;
	public int animationStart = 0;
	
	public short soundType = 0;
	public ResourceLocation cloakTexture;

	public byte urlType = 0;	//	0:url, 1:url64
	public String url= "";
	public String cloakUrl = "";
	public String displayName = "";
	public int modelType = 0; 	// 	0: Steve, 1: Steve64, 2: Alex

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
		compound.setString("CloakUrl", cloakUrl);

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
		String prevCloakUrl = cloakUrl;
		byte prevUrlType = urlType;
		int prevModelType = modelType;
		url = compound.getString("CustomSkinUrl");
		urlType = compound.getByte("UrlType");
		modelType = compound.getInteger("ModelType");
		cloakUrl = compound.getString("CloakUrl");

		setAnimation(compound.getInteger("Animation"));
		
		if(!prevUrl.equals(url) || prevUrlType != urlType || prevModelType != modelType){
			loaded = false;
			playerLoaded = false;
		}
		if(!prevCloakUrl.equals(cloakUrl)){
			cloakLoaded = false;
		}
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

			toHash += legParts.toString() + headwear + soundType + url;
			
			for(EnumParts e : parts.keySet()){
				toHash += e.name + ":" + parts.get(e).toString();
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
		data.cloakLoaded = cloakLoaded;
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

	public void save(){
		if(player == null)
			return;
		final EntityPlayer player = this.player;
		saveExecutor.submit(() -> {
			try {
				String filename = player.getUniqueID().toString().toLowerCase();
				if(filename.isEmpty())
					filename = "noplayername";
				filename += ".dat";
				File file = new File(MorePlayerModels.dir, filename+"_new");
				File file1 = new File(MorePlayerModels.dir, filename+"_old");
				File file2 = new File(MorePlayerModels.dir, filename);
				CompressedStreamTools.writeCompressed(writeToNBT(), new FileOutputStream(file));
				if(file1.exists()){
					file1.delete();
				}
				file2.renameTo(file1);
				if(file2.exists()){
					file2.delete();
				}
				file.renameTo(file2);
				if(file.exists()){
					file.delete();
				}
			} catch (Exception e) {
				LogWriter.except(e);
			}
		});
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
