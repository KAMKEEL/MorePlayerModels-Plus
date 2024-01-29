package noppes.mpm;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
import noppes.mpm.constants.EnumPackets;

public class ServerEventHandler {

	@SubscribeEvent
	public void chat(ServerChatEvent event){
		Server.sendToAll(event.player.mcServer, EnumPackets.CHAT_EVENT, event.player.getUniqueID(), event.message);
	}

	@SubscribeEvent
	public void playerTracking(PlayerEvent.StartTracking event){
		if(!(event.target instanceof EntityPlayer))
			return;
		EntityPlayer target = (EntityPlayer) event.target;
		EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;

		ModelData data = PlayerDataController.instance.getPlayerData(target);
		Server.sendDelayedData(player, EnumPackets.SEND_PLAYER_DATA, 100, target.getUniqueID(), data.writeToNBT());
		ItemStack back = player.inventory.mainInventory[0];
		if(back != null)
			Server.sendDelayedData(player, EnumPackets.BACK_ITEM_UPDATE, 100, target.getUniqueID(), back.writeToNBT(new NBTTagCompound()));
		else
			Server.sendDelayedData(player, EnumPackets.BACK_ITEM_REMOVE, 100, target.getUniqueID());
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
	public void onNameSet(PlayerEvent.NameFormat event){
		ModelData data = PlayerDataController.instance.getPlayerData(event.entityPlayer);
		if(!data.displayName.isEmpty()){
			event.displayname = data.displayName;
		}
	}
}
