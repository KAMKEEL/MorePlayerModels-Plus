package noppes.mpm.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.mpm.client.Preset;
import noppes.mpm.client.PresetController;
import noppes.mpm.client.gui.util.GuiCustomScroll;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.ICustomScrollListener;
import noppes.mpm.config.ConfigMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiCreationLoad extends GuiCreationScreenInterface implements ICustomScrollListener{

	private List<String> list = new ArrayList<String>();
	private HashMap<String, String> nameToFile = new HashMap<String, String>();

	private GuiCustomScroll scroll;
	
	public GuiCreationLoad(){
		active = 5;
		xOffset = 60;
		if(!PresetController.Instance.loaded){
			PresetController.Instance.loadAllPresets();
			PresetController.Instance.loaded = true;
		}
	}

    @Override
    public void initGui() {
    	super.initGui();
    	if(scroll == null){
    		scroll = new GuiCustomScroll(this, 0);
    	}
    	list.clear();
		nameToFile.clear();
        for(Preset preset : PresetController.Instance.presets.values()){
			list.add(preset.name);
			nameToFile.put(preset.name, preset.fileName);
		}
		scroll.setList(list);
    	scroll.guiLeft = guiLeft;
    	scroll.guiTop = guiTop + 45;
    	scroll.setSize(100, ySize - 96);
    	addScroll(scroll);

    	addButton(new GuiNpcButton(10, guiLeft, guiTop + ySize - 46, 120, 20, "gui.remove"));
		addButton(new GuiNpcButton(310, guiLeft + 124, guiTop + ySize - 24, 60, 20, "gui.defaults"));
		if(!ConfigMain.EnablePresetDefaults)
			getButton(310).enabled = false;
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(btn.id == 10 && scroll.hasSelected()){
			String filename = nameToFile.get(scroll.getSelected());
    		PresetController.Instance.removePreset(filename);
    		initGui();
    	}
		if(btn.id == 310){
			PresetController.Instance.addDefaults();
			initGui();
		}
    }

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		String filename = nameToFile.get(scroll.getSelected());
    	Preset preset = PresetController.Instance.getPreset(filename);
		if(preset == null)
			return;

    	playerdata.setNBT(preset.data.getNBT());
		initGui();
	}
}
