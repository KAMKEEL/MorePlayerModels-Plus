package noppes.mpm.client.gui;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import noppes.mpm.client.controller.ClientPermController;
import noppes.mpm.client.gui.util.*;
import noppes.mpm.constants.EnumParts;

import java.util.ArrayList;
import java.util.List;

public class GuiCreationLimbs extends GuiCreationScreenInterface implements ISliderListener, ICustomScrollListener{
	private GuiCustomScroll scroll;
	private List<EnumParts> data = new ArrayList<EnumParts>();

	private static EnumParts selected = EnumParts.HEAD;

	private final String[] arrHide = new String[]{"gui.no","gui.yes"};
	private final String[] arrSolid = new String[]{"gui.no","gui.yes","part.solid"};
	private final String[] arrMulti = new String[]{"gui.no","part.both","part.right","part.left"};

	public GuiCreationLimbs(){
		active = 251;
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
		int y = guiTop + 65;
		boolean x64 = false;
		if(playerdata.modelType == 1 || playerdata.modelType == 2){
			x64 = true;
		}
		if(selected == EnumParts.HEAD){
			addLabel(new GuiNpcLabel(140, "gui.hide", guiLeft + 102, y + 5, 0xFFFFFF));
			addButton(new GuiNpcButton(140, guiLeft + 156, y, 70, 20, arrHide, playerdata.hideHead));
			if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_HIDE))
				getButton(140).enabled = false;

			y += 22;
			addLabel(new GuiNpcLabel(141, "gui.headwear", guiLeft + 102, y + 5, 0xFFFFFF));
			addButton(new GuiNpcButton(141, guiLeft + 156, y, 70, 20, arrSolid, playerdata.headwear));
		}
		else if(selected == EnumParts.BODY){
			addLabel(new GuiNpcLabel(142, "gui.hide", guiLeft + 102, y + 5, 0xFFFFFF));
			addButton(new GuiNpcButton(142, guiLeft + 156, y, 70, 20, arrHide, playerdata.hideBody));
			if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_HIDE))
				getButton(142).enabled = false;

			if(x64){
				y += 22;
				addLabel(new GuiNpcLabel(143, "gui.bodywear", guiLeft + 102, y + 5, 0xFFFFFF));
				addButton(new GuiNpcButton(143, guiLeft + 156, y, 70, 20, arrSolid, playerdata.bodywear));
			}
		}
		else if(selected == EnumParts.ARMS){
			addLabel(new GuiNpcLabel(144, "gui.hide", guiLeft + 102, y + 5, 0xFFFFFF));
			addButton(new GuiNpcButton(144, guiLeft + 156, y, 70, 20, arrMulti, playerdata.hideArms));
			if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_HIDE))
				getButton(144).enabled = false;

			if(x64){
				y += 22;
				addLabel(new GuiNpcLabel(145, "gui.armwear", guiLeft + 102, y + 5, 0xFFFFFF));
				addButton(new GuiNpcButton(145, guiLeft + 156, y, 70, 20, arrMulti, playerdata.armwear));
				y += 22;
				addLabel(new GuiNpcLabel(146, "gui.solid", guiLeft + 102, y + 5, 0xFFFFFF));
				addButton(new GuiNpcButton(146, guiLeft + 156, y, 70, 20, arrMulti, playerdata.solidArmwear));
			}
		}
		else if(selected == EnumParts.LEGS){
			addLabel(new GuiNpcLabel(147, "gui.hide", guiLeft + 102, y + 5, 0xFFFFFF));
			addButton(new GuiNpcButton(147, guiLeft + 156, y, 70, 20, arrMulti, playerdata.hideLegs));
			if(!ClientPermController.hasPermission(MorePlayerModelsPermissions.CONFIG_HIDE))
				getButton(147).enabled = false;

			if(x64){
				y += 22;
				addLabel(new GuiNpcLabel(148, "gui.legwear", guiLeft + 102, y + 5, 0xFFFFFF));
				addButton(new GuiNpcButton(148, guiLeft + 156, y, 70, 20, arrMulti, playerdata.legwear));
				y += 22;
				addLabel(new GuiNpcLabel(149, "gui.solid", guiLeft + 102, y + 5, 0xFFFFFF));
				addButton(new GuiNpcButton(149, guiLeft + 156, y, 70, 20, arrMulti, playerdata.solidLegwear));
			}
		}
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
		if(btn.id >= 140 && btn.id <= 149){
			GuiNpcButton button = (GuiNpcButton) btn;
			if(button.id == 140){
				playerdata.hideHead = (byte) button.getValue();
			}
			if(button.id == 141){
				playerdata.headwear = (byte) button.getValue();
			}
			if(button.id == 142){
				playerdata.hideBody = (byte) button.getValue();
			}
			if(button.id == 143){
				playerdata.bodywear = (byte) button.getValue();
			}
			if(button.id == 144){
				playerdata.hideArms = (byte) button.getValue();
			}
			if(button.id == 145){
				playerdata.armwear = (byte) button.getValue();
			}
			if(button.id == 146){
				playerdata.solidArmwear = (byte) button.getValue();
			}
			if(button.id == 147){
				playerdata.hideLegs = (byte) button.getValue();
			}
			if(button.id == 148){
				playerdata.legwear = (byte) button.getValue();
			}
			if(button.id == 149){
				playerdata.solidLegwear = (byte) button.getValue();
			}
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
