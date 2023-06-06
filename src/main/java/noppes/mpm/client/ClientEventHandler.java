package noppes.mpm.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MPMEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.mpm.*;
import noppes.mpm.client.fx.EntityEnderFX;
import noppes.mpm.client.fx.EntityRainbowFX;
import noppes.mpm.client.gui.GuiCreationScreenInterface;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPackets;
import noppes.mpm.constants.EnumParts;

import java.util.List;
import java.util.Random;

public class ClientEventHandler {

	public static float partialTicks = 0;
	private World prevWorld;
	private List<EntityPlayer> playerlist;
	private EntityRendererAlt alt;
	private EntityRenderer prevAlt;

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event){
		Minecraft mc = Minecraft.getMinecraft();
		if(mc == null || mc.thePlayer == null)
			return;
		if(ClientProxy.Screen.isPressed()){
			ModelData data = PlayerDataController.instance.getPlayerData(mc.thePlayer);
			data.animation = EnumAnimation.NONE;
			if(mc.currentScreen == null)
				mc.displayGuiScreen(new GuiCreationScreenInterface());
			else if(mc.currentScreen instanceof GuiCreationScreenInterface)
				mc.setIngameFocus();
		}
		if(!mc.inGameHasFocus)
			return;
		if(ClientProxy.Sleep.isPressed()){
			processAnimation(ConfigClient.button1);
		}
		if(ClientProxy.Sit.isPressed()){
			processAnimation(ConfigClient.button2);
		}
		if(ClientProxy.Dance.isPressed()){
			processAnimation(ConfigClient.button3);
		}
		if(ClientProxy.Hug.isPressed()){
			processAnimation(ConfigClient.button4);
		}
		if(ClientProxy.Crawl.isPressed()){
			processAnimation(ConfigClient.button5);
		}
	}

	private void processAnimation(int type) {
		if(type <= 0)
			return;
		if(MorePlayerModels.HasServerSide)
			Client.sendData(EnumPackets.ANIMATION, type);
		else{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			EnumAnimation animation = EnumAnimation.values()[type];
			if(animation == EnumAnimation.SLEEPING_SOUTH){
				float rotation = player.rotationYaw;
				while(rotation < 0)
					rotation += 360;
				while(rotation > 360)
					rotation -= 360;
				int rotate = (int) ((rotation + 45) / 90);
				if(rotate == 1)
					animation = EnumAnimation.SLEEPING_WEST;
				if(rotate == 2)
					animation = EnumAnimation.SLEEPING_NORTH;
				if(rotate == 3)
					animation = EnumAnimation.SLEEPING_EAST;
			}
			ModelData data = PlayerDataController.instance.getPlayerData(player);
			if(data.animationEquals(animation))
				animation = EnumAnimation.NONE;
			data.setAnimation(animation.ordinal());
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
        partialTicks = event.renderTickTime;
		Minecraft mc = Minecraft.getMinecraft();
		if(ConfigClient.EnablePOV){
			if(alt == null)
				alt = new EntityRendererAlt(mc);
    		if(mc.entityRenderer != alt){
    			prevAlt = mc.entityRenderer;
    			mc.entityRenderer = alt;
    		}
		}
		else if(prevAlt != null && mc.entityRenderer != prevAlt){
			mc.entityRenderer = prevAlt;
		}
	}
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.side == Side.SERVER || event.phase == Phase.START)
			return;
    	Minecraft mc = Minecraft.getMinecraft();
    	World world = mc.theWorld;
		if ((this.prevWorld == null || world == null) && this.prevWorld != world) {
			ClientCacheHandler.clearCache();
		}
    	if(world != null && prevWorld != world){
			MorePlayerModels.HasServerSide = false;
			ModelData data = PlayerDataController.instance.getPlayerData(mc.thePlayer);
			Client.sendData(EnumPackets.PING, MorePlayerModels.Revision, data.writeToNBT());
			prevWorld = world;
    	}
    	if(MorePlayerModels.HasServerSide && mc.thePlayer != null && world != null && world.getWorldTime() % 20 == 0){
    		List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, mc.thePlayer.boundingBox.expand(64, 64, 64));
    		for(EntityPlayer player : list){
    			if(player == mc.thePlayer)
    				continue;
    			if(playerlist != null && playerlist.contains(player))
    				continue;
    			ModelData data = PlayerDataController.instance.getPlayerData(player);
    			Client.sendData(EnumPackets.REQUEST_PLAYER_DATA, player.getCommandSenderName(), data.getHash());
    		}
    		playerlist = list;
    	}
    	RenderEvent.lastSkinTick++;
		RenderEvent.lastCapeTick++;
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		if(event.side == Side.SERVER || event.phase == Phase.START)
			return;
    	EntityPlayer player = event.player;
		ModelData data = PlayerDataController.instance.getPlayerData(player);
    	EntityLivingBase entity = data.getEntity(player.worldObj, player);
    	if(entity != null){
    		entity.posY -= player.yOffset;
			MPMEntityUtil.Copy(player, entity);
    		entity.onUpdate();
    	}
        if (data.inLove > 0){
            --data.inLove;
            if(player.getRNG().nextBoolean()){
                String s = "heart";
                double d0 = player.getRNG().nextGaussian() * 0.02D;
                double d1 = player.getRNG().nextGaussian() * 0.02D;
                double d2 = player.getRNG().nextGaussian() * 0.02D;
                player.worldObj.spawnParticle(s, player.posX + player.getRNG().nextFloat() * player.width * 2.0F - player.width, player.posY + 0.5D + player.getRNG().nextFloat() * player.height - player.yOffset, player.posZ + player.getRNG().nextFloat() * player.width * 2.0F - player.width, d0, d1, d2);
            
            }
        }
        if(data.animation == EnumAnimation.CRY){
            float f1 = player.rotationYaw * (float)Math.PI / 180.0F;
            float dx = -MathHelper.sin(f1);
            float dz = MathHelper.cos(f1);
            for (int i = 0; (float)i < 10; ++i){
                float f2 = (player.getRNG().nextFloat() - 0.5f) * player.width * 0.5f + dx * 0.15f;
                float f3 = (player.getRNG().nextFloat() - 0.5f) * player.width * 0.5f + dz * 0.15f;
                player.worldObj.spawnParticle("splash", player.posX + (double)f2, player.posY - data.getBodyY() + 1.1f - player.getYOffset(), player.posZ + (double)f3, 0.0000000000000000000000001f, 0, 0.0000000000000000000000001f);
            }
        }
        if(data.animation != EnumAnimation.NONE)
        	ServerTickHandler.checkAnimation(player, data);
        
        ModelPartData particles = data.getPartData(EnumParts.PARTICLES);
        if(particles != null)
        	spawnParticles(player, data, particles);
	}
	
	private void spawnParticles(EntityPlayer player, ModelData data, ModelPartData particles) {
		if(!ConfigClient.EnableParticles)
			return;
		Minecraft minecraft =  Minecraft.getMinecraft();
		double height = player.getYOffset() + data.getBodyY();
		Random rand = player.getRNG();
		if(particles.type == 2){
			for(int i = 0; i < 2; i++){
				double x = player.posX + (rand.nextDouble() - 0.5D) * 0.9;
				double y = (player.posY + rand.nextDouble() * 1.9) - 0.25D - height;
				double z = player.posZ + (rand.nextDouble() - 0.5D) * 0.9;


				double f = (rand.nextDouble() - 0.5D) * 2D;
				double f1 =  -rand.nextDouble();
				double f2 = (rand.nextDouble() - 0.5D) * 2D;

				minecraft.effectRenderer.addEffect(new EntityRainbowFX(player.worldObj, x, y, z, f, f1, f2));
			}
		} else {
			for(int i = 0; i< 2; i++) {
				EntityEnderFX fx = new EntityEnderFX((AbstractClientPlayer) player, (rand.nextDouble() - 0.5D) * (double) player.width, (rand.nextDouble() * (double) player.height) - height - 0.25D, (rand.nextDouble() - 0.5D) * (double) player.width, (rand.nextDouble() - 0.5D) * 2D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2D, particles);
				minecraft.effectRenderer.addEffect(fx);
			}
		}
	}
}
