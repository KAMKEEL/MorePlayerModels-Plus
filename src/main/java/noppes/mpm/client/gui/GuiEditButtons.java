package noppes.mpm.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.KeyBinding;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.SubGuiInterface;
import noppes.mpm.config.ConfigClient;
import org.lwjgl.input.Keyboard;

public class GuiEditButtons extends SubGuiInterface{

	private GuiCreationScreenInterface parent;
	private final String[] animations = new String[]{"None","Sleep","Crawl","Hug","Sit","Dance","Wave","Wag","Bow","Cry","Yes","No","Point","Death"};
	
	public GuiEditButtons(GuiCreationScreenInterface parent){
		this.parent = parent;
		this.closeOnEsc = true;
	}

    @Override
    public void initGui() {
    	super.initGui();
		int y = guiTop + 20;

		addLabel(new GuiNpcLabel(0, "This is only to change the animation/commands linked to a button.", guiLeft, y, 0xFFFFFF));
		addLabel(new GuiNpcLabel(6, "To change the actual button use minecraft options -> contols", guiLeft, y + 11, 0xFFFFFF));

		addButton(1, y += 22, "MPM 1", ConfigClient.button1);
		addButton(2, y += 22, "MPM 2", ConfigClient.button2);
		addButton(3, y += 22, "MPM 3", ConfigClient.button3);
		addButton(4, y += 22, "MPM 4", ConfigClient.button4);
		addButton(5, y += 22, "MPM 5", ConfigClient.button5);


    }
    private void addButton(int id, int y, String title, int value){
		for(KeyBinding key : Minecraft.getMinecraft().gameSettings.keyBindings){
			if(key.getKeyDescription().equals(title)){
				title += " (" + Keyboard.getKeyName(key.getKeyCode()) + ")";
				break;
			}
		}
    	value = getValue(value);
    	addButton(new GuiNpcButton(id, guiLeft + 50, y, 70, 20, animations, value));
		addLabel(new GuiNpcLabel(id, title, guiLeft, y + 5, 0xFFFFFF));
    }
    private int getValue(int i){
    	if(i == 0)
    		return 0;
    	if(i >= 1 && i <= 4)
    		return 1;
    	
    	return i - 3;
    }

	@Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	GuiNpcButton button = (GuiNpcButton) btn;

    	if(button.id == 1){
			ConfigClient.button1 = getValue(button);
			ConfigClient.button1Property.set(ConfigClient.button1);
    	}
    	if(button.id == 2){
			ConfigClient.button2 = getValue(button);
			ConfigClient.button2Property.set(ConfigClient.button2);
    	}
    	if(button.id == 3){
			ConfigClient.button3 = getValue(button);
			ConfigClient.button3Property.set(ConfigClient.button3);
    	}
    	if(button.id == 4){
			ConfigClient.button4 = getValue(button);
			ConfigClient.button4Property.set(ConfigClient.button4);
    	}
    	if(button.id == 5){
    		ConfigClient.button5 = getValue(button);
			ConfigClient.button5Property.set(ConfigClient.button5);
    	}
    	if(button.id == 66){
    		close();
    	}
    }
    
    private int getValue(GuiNpcButton button) {
    	int value = button.getValue();
		if(value <= 1)
			return value;
		return value + 3;
	}

	@Override
    public void close(){
        this.mc.displayGuiScreen(parent);
    }
}
