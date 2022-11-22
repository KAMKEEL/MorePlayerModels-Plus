package noppes.mpm.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MPMEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;
import noppes.mpm.client.Client;
import noppes.mpm.client.gui.util.GuiNPCInterface;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.GuiNpcLabel;
import noppes.mpm.client.gui.util.GuiNpcSlider;
import noppes.mpm.client.gui.util.ISliderListener;
import noppes.mpm.client.gui.util.ISubGuiListener;
import noppes.mpm.client.gui.util.SubGuiInterface;
import noppes.mpm.constants.EnumPackets;

import org.lwjgl.input.Keyboard;

public class GuiCreationScreenInterface extends GuiNPCInterface implements ISubGuiListener, ISliderListener{
	public static String Message = "";
	public EntityLivingBase entity;
	
	private boolean saving = false;
	protected boolean hasSaving = true;
	public int active = 0;

	private EntityPlayer player;
	public int xOffset = 0;
	public ModelData playerdata;
	
	public static GuiCreationScreenInterface Gui = new GuiCreationParts();
	
	protected NBTTagCompound original = new NBTTagCompound();
	
	private static float rotation = 0.5f;
	
	public GuiCreationScreenInterface(){
		playerdata = PlayerDataController.instance.getPlayerData(Minecraft.getMinecraft().thePlayer);
		original = playerdata.writeToNBT();
		xSize = 400;
		ySize = 240;
		xOffset = 140;

		player = Minecraft.getMinecraft().thePlayer;
		this.closeOnEsc = true;
	}

    @Override
    public void initGui() {
    	super.initGui();
    	entity = playerdata.getEntity(mc.thePlayer);
    	Keyboard.enableRepeatEvents(true);

    	addButton(new GuiNpcButton(0, guiLeft, guiTop, 60, 20, "gui.config"));
    	addButton(new GuiNpcButton(1, guiLeft + 62, guiTop, 60, 20, "gui.entity"));
    	if(entity == null)
    		addButton(new GuiNpcButton(2, guiLeft, guiTop + 23, 60, 20, "gui.parts"));
    	else{
    		GuiCreationExtra gui = new GuiCreationExtra();
    		gui.playerdata = playerdata;
    		if(!gui.getData(entity).isEmpty())
    			addButton(new GuiNpcButton(2, guiLeft, guiTop + 23, 60, 20, "gui.extra"));
    		else if(active == 2){
        		mc.displayGuiScreen(new GuiCreationEntities());
    			return;
    		}
    	}
    	if(entity == null)
    		addButton(new GuiNpcButton(3, guiLeft + 62, guiTop + 23, 60, 20, "gui.scale"));
    	if(hasSaving){
    		addButton(new GuiNpcButton(4, guiLeft, guiTop + ySize - 24, 60, 20, "gui.save"));
    		addButton(new GuiNpcButton(5, guiLeft + 62, guiTop + ySize - 24, 60, 20, "gui.load"));
    	}
    	getButton(active).enabled = false;
    	addButton(new GuiNpcButton(66, guiLeft + xSize - 20, guiTop, 20, 20, "X"));
    	    	
    	addLabel(new GuiNpcLabel(0, Message, guiLeft + 120, guiTop + ySize - 10, 0xff0000));
    	getLabel(0).center(xSize - 120);
    	
    	addSlider(new GuiNpcSlider(this, 500, guiLeft + xOffset + 142, guiTop + 210, 120, 20, rotation));
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(btn.id == 0){
    		openGui(new GuiCreationConfig());
    	}
    	if(btn.id == 1){
    		openGui(new GuiCreationEntities());
    	}
    	if(btn.id == 2){
    		if(entity == null)
    			openGui(new GuiCreationParts());
    		else
    			openGui(new GuiCreationExtra());
    	}
    	if(btn.id == 3){
    		openGui(new GuiCreationScale());
    	}
    	if(btn.id == 4){
    		this.setSubGui(new GuiPresetSave(this, playerdata));
    	}
    	if(btn.id == 5){
    		openGui(new GuiCreationLoad());
    	}
    	if(btn.id == 66){
    		close();
    	}
    }
    
    @Override
    public void mouseClicked(int i, int j, int k) {
    	if(!saving)
    		super.mouseClicked(i, j, k);
    }
    
    @Override
    public void drawScreen(int x, int y, float f){
    	super.drawScreen(x, y, f);
    	entity = playerdata.getEntity(mc.thePlayer);
    	EntityLivingBase entity = this.entity;
    	if(entity == null)
    		entity = this.player;
    	else
    		MPMEntityUtil.Copy(mc.thePlayer, player);
    	
    	drawNpc(entity, xOffset + 200, 200, 1, (int)(rotation * 360 - 180));
    }
    
    @Override
    public void onGuiClosed(){
        Keyboard.enableRepeatEvents(false);
    }
    @Override
    public void save(){
    	NBTTagCompound newCompound = playerdata.writeToNBT();
    	if(!original.equals(newCompound)){
    		PlayerDataController.instance.savePlayerData(this.mc.thePlayer, playerdata);
    		Client.sendData(EnumPackets.UPDATE_PLAYER_DATA, newCompound);
    		original = newCompound;
    	}
    	
    }
    
    public void openGui(GuiScreen gui){
    	save();
    	mc.displayGuiScreen(gui);
    	if(gui instanceof GuiCreationScreenInterface)
    		Gui = (GuiCreationScreenInterface) gui;
    }
    
	public void subGuiClosed(SubGuiInterface subgui){
		initGui();
	}


	@Override
	public void mouseDragged(GuiNpcSlider slider) {
		if(slider.id == 500){
			rotation = slider.sliderValue;
			slider.setString("" + (int)(rotation * 360));
		}
	}

	@Override
	public void mousePressed(GuiNpcSlider slider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(GuiNpcSlider slider) {
		// TODO Auto-generated method stub
		
	}
}
