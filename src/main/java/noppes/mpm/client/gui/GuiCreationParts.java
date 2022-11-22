package noppes.mpm.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.gui.util.GuiButtonBiDirectional;
import noppes.mpm.client.gui.util.GuiColorButton;
import noppes.mpm.client.gui.util.GuiCustomScroll;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcButtonYesNo;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.GuiNpcTextField;
import noppes.mpm.client.gui.util.ICustomScrollListener;
import noppes.mpm.client.gui.util.ITextfieldListener;
import noppes.mpm.constants.EnumParts;

public class GuiCreationParts extends GuiCreationScreenInterface implements ITextfieldListener, ICustomScrollListener{	
	private GuiCustomScroll scroll;
	
	private GuiPart[] parts = {
			new GuiPart(EnumParts.EARS).setTypes(new String[]{"gui.none", "gui.normal", "ears.bunny"}), 
			new GuiPartHorns(), 
			new GuiPartHair(), 
			new GuiPart(EnumParts.MOHAWK).setTypes(new String[]{"gui.none", "1", "2"}).noPlayerOptions(), 
			new GuiPartSnout(), 
			new GuiPartBeard(), 
			new GuiPart(EnumParts.FIN).setTypes(new String[]{"gui.none", "fin.shark", "fin.reptile"}), 
			new GuiPart(EnumParts.BREASTS).setTypes(new String[]{"gui.none", "1", "2", "3"}).noPlayerOptions(), 
			new GuiPart(EnumParts.WINGS).setTypes(new String[]{"gui.no","gui.player","1","2","3","4","5","6","7","8","9",
					"10","11","12","13","14","15"}),
			new GuiPartClaws(), 
			new GuiPart(EnumParts.SKIRT).setTypes(new String[]{"gui.none", "gui.normal"}).noPlayerOptions(), 
			new GuiPartLegs(), 
			new GuiPartTail()
		};

	private static int selected = 0;
	
	public GuiCreationParts(){
		active = 2;
		Arrays.sort(parts, new Comparator<GuiPart>(){

			@Override
			public int compare(GuiPart o1, GuiPart o2) {
				String s1 = StatCollector.translateToLocal("part." + o1.part.name);
				String s2 = StatCollector.translateToLocal("part." + o2.part.name);
				return s1.compareToIgnoreCase(s2);
			}
			
		});
	}
	
    @Override
    public void initGui() {
    	super.initGui();
    	if(entity != null){
    		openGui(new GuiCreationExtra());
    		return;
    	}
    	
    	if(scroll == null){
    		List<String> list = new ArrayList<String>();
    		for(GuiPart part : parts)
    			list.add(StatCollector.translateToLocal("part." + part.part.name));
    		scroll = new GuiCustomScroll(this, 0);
    		scroll.setUnsortedList(list);
    	}
    	scroll.guiLeft = guiLeft;
    	scroll.guiTop = guiTop + 46;
    	scroll.setSize(100, ySize - 74);
    	
    	addScroll(scroll);
    	

    	if(parts[selected] != null){
    		scroll.setSelected(StatCollector.translateToLocal("part." + parts[selected].part.name));
    		parts[selected].initGui();
    	}
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(parts[selected] != null){
    		parts[selected].actionPerformed(btn);
    	}
    }
    
	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 23){
			
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if(scroll.selected >= 0){
			selected = scroll.selected;		
			initGui();
		}
	}
	class GuiPart{
		EnumParts part;
		private int paterns = 0;
		protected String[] types = {"gui.none"};
		protected ModelPartData data;
		protected boolean hasPlayerOption = true;
		protected boolean noPlayerTypes = false;
		protected boolean canBeDeleted = true;
		
		public GuiPart(EnumParts part){
			this.part = part;
			data = playerdata.getPartData(part);
		}
		
		public int initGui(){
			data = playerdata.getPartData(part);
			int y = guiTop + 50;
			if(data == null || !data.playerTexture || !noPlayerTypes){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(20, "gui.type", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(20, guiLeft + 145, y, 100, 20, types, data == null?0:data.type + 1));
				y += 25;
			}
			if(data != null && hasPlayerOption){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(21, "gui.playerskin", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiNpcButtonYesNo(21, guiLeft + 170, y, data.playerTexture));
				 y += 25;
			}
			if(data != null && !data.playerTexture){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(23, "gui.color", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiColorButton(23, guiLeft + 170, y, data.color));
				 y += 25;
			}
			return y;
		}
		
	    protected void actionPerformed(GuiButton btn) {
	    	if(btn.id == 20){
	    		int i = ((GuiNpcButton)btn).getValue();
	    		if(i == 0 && canBeDeleted)
	    			playerdata.removePart(part);
	    		else{
	    			data = playerdata.getOrCreatePart(part);
	    			data.pattern = 0;
	    			data.setType(i - 1);
	    		}
	    		GuiCreationParts.this.initGui();
	    	}
	    	if(btn.id == 22){
    			data.pattern = (byte) ((GuiNpcButton)btn).getValue();
	    	}
	    	if(btn.id == 21){
	    		data.playerTexture = ((GuiNpcButtonYesNo)btn).getBoolean();
	    		GuiCreationParts.this.initGui();
	    	}
	    	if(btn.id == 23){
	    		setSubGui(new GuiModelColor(GuiCreationParts.this, data));
	    	}
	    }
	    public GuiPart noPlayerOptions(){
	    	hasPlayerOption = false;
	    	return this;
	    }
	    
	    public GuiPart noPlayerTypes(){
	    	noPlayerTypes = true;
	    	return this;
	    }
		
		public GuiPart setTypes(String[] types){
			this.types = types;
			return this;
		}
	}
	class GuiPartTail extends GuiPart{
		public GuiPartTail() {
			super(EnumParts.TAIL);
			types = new String[]{"gui.none","gui.player", "tail.player_dragon", "tail.cat", "tail.wolf", "tail.horse", "tail.dragon", "tail.squirrel", "tail.fin", "tail.rodent", "tail.feather"};
		}

		@Override
		public int initGui(){
			data = playerdata.getPartData(part);
			hasPlayerOption = data != null && (data.type == 0 || data.type == 1 || data.type == 6 || data.type == 7);
			int y = super.initGui();
			if(data != null && data.type == 0){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"1","2"}, data.pattern));
			}
			return y;
		}
	}
	
	class GuiPartHorns extends GuiPart{
		public GuiPartHorns() {
			super(EnumParts.HORNS);
			types = new String[]{"gui.none", "horns.bull", "horns.antlers", "horns.antenna"};
		}

		@Override
		public int initGui(){
			int y = super.initGui();
			if(data != null && data.type == 2){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"1","2"}, data.pattern));
			}
			return y;
		}
	}
	class GuiPartHair extends GuiPart{
		public GuiPartHair() {
			super(EnumParts.HAIR);
			types = new String[]{"gui.none", "1", "2", "3", "4"};
			noPlayerTypes();
		}
	}
	class GuiPartSnout extends GuiPart{
		public GuiPartSnout() {
			super(EnumParts.SNOUT);
			types = new String[]{"gui.none", "snout.small", "snout.medium", "snout.large", "snout.bunny", "snout.beak"};
		}
	}
	class GuiPartBeard extends GuiPart{
		public GuiPartBeard() {
			super(EnumParts.BEARD);
			types = new String[]{"gui.none", "1", "2", "3", "4"};
			noPlayerTypes();
		}
	}
	class GuiPartClaws extends GuiPart{
		public GuiPartClaws() {
			super(EnumParts.CLAWS);
			types = new String[]{"gui.none", "gui.show"};
		}
		
		@Override
		public int initGui(){
			int y = super.initGui();
			if(data == null)
				return y;
			GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
			GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"gui.both","gui.left","gui.right"}, data.pattern));
			return y;
		}
	}
	class GuiPartLegs extends GuiPart{
		public GuiPartLegs() {
			super(EnumParts.LEGS);
			types = new String[]{"gui.no","gui.player", "legs.spider",
					"legs.horse","legs.naga", "legs.mermaid", "Mermaid 2", "legs.digitigrade"};
			canBeDeleted = false;
		}
		@Override
		public int initGui(){
			hasPlayerOption = data.type == 4 || data.type == 7;
			return super.initGui();
		}

		@Override
	    protected void actionPerformed(GuiButton btn) {
	    	if(btn.id == 20){
	    		int i = ((GuiNpcButton)btn).getValue();
	    		if(i <= 1)
	    			data.playerTexture = true;
	    		else
	    			data.playerTexture = false;
	    	}
	    	super.actionPerformed(btn);
	    }
	}
}
