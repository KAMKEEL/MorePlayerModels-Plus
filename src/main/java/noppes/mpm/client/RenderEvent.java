package noppes.mpm.client;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.MPMRendererHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import noppes.mpm.*;
import noppes.mpm.constants.EnumAnimation;
import org.lwjgl.opengl.GL11;

public class RenderEvent {
	public static RenderEvent Instance;
	public static RenderMPM renderer = new RenderMPM();
	public static long lastSkinTick = 0;
	public static long lastCapeTick = 0;
	public final static long MaxSkinTick = 6;
	private ModelData data;

	private static final Entity hideNameSheep = new EntitySheep(null);

	public RenderEvent(){
		Instance = this;
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void pre(RenderPlayerEvent.Pre event){
		if(MorePlayerModels.HidePlayerNames && event.entity.riddenByEntity == null){
			event.entity.riddenByEntity = hideNameSheep;
		}
		if(!(event.entity instanceof AbstractClientPlayer))
			return;

		EntityPlayer player = event.entityPlayer;
		this.data = PlayerDataController.instance.getPlayerData(player);
		renderer.setModelData(data, player);
		setModels(event.renderer);
		if(!data.loaded && lastSkinTick > MaxSkinTick){
			renderer.loadResource((AbstractClientPlayer) player);
			lastSkinTick = 0;
			data.loaded = true;
		}
		if(!(event.renderer instanceof RenderMPM)){
			RenderManager.instance.entityRenderMap.put(EntityPlayer.class, renderer);
			RenderManager.instance.entityRenderMap.put(EntityPlayerSP.class, renderer);
			RenderManager.instance.entityRenderMap.put(EntityPlayerMP.class, renderer);
			RenderManager.instance.entityRenderMap.put(EntityOtherPlayerMP.class, renderer);
			RenderManager.instance.entityRenderMap.put(EntityClientPlayerMP.class, renderer);
			RenderManager.instance.entityRenderMap.put(AbstractClientPlayer.class, renderer);
		}

		EntityLivingBase entity = data.getEntity(player.worldObj, player);
		renderer.setEntity(entity);
		if(player == Minecraft.getMinecraft().thePlayer){
			player.yOffset = 1.62f;
			data.backItem = player.inventory.mainInventory[0];
		}
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void post(RenderLivingEvent.Post event){
		if(event.entity.riddenByEntity == hideNameSheep){
			event.entity.riddenByEntity = null;
		}
	}

	private void setModels(RenderPlayer render){
		if(MPMRendererHelper.getMainModel(render) == renderer.modelBipedMain)
			return;
		ReflectionHelper.setPrivateValue(RenderPlayer.class, render, renderer.modelBipedMain, 1);
		ReflectionHelper.setPrivateValue(RenderPlayer.class, render, renderer.modelArmorChestplate, 2);
		ReflectionHelper.setPrivateValue(RenderPlayer.class, render, renderer.modelArmor, 3);
		MPMRendererHelper.setMainModel(render, renderer.modelBipedMain);
	}

	@SubscribeEvent
	public void special(RenderPlayerEvent.Specials.Pre event){
		if(data.animation == EnumAnimation.BOW){
			float ticks = (event.entityPlayer.ticksExisted - data.animationStart) / 10f;
			if(ticks > 1)
				ticks = 1;
			float scale = (2 - data.body.scaleY);
			GL11.glTranslatef(0, 12 * scale * 0.065f, 0);
			GL11.glRotatef(60 * ticks, 1, 0, 0);
			GL11.glTranslatef(0, -12 * scale * 0.065f, 0);
		}
		if(event.renderCape){
			if(!data.cloakLoaded && RenderEvent.lastCapeTick > RenderEvent.MaxSkinTick){
				data.cloakTexture = renderer.loadCapeResource((AbstractClientPlayer) event.entityPlayer);
				RenderEvent.lastCapeTick = 0;
				data.cloakLoaded = true;
			}
		}
		event.renderItem = false;
		event.renderHelmet = false;
		renderer.renderItem(event.entityPlayer);
		renderer.renderHelmet(event.entityPlayer);
		if(MorePlayerModels.EnableBackItem)
			renderer.renderBackitem(event.entityPlayer);

		GL11.glTranslatef(0, data.getBodyY(), 0); // Cape Fix
	}

	@SubscribeEvent
	public void chat(ClientChatReceivedEvent event){
		if(MorePlayerModels.HasServerSide)
			return;
		try{
			ChatMessages.parseMessage(event.message.getFormattedText());
		}
		catch(Exception ex){
			System.out.println("Cant handle chatmessage: " + event.message + ":" + ex.getMessage());
		}
	}

	@SubscribeEvent
	public void overlay(RenderGameOverlayEvent event){
		if(event.type != ElementType.ALL)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen != null || MorePlayerModels.Tooltips == 0)
			return;
		ItemStack item = mc.thePlayer.getCurrentEquippedItem();
		if(item == null)
			return;

		String name = item.getDisplayName();
		int x = event.resolution.getScaledWidth() - mc.fontRenderer.getStringWidth(name);

		int posX = 4;
		int posY = 4;
		if(MorePlayerModels.Tooltips % 2 == 0)
			posX = x - 4;

		if(MorePlayerModels.Tooltips > 2)
			posY = event.resolution.getScaledHeight() - 24;

		mc.fontRenderer.drawStringWithShadow(name, posX, posY, 0xffffff);
		if(item.isItemStackDamageable()){
			int max = item.getMaxDamage();

			String dam = (max - item.getItemDamage()) + "/" + max;

			x = event.resolution.getScaledWidth() - mc.fontRenderer.getStringWidth(dam);

			if(MorePlayerModels.Tooltips == 2 || MorePlayerModels.Tooltips == 4)
				posX = x - 4;

			mc.fontRenderer.drawStringWithShadow(dam, posX, posY + 12, 0xffffff);
		}
	}

	@SubscribeEvent
	public void selectionBox(DrawBlockHighlightEvent event){
		if(MorePlayerModels.HideSelectionBox)
			event.setCanceled(true);
	}
}
