package noppes.mpm.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.mpm.client.Preset;
import noppes.mpm.client.PresetController;
import noppes.mpm.client.gui.util.GuiCustomScroll;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.ICustomScrollListener;

import java.util.ArrayList;
import java.util.List;

public class GuiCreationLoad extends GuiCreationScreenInterface implements ICustomScrollListener{

	private List<String> list = new ArrayList<String>();
	private GuiCustomScroll scroll;
	
	public GuiCreationLoad(){
		active = 5;
		xOffset = 60;
		PresetController.instance.load();
	}

    @Override
    public void initGui() {
    	super.initGui();
    	if(scroll == null){
    		scroll = new GuiCustomScroll(this, 0);
    	}
    	list.clear();
        for(Preset preset : PresetController.instance.presets.values())
        	list.add(preset.name);
		scroll.setList(list);
    	scroll.guiLeft = guiLeft;
    	scroll.guiTop = guiTop + 45;
    	scroll.setSize(100, ySize - 96);
    	
    	addScroll(scroll);
    	
    	addButton(new GuiNpcButton(10, guiLeft, guiTop + ySize - 46, 120, 20, "gui.remove"));
		addButton(new GuiNpcButton(310, guiLeft + 124, guiTop + ySize - 24, 60, 20, "gui.defaults"));
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(btn.id == 10 && scroll.hasSelected()){
    		PresetController.instance.removePreset(scroll.getSelected());
    		initGui();
    	}
		if(btn.id == 310){
			PresetController.instance.addDefaults();
			initGui();
		}
    }

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
    	Preset preset = PresetController.instance.getPreset(scroll.getSelected());
    	playerdata.setNBT(preset.data.getNBT());
		initGui();
	}
}
