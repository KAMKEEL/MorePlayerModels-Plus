package noppes.mpm.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiNpcTextField extends GuiTextField{
	public boolean enabled = true;
	public boolean inMenu = true;
	public boolean numbersOnly = false;
	private ITextfieldListener listener;
    public int id;
    public int min = 0,max = Integer.MAX_VALUE,def = 0;
    private static GuiNpcTextField activeTextfield = null;
    
    private final int[] allowedSpecialChars = {14,211,203,205};
	
	public GuiNpcTextField(int id,GuiScreen parent, int i, int j, int k, int l, String s) {
		super(Minecraft.getMinecraft().fontRenderer, i, j, k, l);
		setMaxStringLength(500);
		this.setText(s);
		this.id = id;
		if(parent instanceof ITextfieldListener)
			listener = (ITextfieldListener) parent;
	}
	
	private boolean charAllowed(char c, int i){
		if(!numbersOnly || Character.isDigit(c))
			return true;
		for(int j : allowedSpecialChars)
			if(j == i)
				return true;
		
		return false;
	}
	
	@Override
    public boolean textboxKeyTyped(char c, int i)
    {
    	if(!charAllowed(c,i))
    		return false;
		return super.textboxKeyTyped(c, i);
    }
    public boolean isEmpty(){
    	return getText().trim().length() == 0;
    }

	public int getInteger(){
    	return Integer.parseInt(getText());
    }
    public boolean isInteger(){
    	try{
    		Integer.parseInt(getText());
    		return true;
    	}catch(NumberFormatException e){
    		return false;
    	}
    }
	@Override
    public void mouseClicked(int i, int j, int k)
    {
    	boolean wasFocused = this.isFocused();
    	super.mouseClicked(i, j, k);
    	if(wasFocused != isFocused()){
    		if(wasFocused){
    			unFocused();
    		}
    	}
    	if(isFocused())
    		activeTextfield = this;
    }

	public void unFocused(){
		if(numbersOnly){
			if(isEmpty() || !isInteger())
				setText(def + "");
			else if( getInteger() < min)
				setText(min+"");
			else if(getInteger() > max)
				setText(max+"");
		}
		if(listener != null)
			listener.unFocused(this);
		
		if(this == activeTextfield)
			activeTextfield = null;
	}
    public void drawTextBox()
    {
    	if(enabled)
    		super.drawTextBox();
    }
	public void setMinMaxDefault(int i, int j, int k) {
		min = i;
		max = j;
		def = k;
	}
	public static void unfocus() {
		if(activeTextfield != null)
			activeTextfield.unFocused();
		activeTextfield = null;
	}

	
}
