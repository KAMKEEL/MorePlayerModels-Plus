package noppes.mpm.client;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.MPMRendererHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelRenderPassHelper;
import noppes.mpm.constants.EnumAnimation;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class RenderMPM extends RenderPlayer{
	public ModelMPM modelBipedMain;
	public ModelMPM modelArmorChestplate;
	public ModelMPM modelArmor;
	private ModelData data;

	// ModelMPM Presets
	// Steve
	public ModelMPM steve32 = new ModelMPM(0, 0);
	public ModelMPM steve64 = new ModelMPM(0, false);
	public ModelMPM steveArmorChest = new ModelMPM(1,0);
	public ModelMPM steveArmor = new ModelMPM(0.5F,0);

	// Alex
	public ModelMPM alex = new ModelMPM(0, true);
	public ModelMPM alex32armorChest = new ModelMPM(1,1);
	public ModelMPM alex32armor = new ModelMPM(0.5F,1);

	private RendererLivingEntity renderEntity;
	private EntityLivingBase entity;
	private ModelRenderPassHelper renderpass = new ModelRenderPassHelper();

	private static final ResourceLocation steve64Skin = new ResourceLocation("moreplayermodels:textures/skins/steve.png");
	private static final ResourceLocation alexSkin = new ResourceLocation("moreplayermodels:textures/skins/alex.png");

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
//		if (this.mainModel instanceof ModelMPM) {
//			((ModelMPM) this.mainModel).setPlayerData(this.data, entity);
//		}
		modelBipedMain.setPlayerData(data, entity);
		modelArmorChestplate.setPlayerData(data, entity);
		modelArmor.setPlayerData(data, entity);
	}

	@Override
	protected void passSpecialRender(EntityLivingBase base, double x, double y, double z)
	{
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

	private void loadTexture(File file, ResourceLocation resource, String par1Str, boolean version, int modelType){
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		ResourceLocation defaultLoc = getDefaultSkin(modelType);
		ITextureObject object = new ImageDownloadAlt(file, par1Str, defaultLoc, new ImageBufferDownloadAlt(version));
		texturemanager.loadTexture(resource, object);
	}

	public void loadResource(AbstractClientPlayer player) {
		String url = data.url;
		if(url != null && !url.isEmpty()){
			ResourceLocation location;
			if(!url.startsWith("http://") && !url.startsWith("https://")){
				location = new ResourceLocation(url);
				try {
					Minecraft.getMinecraft().getTextureManager().bindTexture(location);
				}
				catch(Exception e){
					// No Texture Found
					location = getDefaultSkin(data.modelType);
				}
				player.func_152121_a(Type.SKIN, location);
			}
			else{
				if (data.urlType == 1) {
					location = new ResourceLocation("skins64/" + url.hashCode());
					player.func_152121_a(Type.SKIN, location);
					loadTexture(null, location, url, true, data.modelType);
				} else {
					location = new ResourceLocation("skins/" + url.hashCode());
					player.func_152121_a(Type.SKIN, location);
					loadTexture(null, location, url, false, data.modelType);
				}
			}
			return;
		} else if(!data.playerLoaded){
			Minecraft mc = Minecraft.getMinecraft();
			SkinManager skinmanager = mc.func_152342_ad();
			Map map = skinmanager.func_152788_a(player.getGameProfile());
			if(map.isEmpty()){
				map = mc.func_152347_ac().getTextures(mc.func_152347_ac().fillProfileProperties(player.getGameProfile(), false), false);
			}
			if (!map.containsKey(Type.SKIN)){
				player.func_152121_a(Type.SKIN, null);
				return;
			}

			MinecraftProfileTexture profile = (MinecraftProfileTexture) map.get(Type.SKIN);
			File dir = new File((File)ObfuscationReflectionHelper.getPrivateValue(SkinManager.class, skinmanager, 3), profile.getHash().substring(0, 2));
			File file = new File(dir, profile.getHash());
			if(file.exists())
				file.delete();

			ResourceLocation location;
			if (data.modelType > 0) {
				location = new ResourceLocation("skins64/" + profile.getHash());
				player.func_152121_a(Type.SKIN, location);
				loadTexture(file, location, profile.getUrl(), true, data.modelType);
			} else {
				location = new ResourceLocation("skins/" + profile.getHash());
				player.func_152121_a(Type.SKIN, location);
				loadTexture(file, location, profile.getUrl(), false, data.modelType);
			}
			data.playerLoaded = true;
			return;
		}
		player.func_152121_a(Type.SKIN, null);
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer player){
		data = PlayerDataController.instance.getPlayerData(player);

		if(!data.loaded && RenderEvent.lastSkinTick > RenderEvent.MaxSkinTick){
			loadResource((AbstractClientPlayer) player);
			RenderEvent.lastSkinTick = 0;
			data.loaded = true;
		}
		setModelData(data, player);

		float f = 1.0F;
		GL11.glColor3f(f, f, f);
		this.modelBipedMain.onGround = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
		this.modelBipedMain.renderArms(player, 0.0625F, true);
	}

	public ResourceLocation loadCapeResource(AbstractClientPlayer player) {
		String url = data.cloakUrl;
		ResourceLocation location;
		if (url != null && !url.isEmpty()) {
			if (!url.startsWith("http://") && !url.startsWith("https://")) {
				location = new ResourceLocation(url);
				try {
					Minecraft.getMinecraft().getTextureManager().bindTexture(location);
				} catch (Exception e) {
					// No Texture Found
					location = ModelPartData.defaultCape;
				}
				// player.func_152121_a(Type.CAPE, location);
			} else {
				location = new ResourceLocation("cape/" + url.hashCode());
				// player.func_152121_a(MinecraftProfileTexture.Type.CAPE, location);
				TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
				ITextureObject object = new ImageDownloadAlt(null, url, ModelPartData.defaultCape, new ImageBufferDownloadAlt(false));
				texturemanager.loadTexture(location, object);
			}
			return location;
		}
		return null;
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
		EntityPlayer player = (EntityPlayer) par1EntityLiving;
		if(!player.isEntityAlive()){
			super.rotateCorpse(par1EntityLiving, par2, par3, par4);
			return;
		}

		if(player.ridingEntity != null){
			GL11.glTranslatef(0, data.getLegsY(), 0);
		}

		if (data.animation == EnumAnimation.SITTING){
			GL11.glTranslatef(0, -0.6f + data.getLegsY(), 0);
		}
		if (data.animation == EnumAnimation.SLEEPING_EAST){
			GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
		}
		else if (data.animation == EnumAnimation.SLEEPING_NORTH){
			GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
		}
		else if (data.animation == EnumAnimation.SLEEPING_WEST){
			GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
		}
		else if (data.animation == EnumAnimation.SLEEPING_SOUTH){
			GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1.6f + data.offsetY(), 0.05f, 0);
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
		}
		else if(data.animation == EnumAnimation.CRAWLING){
			GL11.glTranslatef(0, 0.2f, 0);
			super.rotateCorpse(par1EntityLiving, par2, par3, par4);
			GL11.glTranslatef(0, 0f, 1.5f);
			GL11.glRotatef(-90, 1.0F, 0F, 0.0F);
		}
		else
			super.rotateCorpse(par1EntityLiving, par2, par3, par4);
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
	protected void preRenderCallback(EntityLivingBase entityliving, float f){
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
//			this.mainModel = steve64;
			this.modelBipedMain = steve64;
			this.modelArmorChestplate = steveArmorChest;
			this.modelArmor = steveArmor;
		}
		else if(modelVal ==  2){
//			this.mainModel = alex;
			this.modelBipedMain = alex;
			this.modelArmorChestplate = alex32armorChest;
			this.modelArmor = alex32armor;
		}
		else{
//			this.mainModel = steve32;
			this.modelBipedMain = steve32;
			this.modelArmorChestplate = steveArmorChest;
			this.modelArmor = steveArmor;
		}
	}

	public static ResourceLocation getDefaultSkin(int modelType){
		ResourceLocation location;
		if(modelType == 2){
			location = alexSkin;
		} else if(modelType == 1){
			location = steve64Skin;
		} else {
			location = SkinManager.field_152793_a;
		}

		return location;
	}
}
