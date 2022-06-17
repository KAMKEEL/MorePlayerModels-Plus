package noppes.mpm;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumPackets;

public class ServerEventHandler {

	@SubscribeEvent
	public void chat(ServerChatEvent event){
		Server.sendToAll(EnumPackets.CHAT_EVENT, event.player.getCommandSenderName(), event.message);
	}
	
	@SubscribeEvent
    public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event)
    {
    	if(!(event.entity instanceof EntityPlayer) || event.name == null || !event.name.equals("game.player.hurt"))
    		return;
    	EntityPlayer player = (EntityPlayer) event.entity;
		ModelData data = getModelData(player);
		if(data == null)
			return;
		if(data.soundType == 0)
			return;
    	if(player.getHealth() <= 1 || player.isDead){
    		if(data.soundType == 1)
    			event.name = "moreplayermodels:human.female.death";
    		else if(data.soundType == 2)
    			event.name = "moreplayermodels:human.male.death";
    		else if(data.soundType == 3)
    			event.name = "moreplayermodels:goblin.male.death";
    	}
    	else{
    		if(data.soundType == 1)
    			event.name = "moreplayermodels:human.female.hurt";
    		else if(data.soundType == 2)
    			event.name = "moreplayermodels:human.male.hurt";
    		else if(data.soundType == 3)
    			event.name = "moreplayermodels:goblin.male.hurt";
    	}
    }

	@SubscribeEvent
	public void onAttack(LivingAttackEvent event){
		if(event.entityLiving.worldObj.isRemote || event.ammount < 1 || !(event.source.damageType.equals("player")) || !(event.source.getSourceOfDamage() instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
        boolean flag = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null;
		if(!flag || event.entityLiving.getHealth() < 0 || player.hurtResistantTime > player.maxHurtResistantTime / 2.0F)
			return;

		ModelData data = getModelData(player);
		if(data == null)
			return;
		String sound = "";
		if(data.soundType == 1)
			sound = "moreplayermodels:human.female.attack";
		else if(data.soundType == 2)
			sound = "moreplayermodels:human.male.attack";
		else if(data.soundType == 3)
			sound = "moreplayermodels:goblin.male.attack";

		float pitch = (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.2F + 1.0F;
		player.worldObj.playSoundAtEntity(player, sound,  0.98765432123456789f, pitch);
		
	}

	private ModelData getModelData(EntityLivingBase entityLiving) {
		if(entityLiving == null || !(entityLiving instanceof EntityPlayer))
			return null;
		EntityPlayer player = (EntityPlayer) entityLiving;
		return PlayerDataController.instance.getPlayerData(player);
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		if(event.side == Side.CLIENT)
			return;
    	EntityPlayerMP player = (EntityPlayerMP) event.player;
    	ModelData data = PlayerDataController.instance.getPlayerData(player);
    	ItemStack item = player.inventory.mainInventory[0];
    	if(data.backItem == item)
    		return;
    	if(item == null){
    		Server.sendAssociatedData(player, EnumPackets.BACK_ITEM_REMOVE, player.getCommandSenderName());
    	}
    	else{
    		NBTTagCompound tag = item.writeToNBT(new NBTTagCompound());
    		Server.sendAssociatedData(player, EnumPackets.BACK_ITEM_UPDATE, player.getCommandSenderName(), tag);
    		
    		
    	}
    	data.backItem = item;
        if(data.animation != EnumAnimation.NONE)
        	checkAnimation(player, data);
	}
	public static void checkAnimation(EntityPlayer player, ModelData data){
    	double motionX = player.prevPosX - player.posX;
    	double motionY = player.prevPosY - player.posY;
    	double motionZ = player.prevPosZ - player.posZ;

    	double speed = motionX * motionX +  motionZ * motionZ;
    	boolean isJumping = motionY * motionY > 0.08;

    	if(data.animationTime > 0)
    		data.animationTime--;

    	if(player.isPlayerSleeping() || player.isRiding() || data.animationTime == 0 && data.animation == EnumAnimation.WAVING || data.animation == EnumAnimation.BOW && player.isSneaking())
    		data.animation = EnumAnimation.NONE;
    	    	
    	if(!isJumping && player.isSneaking() && (data.animation == EnumAnimation.HUG || data.animation == EnumAnimation.CRAWLING || 
    			data.animation == EnumAnimation.SITTING || data.animation == EnumAnimation.DANCING))
    		return;

    	if(speed > 0.01 || isJumping || player.isPlayerSleeping() || player.isRiding() || data.isSleeping() && speed > 0.001)
    		data.animation = EnumAnimation.NONE;
	}

	@SubscribeEvent
	public void onNameSet(PlayerEvent.NameFormat event){
		ModelData data = PlayerDataController.instance.getPlayerData(event.entityPlayer);
		if(!data.displayName.isEmpty()){
			event.displayname = data.displayName;
		}
	}
}
