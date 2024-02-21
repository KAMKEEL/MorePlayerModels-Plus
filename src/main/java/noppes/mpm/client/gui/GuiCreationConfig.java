package noppes.mpm.client.gui;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;
import noppes.mpm.client.controller.ClientCacheController;
import noppes.mpm.client.controller.ClientDataController;
import noppes.mpm.client.controller.ClientPermController;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.GuiNpcTextField;
import noppes.mpm.client.gui.util.ITextfieldListener;
import noppes.mpm.config.ConfigClient;

import java.util.List;

public class GuiCreationConfig extends GuiCreationScreenInterface implements ITextfieldListener{
	
	public GuiCreationConfig(){
		hasSaving = false;
		xOffset = 150;
		active = 0;
	}

    @Override
    public void initGui() {
    	super.initGui();
		int y = guiTop + 50;

		addButton(new GuiNpcButton(9, guiLeft + 79, y, 80, 20, new String[]{"gui.default", "config.humanfemale", "config.humanmale", "config.goblinmale"}, playerdata.soundType));
		addLabel(new GuiNpcLabel(5, "config.sounds", guiLeft, y + 5, 0xFFFFFF));
		if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_SOUND))
			getButton(9).enabled = false;

		addTextField(new GuiNpcTextField(52, this, guiLeft + 80, y += 22, 160, 20, playerdata.url));
		addLabel(new GuiNpcLabel(52, "config.skinurl", guiLeft, y + 5, 0xFFFFFF));
		if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_SKIN))
			getTextField(52).enabled = false;

		addButton(new GuiNpcButton(46, guiLeft, y += 32, 80, 20, "config.reloadskins"));
		addButton(new GuiNpcButton(51, guiLeft + 90, y, 80, 20, "config.editbuttons"));
		if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.EMOTE))
			getButton(51).enabled = false;

		addLabel(new GuiNpcLabel(254, "config.urltype", guiLeft, y + 27, 0xFFFFFF));
		addButton(new GuiNpcButton(254, guiLeft + 90, y + 22, 50, 20, new String[]{"url.default", "url.full"}, playerdata.urlType));
		if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_SKIN))
			getButton(254).enabled = false;

		addButton(new GuiNpcButton(48, guiLeft + 90 + 144, y += 22, 50, 20, new String[]{"gui.no","gui.yes"}, ConfigClient.EnableChatBubbles?1:0));
		addLabel(new GuiNpcLabel(48, "config.chatbubbles", guiLeft + 144, y + 5, 0xFFFFFF));

		addButton(new GuiNpcButton(49, guiLeft + 90, y + 22, 50, 20, new String[]{"gui.no","gui.yes"}, ConfigClient.EnableBackItem?1:0));
		addLabel(new GuiNpcLabel(49, "config.backitem", guiLeft, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(50, guiLeft + 90 + 144, y += 22, 50, 20, new String[]{"gui.no","1","2","3","4"}, ConfigClient.Tooltips));
		addLabel(new GuiNpcLabel(50, "config.tooltip", guiLeft + 144, y + 5, 0xFFFFFF));

		addButton(new GuiNpcButton(57, guiLeft + 90 + 144, y + 22, 50, 20, new String[]{"gui.yes","gui.no"}, ConfigClient.HidePlayerNames?1:0));
		addLabel(new GuiNpcLabel(57, "config.names", guiLeft + 144, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(53, guiLeft + 90, y += 22, 50, 20, new String[]{"gui.no","gui.yes"}, ConfigClient.EnableParticles?1:0));
		addLabel(new GuiNpcLabel(53, "config.particles", guiLeft, y + 5, 0xFFFFFF));

		addButton(new GuiNpcButton(56, guiLeft + 90 + 144, y + 22, 50, 20, new String[]{"gui.yes","gui.no"}, ConfigClient.HideSelectionBox?1:0));
		addLabel(new GuiNpcLabel(56, "config.blockhighlight", guiLeft + 144, y + 27, 0xFFFFFF));

		addButton(new GuiNpcButton(47, guiLeft + 90, y += 22, 50, 20, new String[]{"gui.no","gui.yes"}, ConfigClient.EnablePOV?1:0));
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
				ModelData data = ClientDataController.Instance().getPlayerData(player);
				data.textureLocation = null;
				data.resourceInit = false;
    		}
			ClientCacheController.clearSkinData();
		}
    	if(button.id == 47){
			ConfigClient.EnablePOV = button.getValue() == 1;
			ConfigClient.EnablePOVProperty.set(ConfigClient.EnablePOV);
    	}
    	if(button.id == 48){
			ConfigClient.EnableChatBubbles = button.getValue() == 1;
			ConfigClient.EnableChatBubblesProperty.set(ConfigClient.EnableChatBubbles);
    	}
    	if(button.id == 49){
			ConfigClient.EnableBackItem = button.getValue() == 1;
			ConfigClient.EnableBackItemProperty.set(ConfigClient.EnableBackItem);
    	}
    	if(button.id == 50){
			ConfigClient.Tooltips = button.getValue();
			ConfigClient.TooltipsProperty.set(ConfigClient.Tooltips);
    	}
    	if(button.id == 51){
            this.mc.displayGuiScreen(new GuiEditButtons(this));
    	}
    	if(button.id == 53){
			ConfigClient.EnableParticles = button.getValue() == 1;
			ConfigClient.EnableParticlesProperty.set(ConfigClient.EnableParticles);
    	}
    	if(button.id == 56){
			ConfigClient.HideSelectionBox = button.getValue() == 1;
			ConfigClient.HideSelectionBoxProperty.set(ConfigClient.HideSelectionBox);
    	}
    	if(button.id == 57){
			ConfigClient.HidePlayerNames = button.getValue() == 1;
			ConfigClient.HidePlayerNamesProperty.set(ConfigClient.HidePlayerNames);
    	}
		if(button.id == 254){
			playerdata.urlType = (byte) button.getValue();
			playerdata.textureLocation = null;
			playerdata.resourceInit = false;
		}
    }
    

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if(guiNpcTextField.id == 52){
			playerdata.url = guiNpcTextField.getText();
			playerdata.textureLocation = null;
			playerdata.resourceInit = false;
		}
	}
}
