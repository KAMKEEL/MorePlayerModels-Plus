package noppes.mpm.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.PlayerDataController;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.GuiNpcTextField;
import noppes.mpm.client.gui.util.ITextfieldListener;

public class GuiCreationConfig extends GuiCreationScreenInterface implements ITextfieldListener{
	
	public GuiCreationConfig(){
		hasSaving = false;
		xOffset = 150;
	}

    @Override
    public void initGui() {
    	super.initGui();

		int y = guiTop + 50;

	    addButton(new GuiNpcButton(9, guiLeft + 79, y, 80, 20, new String[]{"gui.default", "config.humanfemale", "config.humanmale", "config.goblinmale"}, playerdata.soundType));
		addLabel(new GuiNpcLabel(5, "config.sounds", guiLeft, y + 5, 0xFFFFFF));

		addTextField(new GuiNpcTextField(52, this, guiLeft + 80, y += 22, 160, 20, playerdata.url));
		addLabel(new GuiNpcLabel(52, "config.skinurl", guiLeft, y + 5, 0xFFFFFF));
		
    	addButton(new GuiNpcButton(46, guiLeft, y += 32, 80, 20, "config.reloadskins"));
    	addButton(new GuiNpcButton(51, guiLeft + 90, y, 80, 20, "config.editbuttons"));

		addButton(new GuiNpcButton(254, guiLeft + 90, y + 22, 50, 20, new String[]{"url.default", "url.full"}, playerdata.urlType));
		addLabel(new GuiNpcLabel(254, "config.urltype", guiLeft, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(48, guiLeft + 90 + 144, y += 22, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnableChatBubbles?1:0));
		addLabel(new GuiNpcLabel(48, "config.chatbubbles", guiLeft + 144, y + 5, 0xFFFFFF));

		addButton(new GuiNpcButton(49, guiLeft + 90, y + 22, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnableBackItem?1:0));
		addLabel(new GuiNpcLabel(49, "config.backitem", guiLeft, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(50, guiLeft + 90 + 144, y += 22, 50, 20, new String[]{"gui.no","1","2","3","4"}, MorePlayerModels.Tooltips));
		addLabel(new GuiNpcLabel(50, "config.tooltip", guiLeft + 144, y + 5, 0xFFFFFF));

		addButton(new GuiNpcButton(57, guiLeft + 90 + 144, y + 22, 50, 20, new String[]{"gui.yes","gui.no"}, MorePlayerModels.HidePlayerNames?1:0));
		addLabel(new GuiNpcLabel(57, "config.names", guiLeft + 144, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(53, guiLeft + 90, y += 22, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnableParticles?1:0));
		addLabel(new GuiNpcLabel(53, "config.particles", guiLeft, y + 5, 0xFFFFFF));

		addButton(new GuiNpcButton(56, guiLeft + 90 + 144, y + 22, 50, 20, new String[]{"gui.yes","gui.no"}, MorePlayerModels.HideSelectionBox?1:0));
		addLabel(new GuiNpcLabel(56, "config.blockhighlight", guiLeft + 144, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(47, guiLeft + 90, y += 22, 50, 20, new String[]{"gui.no","gui.yes"}, MorePlayerModels.EnablePOV?1:0));
		addLabel(new GuiNpcLabel(47, "config.pov", guiLeft, y + 5, 0xFFFFFF));
	}

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(!(btn instanceof GuiNpcButton))
    		return;
    	GuiNpcButton button = (GuiNpcButton) btn;
    	if(button.id == 9){
    		playerdata.soundType = (short) button.getValue();
    	}
    	if(button.id == 46){
    		List<EntityPlayer> players = mc.theWorld.playerEntities;
    		for(EntityPlayer player : players){
    			ModelData data = PlayerDataController.instance.getPlayerData(player);
    			data.playerLoaded = false;
				data.cloakLoaded = false;
    			data.loaded = false;
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
    	if(button.id == 53){
    		MorePlayerModels.EnableParticles = button.getValue() == 1;
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
    	if(button.id == 56){
    		MorePlayerModels.HideSelectionBox = button.getValue() == 1;
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
    	if(button.id == 57){
    		MorePlayerModels.HidePlayerNames = button.getValue() == 1;
    		MorePlayerModels.instance.configLoader.updateConfig();
    	}
		if(button.id == 254){
			playerdata.urlType = (byte) button.getValue();
			playerdata.loaded = false;
		}
    }
    

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if(guiNpcTextField.id == 52){
			playerdata.url = guiNpcTextField.getText();
			if(playerdata.url == ""){
				playerdata.playerLoaded = false;
			}
			playerdata.loaded = false;
		}
	}
}
