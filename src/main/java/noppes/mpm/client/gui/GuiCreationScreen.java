package noppes.mpm.client.gui;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.mpm.ModelData;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.PlayerDataController;
import noppes.mpm.client.Client;
import noppes.mpm.client.EntityFakeLiving;
import noppes.mpm.client.gui.util.GuiModelInterface;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.GuiNpcTextField;
import noppes.mpm.client.gui.util.ITextfieldListener;
import noppes.mpm.constants.EnumPackets;

import org.lwjgl.input.Keyboard;

public class GuiCreationScreen extends GuiModelInterface implements ITextfieldListener{

	public HashMap<String,Class<? extends EntityLivingBase>> data = new HashMap<String, Class<? extends EntityLivingBase>>();
	private List<String> list;
	private final String[] ignoredTags = {"CanBreakDoors", "Bred", "PlayerCreated", "Tame", "HasReproduced"};
	
	private GuiNpcButton prev,next;
	
	public String hash;
	
	private HashMap<Integer, String> mapped = new HashMap<Integer, String>();
	
	public GuiCreationScreen(){
		hash = playerdata.getHash() + playerdata.extra;
        Map<?,?> mapping = EntityList.stringToClassMapping;
        for(Object name : mapping.keySet()){
        	Class<?> c = (Class<?>) mapping.get(name);
        	try {
        		if(EntityLiving.class.isAssignableFrom(c) && c.getConstructor(new Class[] {World.class}) != null && !Modifier.isAbstract(c.getModifiers())){
        			if(RenderManager.instance.getEntityClassRenderObject(c) instanceof RendererLivingEntity)
        				data.put(name.toString(),c.asSubclass(EntityLivingBase.class));
        		}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
			}
        } 
		list = new ArrayList<String>(data.keySet());
		Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
		list.remove("Sheep");
		list.add(0, "Sheep");
		
	}

    @Override
    public void initGui() {
    	super.initGui();
    	Keyboard.enableRepeatEvents(true);
    	String title = "Player";
    	EntityLivingBase entity = playerdata.getEntity(mc.theWorld, mc.thePlayer);
    	xOffset = entity == null?0:50;
    	if(entity != null)
    		title = (String) EntityList.classToStringMapping.get(playerdata.getEntityClass());
    	this.addButton(new GuiNpcButton(1, guiLeft + 140, guiTop + 4, 100, 20, title));

    	this.addButton(prev = new GuiNpcButton(0, guiLeft + 118, guiTop + 4, 20, 20, "<"));
    	this.addButton(next = new GuiNpcButton(2, guiLeft + 242, guiTop + 4, 20, 20, ">"));
    	prev.enabled = getCurrentEntityIndex() >= 0;
    	next.enabled = getCurrentEntityIndex() < list.size() - 1;

    	addButton(new GuiNpcButton(46, guiLeft + 310, guiTop + 58, 80, 20, "Reload Skins"));
    	addButton(new GuiNpcButton(51, guiLeft + 310, guiTop + 80, 80, 20, "Edit Buttons"));
  	
		addLabel(new GuiNpcLabel(47, "Point of View", guiLeft + 270, guiTop + 139, 0xFFFFFF));
    	addButton(new GuiNpcButton(47, guiLeft + 350, guiTop + 134, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnablePOV?1:0));
	
		addLabel(new GuiNpcLabel(48, "Chatbubbles", guiLeft + 270, guiTop + 161, 0xFFFFFF));
    	addButton(new GuiNpcButton(48, guiLeft + 350, guiTop + 156, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnableChatBubbles?1:0));

		addLabel(new GuiNpcLabel(49, "BackItem", guiLeft + 270, guiTop + 183, 0xFFFFFF));
    	addButton(new GuiNpcButton(49, guiLeft + 350, guiTop + 178, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnableBackItem?1:0));

		addLabel(new GuiNpcLabel(50, "Tooltip", guiLeft + 270, guiTop + 205, 0xFFFFFF));
    	addButton(new GuiNpcButton(50, guiLeft + 350, guiTop + 200, 50, 20, new String[]{"gui.no","1","2","3","4"}, MorePlayerModels.Tooltips));

    	if(entity == null)
    		showPlayerButtons();
    	else
    		showEntityButtons(entity);
    }
    
    private void showPlayerButtons() {
		int y = guiTop ;
    	
    	addButton(new GuiNpcButton(8, guiLeft + 4, y += 22, 96, 20, "Scale"));

		addButton(new GuiNpcButton(250, guiLeft + 50, y += 22, 50, 20, new String[]{"Default","Steve64","Alex"}, playerdata.modelType));
		addLabel(new GuiNpcLabel(250, "Model", guiLeft, y + 5, 0xFFFFFF));

    	addButton(new GuiNpcButton(4, guiLeft + 50, y += 22, 50, 20, "selectServer.edit"));
		addLabel(new GuiNpcLabel(1, "Head", guiLeft, y + 5, 0xFFFFFF));

    	addButton(new GuiNpcButton(5, guiLeft + 50, y += 22, 50, 20, "selectServer.edit"));
		addLabel(new GuiNpcLabel(2, "Body", guiLeft, y + 5, 0xFFFFFF));
    	
    	addButton(new GuiNpcButton(6, guiLeft + 50, y += 22, 50, 20, "selectServer.edit"));
		addLabel(new GuiNpcLabel(3, "Arms", guiLeft, y + 5, 0xFFFFFF));

    	addButton(new GuiNpcButton(7, guiLeft + 50, y += 22, 50, 20, "selectServer.edit"));
		addLabel(new GuiNpcLabel(4, "Legs", guiLeft, y + 5, 0xFFFFFF));
		
	    addButton(new GuiNpcButton(9, guiLeft + 50, y += 22, 50, 20, new String[]{"Default", "Female", "Male", "Goblin Male"}, playerdata.soundType));
		addLabel(new GuiNpcLabel(5, "Sounds", guiLeft, y + 5, 0xFFFFFF));
		
    	addButton(new GuiNpcButton(44, guiLeft + 310, guiTop + 14, 80, 20, "Save Settings"));
    	addButton(new GuiNpcButton(45, guiLeft + 310, guiTop + 36, 80, 20, "Load Settings"));
		addLabel(new GuiNpcLabel(52, "Skin Url", guiLeft, guiTop + 183, 0xFFFFFF));
		addTextField(new GuiNpcTextField(52, this, guiLeft, guiTop + 193, 140, 20, playerdata.url));

		addLabel(new GuiNpcLabel(251, "URL Type", guiLeft + 270, guiTop + 117, 0xFFFFFF));
		addButton(new GuiNpcButton(251, guiLeft + 350, guiTop + 112, 50, 20, new String[]{"Default", "Full"}, playerdata.urlType));
	}

	private void showEntityButtons(EntityLivingBase entity) {
		mapped.clear();
		int y = guiTop + 20;
		
		NBTTagCompound compound = getExtras(entity);
		Set<String> keys = compound.func_150296_c();
		int i = 0;
		for(String name : keys){
			if(isIgnored(name))
				continue;
			NBTBase base = compound.getTag(name);
			if(name.equals("Age")){
				i++;
				addLabel(new GuiNpcLabel(0, "Child", guiLeft, y + 5 + i * 22, 0xFFFFFF));
		    	addButton(new GuiNpcButton(30, guiLeft + 80, y + i * 22, 50, 20, new String[]{"gui.no","gui.yes"},entity.isChild()?1:0));
			}
			else if(base.getId() == 1){
				byte b = ((NBTTagByte)base).func_150290_f();
				if(b != 0 && b != 1)
					continue;
				if(playerdata.extra.hasKey(name))
					b = playerdata.extra.getByte(name);
				i++;
				addLabel(new GuiNpcLabel(100 + i, name, guiLeft, y + 5 + i * 22, 0xFFFFFF));
		    	addButton(new GuiNpcButton(100 + i, guiLeft + 80, y + i * 22, 50, 20, new String[]{"gui.no","gui.yes"}, b));
		    	
		    	mapped.put(i, name);
			}
		}
		if(EntityList.getEntityString(entity).equals("doggystyle.Dog")){
			int breed = 0;
			try {
				Method method = entity.getClass().getMethod("getBreedID");
				breed = (Integer) method.invoke(entity);
			} catch (Exception e) {
				
			}
			i++;
			addLabel(new GuiNpcLabel(201, "Breed", guiLeft, y + 5 + i * 22, 0xFFFFFF));
	    	addButton(new GuiNpcButton(201, guiLeft + 80, y + i * 22, 50, 20, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"}, breed));
		}
	}

	private boolean isIgnored(String tag){
		for(String s : ignoredTags)
			if(s.equals(tag))
				return true;
		return false;
	}

	private NBTTagCompound getExtras(EntityLivingBase entity) {
		NBTTagCompound fake = new NBTTagCompound();
		try{
			new EntityFakeLiving(entity.worldObj).writeEntityToNBT(fake);
		}
		catch(Exception e){
			
		}
		NBTTagCompound compound = new NBTTagCompound();
		entity.writeEntityToNBT(compound);
		Set<String> keys = fake.func_150296_c();
		for(String name : keys)
			compound.removeTag(name);
		
		return compound;
	}

	private int getCurrentEntityIndex(){
    	return list.indexOf(EntityList.classToStringMapping.get(playerdata.getEntityClass()));
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	GuiNpcButton button = (GuiNpcButton) btn;
    	if(button.id == 0){
    		int index = getCurrentEntityIndex();
    		if(!prev.enabled)
    			return;
    		index--;
    		if(index < 0)
    			playerdata.setEntityClass(null);
    		else
    			playerdata.setEntityClass(data.get(list.get(index)));
    		initGui();
    	}
    	if(button.id == 2){
    		int index = getCurrentEntityIndex();
    		if(!next.enabled)
    			return;
    		index++;
    		playerdata.setEntityClass(data.get(list.get(index)));
    		
    		initGui();
    	}
    	
    	if(button.id == 1){
            this.mc.displayGuiScreen(new GuiEntitySelection(this, playerdata));
    	}

    	if(button.id == 4){
            this.mc.displayGuiScreen(new GuiModelHead(this));
    	}
    	if(button.id == 5){
            this.mc.displayGuiScreen(new GuiModelBody(this));
    	}
    	if(button.id == 6){
            this.mc.displayGuiScreen(new GuiModelArms(this));
    	}
    	if(button.id == 7){
            this.mc.displayGuiScreen(new GuiModelLegs(this));
    	}
    	if(button.id == 8){
            this.mc.displayGuiScreen(new GuiModelScale(this, playerdata));
    	}
    	if(button.id == 9){
    		playerdata.soundType = (short) button.getValue();
    	}
    	if(button.id == 30){
    		playerdata.extra.setInteger("Age",button.getValue() == 1?-24000:0);
    		playerdata.clearEntity();
    	}
    	
    	if(button.id == 44){
            this.mc.displayGuiScreen(new GuiPresetSave(this, playerdata));
    	}

    	if(button.id == 45){
            this.mc.displayGuiScreen(new GuiPresetSelection(this, playerdata));
    	}
    	if(button.id == 46){
    		List<EntityPlayer> players = mc.theWorld.playerEntities;
    		for(EntityPlayer player : players){
    			ModelData data = PlayerDataController.instance.getPlayerData(player);
    	    	data.loaded = false;
				data.playerLoaded = false;
    		}
    	}
    	if(button.id == 47){
    		MorePlayerModels.EnablePOV = button.getValue() == 1;
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
    	if(button.id == 48){
    		MorePlayerModels.EnableChatBubbles = button.getValue() == 1;
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
    	if(button.id == 49){
    		MorePlayerModels.EnableBackItem = button.getValue() == 1;
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
    	if(button.id == 50){
    		MorePlayerModels.Tooltips = button.getValue();
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
    	if(button.id == 51){
            this.mc.displayGuiScreen(new GuiEditButtons(this));
    	}
    	if(button.id >= 100 && button.id < 200){
    		String name = mapped.get(button.id - 100);
    		if(name != null){
	    		playerdata.extra.setBoolean(name, button.getValue() == 1);
	    		playerdata.clearEntity();
    		}
    	}
    	if(button.id == 201){
	    	EntityLivingBase entity = playerdata.getEntity(mc.theWorld, mc.thePlayer);
	    	playerdata.setExtra(entity, "breed", ((GuiNpcButton)button).getValue() + "");
    	}
    	if(button.id == 250){
			if(playerdata.modelType == 0){
				playerdata.modelType = 1;
				playerdata.urlType = 1;
			}
			else if(playerdata.modelType == 1){
				playerdata.modelType = 2;
				playerdata.urlType = 1;
			}
			else {
				playerdata.bodywear = 0;
				playerdata.armwear = 0;
				playerdata.legwear = 0;
				playerdata.modelType = 0;
				playerdata.urlType = 0;
			}
		}
		if(button.id == 251){
			playerdata.urlType = (byte) button.getValue();
		}
    }
    
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void close(){
    	super.close();
    	//if(!hash.equals(playerdata.getHash() + playerdata.extra)){
			playerdata.loaded = false;
			playerdata.playerLoaded = false;
    		PlayerDataController.instance.savePlayerData(this.mc.thePlayer, playerdata);
    		Client.sendData(EnumPackets.UPDATE_PLAYER_DATA, playerdata.writeToNBT());
    	//}
    }

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		playerdata.url = guiNpcTextField.getText();
		playerdata.loaded = false;
	}
}
