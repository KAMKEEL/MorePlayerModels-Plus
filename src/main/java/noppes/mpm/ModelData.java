package noppes.mpm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.renderer.texture.ITextureObject;
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
import noppes.mpm.controllers.ModelDataController;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;


public class ModelData extends ModelDataShared implements IExtendedEntityProperties{
	public ResourceLocation textureLocation = null;
	public boolean resourceInit = false;
	public boolean resourceLoaded = false;

	public boolean didSit = false;
	public ItemStack backItem;

	public int inLove = 0;
	public int animationTime = -1;

	public EnumAnimation animation = EnumAnimation.NONE;
	public EnumAnimation prevAnimation = EnumAnimation.NONE;
	public int animationStart = 0;
	public long lastEdited = System.currentTimeMillis();

	public short soundType = 0;
	public double prevPosX, prevPosY, prevPosZ;


	public EntityPlayer player = null;
	public String playername = "";
	public String uuid = "";

	public int rev = MorePlayerModels.Revision;

	public byte urlType = 0;	//	0:url, 1:url64
	public int modelType = 0; 	// 	0: Steve, 1: Steve64, 2: Alex
	public int size = 5;

	public String url= "";
	public String cloakUrl= "";
	public String displayName = "";

	public ModelData(){}

	public ModelData(EntityPlayer player){
		this.player = player;
		this.playername = player.getCommandSenderName();
		this.uuid = player.getUniqueID().toString();
	}

	public NBTTagCompound getNBT(){
		if(player != null){
			playername = player.getCommandSenderName();
			uuid = player.getPersistentID().toString();
		}
		NBTTagCompound compound = super.getNBT();
		compound.setInteger("Revision", rev);

		compound.setInteger("Animation", animation.ordinal());
		compound.setInteger("Size", size);
		
		compound.setShort("SoundType", soundType);
		compound.setString("DisplayName", displayName);
		compound.setInteger("ModelType", modelType);

		compound.setString("CustomSkinUrl", url);
		compound.setByte("UrlType", urlType);
		compound.setString("CloakUrl", cloakUrl);

		compound.setLong("LastEdited", lastEdited);

		compound.setString("PlayerName", playername);
		compound.setString("UUID", uuid);

		return compound;
	}
	
	public void setNBT(NBTTagCompound compound){
		String prevUrl = url;
		int prevModelType = modelType;
		super.setNBT(compound);
		rev = compound.getInteger("Revision");
		size = compound.getInteger("Size");
		if(size <= 0)
			size = 5;
		if(size > 10)
			size = 5;

		soundType = compound.getShort("SoundType");
		lastEdited = compound.getLong("LastEdited");
		displayName = compound.getString("DisplayName");

		if(player != null)
			player.refreshDisplayName();

		setAnimation(compound.getInteger("Animation"));

		url = compound.getString("CustomSkinUrl");
		urlType = compound.getByte("UrlType");
		modelType = compound.getInteger("ModelType");
		cloakUrl = compound.getString("CloakUrl");

		if(!prevUrl.equals(url)) {
			resourceInit = false;
			resourceLoaded = false;
		}

		if(modelType != prevModelType) {
			resourceInit = false;
			resourceLoaded = false;
		}

		if(player != null){
			playername = player.getCommandSenderName();
			uuid = player.getPersistentID().toString();
		}
		else{
			playername = compound.getString("PlayerName");
			uuid = compound.getString("UUID");
		}
	}

	public void setAnimation(int i) {
		if(i < EnumAnimation.values().length)
			animation = EnumAnimation.values()[i];
		else
			animation = EnumAnimation.NONE;
		setAnimation(animation);
	}

	public void setAnimation(EnumAnimation ani) {
		animationTime = -1;
		animation = ani;
		lastEdited = System.currentTimeMillis();

		if(animation == EnumAnimation.WAVING)
			animationTime = 80;

		if(animation == EnumAnimation.YES || animation == EnumAnimation.NO)
			animationTime = 60;

		if(animation == EnumAnimation.SITTING)
			didSit = true;

		if(player == null || ani == EnumAnimation.NONE)
			animationStart = -1;
		else
			animationStart = player.ticksExisted;
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

	public ModelData copy(){
		ModelData data = new ModelData();
		data.setNBT(this.getNBT());
		data.player = player;
		data.resourceLoaded = false;
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
	public void saveNBTData(NBTTagCompound compound){}

	@Override
	public void loadNBTData(NBTTagCompound compound) {}
	@Override
	public void init(Entity entity, World world) {}

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

	public synchronized void save() {
		if(uuid.isEmpty())
			return;
		final NBTTagCompound compound = getNBT();
		final String filename;
		filename = uuid + ".dat";
		ModelDataController.Instance.putModelDataCache(uuid, this);
		ModelDataController.modelDataThread.execute(() -> {
			try {
				File saveDir = ModelDataController.getSaveDir();
				File file = new File(saveDir, filename + "_new");
				File file1 = new File(saveDir, filename);
				CompressedStreamTools.writeCompressed(compound, new FileOutputStream(file));
				if(file1.exists()){
					file1.delete();
				}
				file.renameTo(file1);
			} catch (Exception e) {
				LogWriter.except(e);
			}
		});
	}


	public void load() {
		NBTTagCompound data = ModelDataController.Instance.loadModelData(player.getPersistentID().toString());
		if (data != null) {
			this.setNBT(data);
		}
	}

	public static ModelData getData(EntityPlayer entity) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return MorePlayerModels.proxy.getClientPlayerData(entity);
		} else {
			return ModelDataController.Instance.getModelData(entity);
		}
	}
}
