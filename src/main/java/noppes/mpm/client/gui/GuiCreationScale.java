package noppes.mpm.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartConfig;
import noppes.mpm.client.gui.util.GuiCustomScroll;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.GuiNpcSlider;
import noppes.mpm.client.gui.util.ICustomScrollListener;
import noppes.mpm.client.gui.util.ISliderListener;
import noppes.mpm.constants.EnumParts;

public class GuiCreationScale extends GuiCreationScreenInterface implements ISliderListener, ICustomScrollListener{
	private GuiCustomScroll scroll;
	private List<EnumParts> data = new ArrayList<EnumParts>();
	
	private static EnumParts selected = EnumParts.HEAD;
	
	public GuiCreationScale(){
		active = 3;
		xOffset = 140;
	}
	
    @Override
    public void initGui() {
    	super.initGui();
    	if(scroll == null){
    		scroll = new GuiCustomScroll(this, 0);
    	}

		ArrayList list = new ArrayList<String>();
		EnumParts[] parts = {EnumParts.HEAD, EnumParts.BODY, EnumParts.ARMS,
				EnumParts.LEGS};
		data.clear();
		for(EnumParts part : parts){
			data.add(part);
			list.add(StatCollector.translateToLocal("part." + part.name));
		}
		scroll.setUnsortedList(list);
    	scroll.setSelected(StatCollector.translateToLocal("part." + selected.name));
    	scroll.guiLeft = guiLeft;
    	scroll.guiTop = guiTop + 46;
    	scroll.setSize(100, ySize - 74);
    	
    	addScroll(scroll);
    	
    	ModelPartConfig config = playerdata.getPartConfig(selected);
		int y = guiTop + 65;
		addLabel(new GuiNpcLabel(10, "scale.width", guiLeft + 102, y + 5, 0xFFFFFF));
		addSlider(new GuiNpcSlider(this, 10, guiLeft + 150, y, 100, 20, config.scaleX - 0.5f));
		y += 22;
		addLabel(new GuiNpcLabel(11, "scale.height", guiLeft + 102, y + 5, 0xFFFFFF));
		addSlider(new GuiNpcSlider(this, 11, guiLeft + 150, y, 100, 20, config.scaleY - 0.5f));
		y += 22;
		addLabel(new GuiNpcLabel(12, "scale.depth", guiLeft + 102, y + 5, 0xFFFFFF));
		addSlider(new GuiNpcSlider(this, 12, guiLeft + 150, y, 100, 20, config.scaleZ - 0.5f));
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(btn.id == 13){
    		boolean bo = ((GuiNpcButton)btn).getValue() == 0;
    		playerdata.getPartConfig(selected).notShared = bo;
    		initGui();
    	}
    }

	@Override
	public void mouseDragged(GuiNpcSlider slider) {
		super.mouseDragged(slider);
		if(slider.id >= 10 && slider.id <= 12){
			int percent = (int) (50 + slider.sliderValue * 100);
			slider.setString(percent + "%");
			ModelPartConfig config = playerdata.getPartConfig(selected);

			if(slider.id == 10)
				config.scaleX = slider.sliderValue + 0.5f;
			if(slider.id == 11)
				config.scaleY = slider.sliderValue + 0.5f;
			if(slider.id == 12)
				config.scaleZ = slider.sliderValue + 0.5f;
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if(scroll.selected >= 0){
			selected = data.get(scroll.selected);	
			initGui();
		}
	}
}
