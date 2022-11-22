package noppes.mpm.client.gui.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.client.RenderEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiNPCInterface extends GuiScreen
{
	public EntityPlayerSP player;
	public boolean drawDefaultBackground = true;
	private HashMap<Integer,GuiNpcButton> buttons = new HashMap<Integer,GuiNpcButton>();
	private HashMap<Integer,GuiNpcTextField> textfields = new HashMap<Integer,GuiNpcTextField>();
	private HashMap<Integer,GuiNpcLabel> labels = new HashMap<Integer,GuiNpcLabel>();
	private HashMap<Integer,GuiCustomScroll> scrolls = new HashMap<Integer,GuiCustomScroll>();
	private HashMap<Integer,GuiNpcSlider> sliders = new HashMap<Integer,GuiNpcSlider>();
	private HashMap<Integer,GuiScreen> extra = new HashMap<Integer,GuiScreen>();
	public String title;
	protected ResourceLocation background = null;
	public boolean closeOnEsc = false;
	public int guiLeft,guiTop,xSize,ySize;
	private SubGuiInterface subgui;
	public int mouseX, mouseY;
	
    public GuiNPCInterface()
    {
    	this.player = Minecraft.getMinecraft().thePlayer;
    	title = "";
    	xSize = 200;
    	ySize = 222;
    }
    public void setBackground(String texture){
    	background = new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    public ResourceLocation getResource(String texture){
    	return new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    @Override
    public void initGui(){
    	super.initGui();
    	GuiNpcTextField.unfocus();
    	if(subgui != null){
    		subgui.setWorldAndResolution(mc, width, height);
    		subgui.initGui();
    	}
    	guiLeft = (width- xSize)/2;
        guiTop = (height - ySize) / 2;
        buttonList.clear();
        labels.clear();
        textfields.clear();
        buttons.clear();
        scrolls.clear();
        sliders.clear();
        Keyboard.enableRepeatEvents(true);
    }
    @Override
    public void updateScreen(){
    	if(subgui != null)
    		subgui.updateScreen();
    	else{
	    	for(GuiNpcTextField tf : textfields.values()){
	    		if(tf.enabled)
	    			tf.updateCursorCounter();
	    	}
	        super.updateScreen();
    	}
    }
	
    @Override
    public void mouseClicked(int i, int j, int k) {
    	if(subgui != null)
    		subgui.mouseClicked(i,j,k);
    	else{
	    	for(GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values()))
	    		if(tf.enabled)
	    			tf.mouseClicked(i, j, k);
	        if (k == 0){
		        for(GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())){
		        	scroll.mouseClicked(i, j, k);
		        }
	        }
	    	mouseEvent(i,j,k);
	    	super.mouseClicked(i, j, k);
    	}
    }

    public void mouseEvent(int i, int j, int k){};

    @Override
	protected void actionPerformed(GuiButton guibutton) {
		if(subgui != null)
			subgui.buttonEvent(guibutton);
		else{
			buttonEvent(guibutton);
		}
	}
    public void buttonEvent(GuiButton guibutton){};

    @Override
	public void keyTyped(char c, int i){
    	if(subgui != null)
    		subgui.keyTyped(c,i);
    	else{
	    	for(GuiNpcTextField tf : textfields.values())
	    			tf.textboxKeyTyped(c, i);
	        if (closeOnEsc && (i == 1 || !GuiNpcTextField.isActive() && isInventoryKey(i))){
	        	close();
	        }
    	}
    }
    public void onGuiClosed(){
    	GuiNpcTextField.unfocus();
    }
    public void close(){
        displayGuiScreen(null);
        mc.setIngameFocus();
        save();
    }
    public void addButton(GuiNpcButton button){
    	buttons.put(button.id,button);
    	buttonList.add(button);
    }
	public GuiNpcButton getButton(int i) {
		return buttons.get(i);
	}
    public void addTextField(GuiNpcTextField tf){
    	textfields.put(tf.id,tf);
    }
    public GuiNpcTextField getTextField(int i){
    	return textfields.get(i);
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
	public void addScroll(GuiCustomScroll scroll) {
        scroll.setWorldAndResolution(mc, 350, 250);
        scrolls.put(scroll.id, scroll);
	}
	public GuiCustomScroll getScroll(int id){
		return scrolls.get(id); 
	}
	
    public abstract void save();
    
    @Override
    public void drawScreen(int i, int j, float f){
    	mouseX = i;
    	mouseY = j;
    	if(drawDefaultBackground)
    		drawDefaultBackground();
    	
    	if(background != null && mc.renderEngine != null){
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    		mc.renderEngine.bindTexture(background);
    		if(xSize > 256){
    			drawTexturedModalRect(guiLeft, guiTop, 0, 0, 250, ySize);
    			drawTexturedModalRect(guiLeft + 250, guiTop, 256 - (xSize - 250), 0, xSize - 250, ySize);
    		}
    		else
        		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    	}
    	
        drawCenteredString(fontRendererObj, title, width / 2, guiTop + 4, 0xffffff);
        for(GuiNpcLabel label : labels.values())
        	label.drawLabel(this,fontRendererObj);
    	for(GuiNpcTextField tf : textfields.values()){
    		tf.drawTextBox();
    	}
        for(GuiCustomScroll scroll : scrolls.values())
            scroll.drawScreen(i, j, f, hasSubGui()?0:Mouse.getDWheel());
        for(GuiScreen gui : extra.values())
        	gui.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
        if(subgui != null){
        	GL11.glTranslatef(0, 0, 150F);
    		subgui.drawScreen(i,j,f);
    		GL11.glTranslatef(0, 0, -150F);
        }
    }
	public FontRenderer getFontRenderer() {
		return this.fontRendererObj;
	}
	
	public void elementClicked() {
		if(subgui != null)
			subgui.elementClicked();
	}
	@Override
    public boolean doesGuiPauseGame(){
        return false;
    }
	
	public void doubleClicked() {
	}
	
	public boolean isInventoryKey(int i){
        return i == mc.gameSettings.keyBindInventory.getKeyCode(); //inventory key
	}
	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
	}

	public void displayGuiScreen(GuiScreen gui) {
		mc.displayGuiScreen(gui);
	}

    public void setSubGui(SubGuiInterface gui){
    	subgui = gui;
		subgui.setWorldAndResolution(mc, width, height);
		subgui.parent = this;
    	initGui();
    }
	public void closeSubGui(SubGuiInterface gui) {
		subgui = null;
	}
	public boolean hasSubGui() {
		return subgui != null;
	}
	public SubGuiInterface getSubGui() {
		if(hasSubGui() && subgui.hasSubGui())
			return subgui.getSubGui();
		return subgui;
	}
	
	public void drawNpc(EntityLivingBase npc, int x, int y, float zoomed, int rotation){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft + x, guiTop + y, 50F);
        float scale = 1;
        if(npc.height > 2.4)
        	scale = 2 / npc.height;
        if(npc instanceof EntityPlayer){
        	//GL11.translate(0, -data * scale * zoomed * 60, 0);
        }
        GL11.glScalef(-60 * scale * zoomed, 60 * scale * zoomed, 60 * scale * zoomed);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		//GL11.disableLighting();
        RenderHelper.enableStandardItemLighting();
		
		float f2 = npc.renderYawOffset;
		float f3 = npc.rotationYaw;
		float f4 = npc.rotationPitch;
		float f7 = npc.rotationYawHead;
		float f5 = (float) (guiLeft + x) - mouseX;
		float f6 = (float) ((guiTop + y) - 100 * scale * zoomed) - mouseY;
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-(float) Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		npc.renderYawOffset = rotation;
		npc.rotationYaw = (float)Math.atan(f5 / 80F) * 40F + rotation;
		npc.rotationPitch = -(float) Math.atan(f6 / 40F) * 20F;
		npc.rotationYawHead = npc.rotationYaw;
		RenderManager.instance.playerViewY = 180F;
		RenderManager.instance.renderEntityWithPosYaw(npc, 0, 0, 0,	0, 1);
		npc.prevRenderYawOffset = npc.renderYawOffset = f2;
		npc.prevRotationYaw = npc.rotationYaw = f3;
		npc.prevRotationPitch = npc.rotationPitch = f4;
		npc.prevRotationYawHead = npc.rotationYawHead = f7;
		GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

	}
	
    public void openLink(String link){
        try{
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(link)});
        }
        catch (Throwable throwable)
        {
        }
    }
}
