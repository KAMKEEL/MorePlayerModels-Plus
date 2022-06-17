package noppes.mpm.client.gui.util;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiInterface extends GuiScreen{
	private HashMap<Integer,GuiNpcButton> buttons = new HashMap<Integer,GuiNpcButton>();
	private HashMap<Integer,GuiNpcLabel> labels = new HashMap<Integer,GuiNpcLabel>();
	private HashMap<Integer,GuiNpcSlider> sliders = new HashMap<Integer,GuiNpcSlider>();
	private HashMap<Integer,GuiNpcTextField> textfields = new HashMap<Integer,GuiNpcTextField>();

	public boolean drawDefaultBackground = false;

	public int guiLeft, guiTop, xSize = 256, ySize = 216;
	
	public void initGui(){
		super.initGui();
    	guiLeft = (width - xSize) / 2;
    	guiTop = (height - ySize) / 2;
    	
    	buttonList.clear();
		labels.clear();
		buttons.clear();
		textfields.clear();
	}

	@Override
    protected void actionPerformed(GuiButton btn) {
    	
    }

    public void addButton(GuiNpcButton button){
    	buttons.put(button.id,button);
    	buttonList.add(button);
    }
	public GuiNpcButton getButton(int i) {
		return buttons.get(i);
	}

    public void addLabel(GuiNpcLabel label) {
		labels.put(label.id, label);
	}
    public GuiNpcLabel getLabel(int i){
    	return labels.get(i);
    }
    public void addSlider(GuiNpcSlider slider){
		sliders.put(slider.id,slider);
    	buttonList.add(slider);
    }
	public GuiNpcSlider getSlider(int i) {
		return sliders.get(i);
	}
	
    public void addTextField( GuiNpcTextField tf){
    	textfields.put(tf.id,tf);
    }
    public GuiNpcTextField getTextField(int i){
    	return textfields.get(i);
    }
	@Override
    public void updateScreen(){
    	for(GuiNpcTextField tf : textfields.values())
    		if(tf.enabled)
    			tf.updateCursorCounter();
        super.updateScreen();
    }
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {    	
		if(drawDefaultBackground)
			this.drawBackground(0); //drawbackground
        for(GuiNpcLabel label : labels.values())
        	label.drawLabel(this, this.fontRendererObj);
    	for(GuiNpcTextField tf : textfields.values())
    		tf.drawTextBox();
        
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void keyTyped(char c, int i)
    {
    	for(GuiNpcTextField tf : textfields.values())
    		tf.textboxKeyTyped(c, i);
    }

	@Override
    public void mouseClicked(int i, int j, int k)
    {
    	for(GuiNpcTextField tf : textfields.values().toArray(new GuiNpcTextField[textfields.size()]))
    		if(tf.enabled)
    			tf.mouseClicked(i, j, k);
    	super.mouseClicked(i, j, k);
    }
}
