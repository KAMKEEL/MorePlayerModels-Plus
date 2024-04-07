package noppes.mpm.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.mpm.ModelData;
import noppes.mpm.client.Preset;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcTextField;
import noppes.mpm.client.gui.util.SubGuiInterface;

public class GuiPresetSave extends SubGuiInterface{
	private ModelData data;
	private GuiCreationScreenInterface parent;
	
	public GuiPresetSave(GuiCreationScreenInterface parent, ModelData data){
		this.data = data;
		this.parent = parent;
		xSize = 200;
		this.drawDefaultBackground = true;
	}
	@Override
	public void initGui(){
		super.initGui();
		this.addTextField(new GuiNpcTextField(0, this, guiLeft, guiTop + 70,200, 20, ""));
		this.addButton(new GuiNpcButton(0,guiLeft, guiTop + 100,98, 20, "Save"));
		this.addButton(new GuiNpcButton(1,guiLeft + 100, guiTop + 100, 98, 20, "Cancel"));
	}
    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	GuiNpcButton button = (GuiNpcButton) btn;
    	if(button.id == 0){
    		String name = this.getTextField(0).getText().trim();
    		if(name.isEmpty())
    			return;
    		Preset preset = new Preset();
    		preset.name = name;
			preset.fileName = name.replaceAll("[^a-zA-Z0-9_-]", "").toLowerCase();
			preset.data = data.copy();
    		preset.save();
    	}
    	close();
    }
}
