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
import noppes.mpm.ModelData;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.client.data.ClientModelData;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.constants.EnumAnimation;
import org.lwjgl.opengl.GL11;

public class RenderEvent {
	public static RenderEvent Instance;
	public static RenderMPM renderer = new RenderMPM();
	public static long lastSkinTick = -30;
	public static long lastCapeTick = -30;
	public final static long MaxSkinTick = 6;
	private ModelData data;

	private static final Entity hideNameSheep = new EntitySheep(null);

	public RenderEvent(){
		Instance = this;
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void pre(RenderPlayerEvent.Pre event){
		if(ConfigClient.HidePlayerNames && event.entity.riddenByEntity == null){
			event.entity.riddenByEntity = hideNameSheep;
		}
		if(!(event.entity instanceof AbstractClientPlayer))
			return;
		EntityPlayer player = event.entityPlayer;
		data = ClientModelData.Instance().getPlayerData(player);
		renderer.setModelData(data, player);
		setModels(event.renderer);

		if(data.isSleeping()){
			if(data.animation == EnumAnimation.SLEEPING_EAST)
				player.renderYawOffset = player.prevRenderYawOffset = -90;
			if(data.animation == EnumAnimation.SLEEPING_WEST)
				player.renderYawOffset = player.prevRenderYawOffset = 90;
			if(data.animation == EnumAnimation.SLEEPING_NORTH)
				player.renderYawOffset = player.prevRenderYawOffset = 180;
			if(data.animation == EnumAnimation.SLEEPING_SOUTH)
				player.renderYawOffset = player.prevRenderYawOffset = 0;
		}

		if(!data.resourceInit && lastSkinTick > MaxSkinTick){
			renderer.loadResource((AbstractClientPlayer) player);
			lastSkinTick = 0;
			data.resourceInit = true;
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
		if(!(event.entity instanceof AbstractClientPlayer))
			return;

		if(event.entity.riddenByEntity == hideNameSheep){
			event.entity.riddenByEntity = null;
		}

		AbstractClientPlayer player = (AbstractClientPlayer) event.entity;
		data = ClientModelData.Instance().getPlayerData(player);
		if(data.isSleeping()){
			player.renderYawOffset = player.prevRenderYawOffset = player.rotationYaw;
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

	@SubscribeEvent(priority=EventPriority.LOWEST)
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
		event.renderItem = false;
		event.renderHelmet = false;
		renderer.renderItem(event.entityPlayer);
		renderer.renderHelmet(event.entityPlayer);
		if(ConfigClient.EnableBackItem)
			renderer.renderBackitem(event.entityPlayer);
		if(event.renderCape){
			if(!data.cloakInnit && RenderEvent.lastCapeTick > RenderEvent.MaxSkinTick){
				data.cloakObject = renderer.loadCapeResource((AbstractClientPlayer) event.entityPlayer);
				RenderEvent.lastCapeTick = 0;
				data.cloakInnit = true;
			}
		}
		GL11.glTranslatef(0, data.getBodyY(), 0); // Cape Fix
	}

	@SubscribeEvent()
	public void hand(RenderHandEvent event){
		Minecraft mc = Minecraft.getMinecraft();
		data = ClientModelData.Instance().getPlayerData(mc.thePlayer);
		Entity entity = data.getEntity(mc.thePlayer);
		if(entity != null || data.isSleeping() || data.animation == EnumAnimation.BOW && mc.thePlayer.getHeldItem() == null){
			event.setCanceled(true);
			return;
		}
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
		if (ClientCacheHandler.loaded) {
			if(event.type != ElementType.ALL)
				return;

			Minecraft mc = Minecraft.getMinecraft();
			if(mc.currentScreen != null || ConfigClient.Tooltips == 0)
				return;
			ItemStack item = mc.thePlayer.getCurrentEquippedItem();
			if(item == null)
				return;

			String name = item.getDisplayName();
			int x = event.resolution.getScaledWidth() - mc.fontRenderer.getStringWidth(name);

			int posX = 4;
			int posY = 4;
			if(ConfigClient.Tooltips % 2 == 0)
				posX = x - 4;

			if(ConfigClient.Tooltips > 2)
				posY = event.resolution.getScaledHeight() - 24;

			mc.fontRenderer.drawStringWithShadow(name, posX, posY, 0xffffff);
			if(item.isItemStackDamageable()){
				int max = item.getMaxDamage();

				String dam = (max - item.getItemDamage()) + "/" + max;

				x = event.resolution.getScaledWidth() - mc.fontRenderer.getStringWidth(dam);

				if(ConfigClient.Tooltips == 2 || ConfigClient.Tooltips == 4)
					posX = x - 4;

				mc.fontRenderer.drawStringWithShadow(dam, posX, posY + 12, 0xffffff);
			}
		}
	}

	@SubscribeEvent
	public void selectionBox(DrawBlockHighlightEvent event){
		if(ConfigClient.HideSelectionBox)
			event.setCanceled(true);
	}
}
