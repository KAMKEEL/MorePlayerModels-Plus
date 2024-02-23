package noppes.mpm;

import cpw.mods.fml.common.FMLCommonHandler;
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
import noppes.mpm.constants.EnumPacketClient;
import noppes.mpm.controllers.ModelDataController;

public class ServerEventHandler {

	@SubscribeEvent
	public void chat(ServerChatEvent event){
		Server.sendToAll(EnumPacketClient.CHAT_EVENT, event.player.getCommandSenderName(), event.message);
	}

	@SubscribeEvent
	public void playerTracking(PlayerEvent.StartTracking event){
		if(!(event.target instanceof EntityPlayer))
			return;
		EntityPlayer target = (EntityPlayer) event.target;
		EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;

		ModelData data = ModelData.getData(target);
		if (data == null)
			return;

		data.resourceInit = false;
		data.textureLocation = null;
		Server.sendData(player, EnumPacketClient.SEND_PLAYER_DATA, target.getCommandSenderName(), data.getNBT());
		ItemStack back = player.inventory.mainInventory[0];
		if(back != null)
			Server.sendData(player, EnumPacketClient.BACK_ITEM_UPDATE, target.getCommandSenderName(), back.writeToNBT(new NBTTagCompound()));
		else
			Server.sendData(player, EnumPacketClient.BACK_ITEM_REMOVE, target.getCommandSenderName());
	}
	
	@SubscribeEvent
    public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event)
    {
    	if(!(event.entity instanceof EntityPlayer) || event.name == null || !event.name.equals("game.player.hurt"))
    		return;
    	EntityPlayer player = (EntityPlayer) event.entity;
		ModelData data = ModelData.getData(player);
		if (data == null)
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

		ModelData data = ModelData.getData(player);
		if (data == null)
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

	@SubscribeEvent
	public void onNameSet(PlayerEvent.NameFormat event){
		ModelData data = ModelData.getData(event.entityPlayer);
		if(!data.displayName.isEmpty()){
			event.displayname = data.displayName;
		}
	}
}
