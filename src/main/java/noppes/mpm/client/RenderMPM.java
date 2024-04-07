package noppes.mpm.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import noppes.mpm.ModelData;
import noppes.mpm.client.controller.ClientCacheController;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelRenderPassHelper;
import noppes.mpm.constants.EnumAnimation;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;
import static noppes.mpm.client.RenderEvent.lastSkinTick;

public class RenderMPM extends RenderPlayer {
	public ModelMPM modelBipedMain;
	public ModelMPM modelArmorChestplate;
	public ModelMPM modelArmor;
	private ModelData data;

	// ModelMPM Presets
	// Steve
	public ModelMPM steve32 = new ModelMPM(0, 0);
	public ModelMPM steve64 = new ModelMPM(0, false);
	public ModelMPM alex = new ModelMPM(0, true);

	public ModelMPM steveArmorChest = new ModelMPM(1,0);
	public ModelMPM steveArmor = new ModelMPM(0.5F,0);

	public ModelMPM alex32armorChest = new ModelMPM(1,1);
	public ModelMPM alex32armor = new ModelMPM(0.5F,1);

	public RendererLivingEntity renderEntity;
	public EntityLivingBase entity;
	public ModelRenderPassHelper renderpass = new ModelRenderPassHelper();

	public static final ResourceLocation steve64Skin = new ResourceLocation("moreplayermodels:textures/skins/steve.png");
	public static final ResourceLocation alexSkin = new ResourceLocation("moreplayermodels:textures/skins/alex.png");

	public RenderMPM() {
		super();
		this.setRenderManager(RenderManager.instance);
		this.modelBipedMain = new ModelMPM(0, 0);
		this.modelArmor = new ModelMPM(0.5f, 0);
		this.modelArmorChestplate = new ModelMPM(1.0f, 0);
	}

	public void setModelData(ModelData data, EntityLivingBase entity){
		this.data = data;
		this.setModelType(data);
		modelBipedMain.setPlayerData(data, entity);
		modelArmorChestplate.setPlayerData(data, entity);
		modelArmor.setPlayerData(data, entity);
	}

	@Override
	protected void passSpecialRender(EntityLivingBase base, double x, double y, double z)
	{
		passSpecialExternal(base, x, y, z);
	}

	public void passSpecialExternal(EntityLivingBase base, double x, double y, double z){
		if(data.isSleeping() || data.animation == EnumAnimation.CRAWLING)
			y -= 1.5;
		else if(data != null)
			y -= data.getBodyY();
		base.isSneaking();
		if(data.animation == EnumAnimation.SITTING)
			y -= 0.6;
		super.passSpecialRender(base, x, y, z);
		EntityPlayer player = (EntityPlayer) base;

		Scoreboard scoreboard = ((EntityPlayer)base).getWorldScoreboard();
		ScoreObjective scoreobjective = scoreboard.func_96539_a(2);
		if(scoreobjective != null)
			y += 0.3;

		if (this.func_110813_b(base))
			ChatMessages.getChatMessages(player.getCommandSenderName()).renderMessages(x, y + 0.7 + player.height, z);
	}

	@Override
	public void doRender(AbstractClientPlayer player, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_){
		super.doRender(player, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}


	public void loadPlayerResource(EntityPlayer pl, ModelData data) {
		AbstractClientPlayer player = (AbstractClientPlayer) pl;
		data.textureLocation = null;
		if(data.url != null && !data.url.isEmpty()){
			if(!data.url.startsWith("http://") && !data.url.startsWith("https://")){
				ResourceLocation location = new ResourceLocation(data.url);
				try{
					data.textureLocation = ClientCacheController.getTexture(location.getResourcePath()).getLocation();
				}
				catch(Exception e){
					if(data.modelType == 2)
						location = alexSkin;
					else if (data.modelType == 1)
						location = steve64Skin;
					else
						location = AbstractClientPlayer.locationStevePng;

					data.resourceInit = false;
				}
				setPlayerTexture(player, location);
			}
			else{
				boolean hasUrl = data.getEntity(pl) == null;
				ResourceLocation location = new ResourceLocation("skins/" + (data.modelType + data.url + hasUrl).hashCode());
				setPlayerTexture(player, location);
				data.textureLocation = ClientCacheController.getPlayerSkin(data.url, data.modelType > 0, location).getLocation();
			}
			return;
		}
		else if(data.modelType > 0){
			String futureSkin = "https://crafatar.com/skins/" + player.getUniqueID().toString().replace("-", "") + ".png";
			ResourceLocation location = new ResourceLocation("skins/" + (futureSkin).hashCode());
			setPlayerTexture(player, location);
			data.textureLocation = ClientCacheController.getPlayerSkin(futureSkin, data.modelType > 0, location).getLocation();
			return;
		}
		else if(!data.resourceLoaded){
			Minecraft mc = Minecraft.getMinecraft();
			SkinManager skinmanager = mc.func_152342_ad();
			Map map = skinmanager.func_152788_a(player.getGameProfile());
			if (map.isEmpty()) {
				map = mc.func_152347_ac().getTextures(mc.func_152347_ac().fillProfileProperties(player.getGameProfile(), false), false);
			}
			if (map.containsKey(MinecraftProfileTexture.Type.SKIN)){
				data.textureLocation = mc.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
				setPlayerTexture(player, data.textureLocation);
				data.resourceLoaded = true;
				return;
			}
			data.resourceInit = false;
		}
		setPlayerTexture(player, null);
	}


	public void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture){
		player.func_152121_a(Type.SKIN, texture);
	}

	private ITextureObject loadTexture(File file, ResourceLocation resource, ResourceLocation def, String par1Str, boolean fix64){
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject object = texturemanager.getTexture(resource);
		if(object == null){
			object = new ImageDownloadAlt(file, par1Str, def, new ImageBufferDownloadAlt(fix64));
			texturemanager.loadTexture(resource, object);
		}
		return object;
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer player){
		data = ModelData.getData(player);
		if(data == null)
			return;


		if(data.textureLocation != null){
			if(!(data.modelType == 0 && data.url.isEmpty())){
				ImageData imageData = ClientCacheController.getTextureUnsafe(data.textureLocation.getResourcePath());
				if(imageData == null || !imageData.imageLoaded()){
					data.resourceInit = false;
				}
			}
			setPlayerTexture((AbstractClientPlayer) player, data.textureLocation);
		}

		if((!data.resourceInit || data.textureLocation == null) && lastSkinTick > RenderEvent.MaxSkinTick){
			lastSkinTick = 0;
			loadPlayerResource(player, data);
			data.resourceInit = true;
		}
		setModelData(data, player);

		float f = 1.0F;
		GL11.glColor3f(f, f, f);
		this.modelBipedMain.onGround = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
		this.modelBipedMain.renderArms(player, 0.0625F, true);
	}

	public void renderItem(EntityPlayer par1AbstractClientPlayer) {
		ItemStack itemstack1 = par1AbstractClientPlayer.inventory.getCurrentItem();

		if (itemstack1 != null)
		{
			GL11.glPushMatrix();
			float y = (data.arms.scaleY - 1) * 0.7f;

			float x = (1 - data.body.scaleX) * 0.25f + (1 - data.arms.scaleX) * 0.075f;
			GL11.glTranslatef(x, data.getBodyY(), 0);
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F + y, 0.0625F);

			if (par1AbstractClientPlayer.fishEntity != null)
			{
				itemstack1 = new ItemStack(Items.stick);
			}

			EnumAction enumaction = null;

			if (par1AbstractClientPlayer.getItemInUseCount() > 0)
			{
				enumaction = itemstack1.getItemUseAction();
			}

			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, EQUIPPED);
			boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack1, BLOCK_3D));

			if (is3D || itemstack1.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack1.getItem()).getRenderType()))
			{
				float f3 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f3 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-f3, -f3, f3);
			}
			else if (itemstack1.getItem() == Items.bow)
			{
				float f3 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f3, -f3, f3);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			}
			else if (itemstack1.getItem().isFull3D())
			{
				float f3 = 0.625F;

				if (itemstack1.getItem().shouldRotateAroundWhenRendering())
				{
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (par1AbstractClientPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block)
				{
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f3, -f3, f3);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				float f3 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f3, f3, f3);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float f4;
			float f12;
			int k;

			if (itemstack1.getItem().requiresMultipleRenderPasses())
			{
				for (k = 0; k <= itemstack1.getItem().getRenderPasses(itemstack1.getItemDamage()); ++k)
				{
					int i = itemstack1.getItem().getColorFromItemStack(itemstack1, k);
					f12 = (float)(i >> 16 & 255) / 255.0F;
					f4 = (float)(i >> 8 & 255) / 255.0F;
					float f5 = (float)(i & 255) / 255.0F;
					GL11.glColor4f(f12, f4, f5, 1.0F);
					this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack1, k);
				}
			}
			else
			{
				k = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
				float f11 = (float)(k >> 16 & 255) / 255.0F;
				f12 = (float)(k >> 8 & 255) / 255.0F;
				f4 = (float)(k & 255) / 255.0F;
				GL11.glColor4f(f11, f12, f4, 1.0F);
				this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack1, 0);
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	protected void rotateCorpse(EntityLivingBase par1EntityLiving, float par2, float par3, float par4)
	{
		byte options = shouldRotateCorpse(par1EntityLiving, par2, par3, par4, false);
		if(options < 2)
			super.rotateCorpse(par1EntityLiving, par2, par3, par4);
	}

	// 0 -- No MPM Operation
	// 1 -- MPM Operation - Super.
	// 2 -- MPM Operation - No Super
	public byte shouldRotateCorpse(EntityLivingBase par1EntityLiving, float par2, float par3, float par4, boolean manual) {
		EntityPlayer player = (EntityPlayer) par1EntityLiving;
		if(!player.isEntityAlive()){
			return 0;
		}

		byte options = 0;
		if(player.ridingEntity != null){
			GL11.glTranslatef(0, data.getLegsY(), 0);
			options = 1;
		}
		if (data.animation == EnumAnimation.SITTING){
			GL11.glTranslatef(0, -0.6f + data.getLegsY(), 0);
			options = 1;
		}

		if (data.animation == EnumAnimation.SLEEPING_EAST) {
			GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
			return 2;
		} else if (data.animation == EnumAnimation.SLEEPING_NORTH) {
			GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
			return 2;
		} else if (data.animation == EnumAnimation.SLEEPING_WEST) {
			GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
			return 2;
		} else if (data.animation == EnumAnimation.SLEEPING_SOUTH) {
			GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
			return 2;
		} else if (data.animation == EnumAnimation.CRAWLING) {
			GL11.glTranslatef(0, 0.2f, 0);
			super.rotateCorpse(par1EntityLiving, par2, par3, par4);
			GL11.glTranslatef(0, 0f, 1.5f);
			GL11.glRotatef(-90, 1.0F, 0F, 0.0F);
			return 2;
		} else {
			return options;
		}
	}

	public void renderHelmet(EntityPlayer entityPlayer) {
		ItemStack itemstack = entityPlayer.inventory.armorItemInSlot(3);
		if(itemstack == null)
			return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0, data.getBodyY(), 0);
		this.modelBipedMain.bipedHead.postRender(0.0625F);
		GL11.glScalef(data.head.scaleX, data.head.scaleY, data.head.scaleZ);
		float f1;

		if (itemstack.getItem() instanceof ItemBlock)
		{
			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
			boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D));

			if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
			{
				f1 = 0.625F;
				GL11.glTranslatef(0.0F, -0.25F, 0.0F);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, -f1);
			}

			this.renderManager.itemRenderer.renderItem(entityPlayer, itemstack, 0);
		}
		else if (itemstack.getItem() == Items.skull)
		{
			f1 = 1.0625F;
			GL11.glScalef(f1, -f1, -f1);
			GameProfile gameprofile = null;

			if (itemstack.hasTagCompound())
			{
				NBTTagCompound nbttagcompound = itemstack.getTagCompound();

				if (nbttagcompound.hasKey("SkullOwner", 10))
				{
					gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
				}
				else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
				{
					gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
				}
			}

			TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), gameprofile);
		}

		GL11.glPopMatrix();

	}

	public void renderBackitem(EntityPlayer player) {
		ItemStack itemstack = data.backItem;
		if(itemstack == null || ItemStack.areItemStacksEqual(itemstack,player.inventory.getCurrentItem()))
			return;
		Block block = null;
		if (itemstack.getItem() instanceof ItemBlock)
			block = Block.getBlockFromItem(itemstack.getItem());

		if (itemstack.getItemSpriteNumber() == 0 && block != null && RenderBlocks.renderItemIn3d(block.getRenderType()))
			return;
		GL11.glPushMatrix();
		EntityItem entity = new EntityItem(player.worldObj);
		entity.hoverStart = 0;
		entity.rotationYaw = 0;
		entity.setEntityItemStack(itemstack);

		if(data.animation == EnumAnimation.DANCING){
			float dancing = player.ticksExisted / 4f;
			GL11.glTranslatef((float)Math.sin(dancing) * 0.015F, 0.0F, 0.0F);
		}

		GL11.glTranslatef(0, data.getBodyY(), 2.2f * 0.065f * data.body.scaleZ);
		modelBipedMain.bipedBody.postRender(0.065f);
		if(itemstack.getItem() == Items.bow){
			GL11.glTranslatef(0, -3 * 0.065f, 0);
			GL11.glScalef(1.7f, 1.7f, 1.7f);
		}
		if(itemstack.getItem().isFull3D()){
			GL11.glScalef(1.7f, 1.7f, 1.7f);
		}
		else{
			GL11.glTranslatef(0, 7 * 0.065f, 0);
			GL11.glRotatef(180, 1, 0, 0);
		}
		boolean isFancy = this.renderManager.options.fancyGraphics;
		this.renderManager.options.fancyGraphics = true;

		int stack = itemstack.stackSize;
		itemstack.stackSize = 1;

		RenderItem render = (RenderItem) RenderManager.instance.getEntityRenderObject(entity);
		render.doRender(entity, 0, 0, 0, 0, 0);
		GL11.glPopMatrix();

		this.renderManager.options.fancyGraphics = isFancy;
		itemstack.stackSize = stack;
	}

	public void setEntity(EntityLivingBase entity) {
		ModelBase model = null;
		renderEntity = null;
		this.entity = entity;
		if(entity != null){
			renderEntity = (RendererLivingEntity) RenderManager.instance.getEntityRenderObject(entity);
			model = MPMRendererHelper.getMainModel(renderEntity);

			if(EntityList.getEntityString(entity).equals("doggystyle.Dog")){
				try {
					Method m = entity.getClass().getMethod("getBreed");
					Object breed = m.invoke(entity);
					m = breed.getClass().getMethod("getModel");
					model = (ModelBase) m.invoke(breed);
					model.getClass().getMethod("setPosition", int.class).invoke(model, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			renderPassModel = renderpass;
			renderpass.renderer = renderEntity;
			renderpass.entity = entity;
		}
		modelBipedMain.entityModel = modelArmorChestplate.entityModel = modelArmor.entityModel = model;
		modelBipedMain.entity = modelArmorChestplate.entity = modelArmor.entity = entity;
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase entityliving, float f){
		if(renderEntity != null)
			MPMRendererHelper.renderEquippedItems(entity, f, renderEntity);
		else
			super.renderEquippedItems(entityliving, f);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3){
		if(renderEntity != null){
			if(renderPassModel != null)
				renderPassModel.isChild = entity.isChild();
			return MPMRendererHelper.shouldRenderPass(entity, par2, par3, renderEntity);
		}
		return this.shouldRenderPass((AbstractClientPlayer)par1EntityLivingBase, par2, par3);
	}

	@Override
	public void preRenderCallback(EntityLivingBase entityliving, float f){
		float ff = 0.9375f;
		GL11.glScalef((ff / 5) * data.size, (ff / 5) * data.size, (ff / 5) * data.size);
		if(renderEntity != null){
			MPMRendererHelper.preRenderCallback(entity, f, renderEntity);
		}
		else
			super.preRenderCallback(entityliving, f);
	}

	@Override
	protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2){
		if(renderEntity != null){
			return MPMRendererHelper.handleRotationFloat(entity, par2, renderEntity);
		}
		return super.handleRotationFloat(par1EntityLivingBase, par2);
	}
	@Override
	protected ResourceLocation getEntityTexture(AbstractClientPlayer player){
		return MPMRendererHelper.getResource(player, renderEntity, entity);
	}

	public void setModelType(ModelData data){
		int modelVal = data.modelType;
		if(modelVal ==  1){
			this.modelBipedMain = steve64;
			this.modelArmorChestplate = steveArmorChest;
			this.modelArmor = steveArmor;
		}
		else if(modelVal ==  2){
			this.modelBipedMain = alex;
			this.modelArmorChestplate = alex32armorChest;
			this.modelArmor = alex32armor;
		}
		else{
			this.modelBipedMain = steve32;
			this.modelArmorChestplate = steveArmorChest;
			this.modelArmor = steveArmor;
		}
	}
}
