package noppes.mpm.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.mpm.ModelData;
import noppes.mpm.client.Preset;
import noppes.mpm.client.PresetController;
import noppes.mpm.client.gui.util.GuiInterface;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcTextField;

public class GuiPresetSave extends GuiInterface{
	private ModelData data;
	private GuiScreen parent;
	
	public GuiPresetSave(GuiScreen parent, ModelData data){
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
    		preset.data = data.copy();
    		PresetController.instance.addPreset(preset);
    	}
    	mc.displayGuiScreen(parent);
    }
}
