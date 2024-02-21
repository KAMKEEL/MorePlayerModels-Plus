package net.minecraft.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import noppes.mpm.client.model.ModelMPM;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

public class MPMRendererHelper {
	
	public static ModelBase getMainModel(RendererLivingEntity renderer){
		return renderer.mainModel;
	}

	public static void setMainModel(RenderPlayer renderer, ModelMPM modelBipedMain) {
		renderer.mainModel = modelBipedMain;
	}

	public static ResourceLocation getResource(AbstractClientPlayer player, RendererLivingEntity render, Entity entity){
		if(render != null){
			try{
				return render.getEntityTexture(entity);
			}
			catch(Exception ex){
				
			}
		}
		return player.getLocationSkin();
	}

	public static int shouldRenderPass(EntityLivingBase entity, int par2, float par3, RendererLivingEntity renderEntity) {
		return renderEntity.shouldRenderPass(entity, par2, par3);
	}

	public static void preRenderCallback(EntityLivingBase entity, float f,
			RendererLivingEntity renderEntity) {
		renderEntity.preRenderCallback(entity, f);
	}

	public static ModelBase getPassModel(RendererLivingEntity render) {
		return render.renderPassModel;
	}

	public static float handleRotationFloat(EntityLivingBase entity,
			float par2, RendererLivingEntity renderEntity) {
		return renderEntity.handleRotationFloat(entity, par2);
	}

	private static ModelBiped modelArmor = new ModelBiped(0.5F);
	public static void renderEquippedItems(EntityLivingBase entity, float f,
			RendererLivingEntity renderEntity) {
		renderEntity.renderEquippedItems(entity, f);

		if(entity instanceof EntitySheep){
			EntitySheep sheep = (EntitySheep) entity;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	        ItemStack itemstack = player.inventory.armorItemInSlot(3);
			RenderManager renderManager = RenderManager.instance;

	        if (itemstack != null)
	        {
	            GL11.glPushMatrix();
				ModelSheep2 model = (ModelSheep2) MPMRendererHelper.getMainModel(renderEntity);
				model.head.postRender(0.0625F);
	            float f1;
                GL11.glTranslatef(0.0F, 0.15F, -0.1F);

	            Item item = itemstack.getItem();

	            if (item instanceof ItemArmor)
	            {
	                ItemArmor itemarmor = (ItemArmor)item;
	                renderManager.renderEngine.bindTexture(RenderBiped.getArmorResource(entity, itemstack, 3, null));
	                modelArmor.bipedHead.render(0.0625F);

	            }
	            else if (itemstack.getItem() instanceof ItemBlock)
	            {
	                net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
	                boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

	                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
	                {
	                    f1 = 0.625F;
	                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
	                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
	                    GL11.glScalef(f1, -f1, -f1);
	                }

	                renderManager.itemRenderer.renderItem(entity, itemstack, 0);
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
	        itemstack = player.inventory.armorItemInSlot(0);
	        if (itemstack != null)
	        {
	        	
	        }

		    itemstack = player.getHeldItem();
		    if (itemstack != null){
				GL11.glPushMatrix();
				if (sheep.isChild()){
				    float scale = 0.5F;
				    GL11.glTranslatef(0.0F, 0.625F, 0.0F);
				    GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
				    GL11.glScalef(scale, scale, scale);
				}
				ModelSheep2 model = (ModelSheep2) MPMRendererHelper.getMainModel(renderEntity);
				model.leg3.postRender(0.0625F);

				GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
				
				EnumAction enumaction = null;
				
				if (player.getItemInUseCount() > 0)
				{
				    enumaction = itemstack.getItemUseAction();
				}				
				
				net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
				boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));
				
				if (is3D || itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
				{
				    float f2 = 0.5F;
				    GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				    f2 *= 0.75F;
				    GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				    GL11.glScalef(-f2, -f2, f2);
				}
				else if (itemstack.getItem() == Items.bow)
				{
				    float f2 = 0.625F;
				    GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				    GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				    GL11.glScalef(f2, -f2, f2);
				    GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				}
				else if (itemstack.getItem().isFull3D())
				{
				    float f2 = 0.625F;
				
				    if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block)
				    {
				        GL11.glTranslatef(0.05F, 0.0F, -0.1F);
				        GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
				        GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
				        GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				    }
				
				    if (itemstack.getItem().shouldRotateAroundWhenRendering())
				    {
				        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
				        GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				    }
				
				    GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				    GL11.glScalef(f2, -f2, f2);
				    GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				}
				else
				{
				    float f2 = 0.375F;
				    GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				    GL11.glScalef(f2, f2, f2);
				    GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				    GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
				}
				
				float f3;
				int k;
				float f12;
				
				if (itemstack.getItem().requiresMultipleRenderPasses())
				{
				    for (k = 0; k < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++k)
				    {
				        int i = itemstack.getItem().getColorFromItemStack(itemstack, k);
				        f12 = (float)(i >> 16 & 255) / 255.0F;
				        f3 = (float)(i >> 8 & 255) / 255.0F;
				        float f4 = (float)(i & 255) / 255.0F;
				        GL11.glColor4f(f12, f3, f4, 1.0F);
				        renderManager.itemRenderer.renderItem(entity, itemstack, k);
				    }
				}
				else
				{
				    k = itemstack.getItem().getColorFromItemStack(itemstack, 0);
				    float f11 = (float)(k >> 16 & 255) / 255.0F;
				    f12 = (float)(k >> 8 & 255) / 255.0F;
				    f3 = (float)(k & 255) / 255.0F;
				    GL11.glColor4f(f11, f12, f3, 1.0F);
				    renderManager.itemRenderer.renderItem(entity, itemstack, 0);
				}

		      GL11.glPopMatrix();
		    }
		}
	}
}
