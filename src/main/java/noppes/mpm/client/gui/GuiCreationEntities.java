package noppes.mpm.client.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import noppes.mpm.client.gui.util.GuiCustomScroll;
import noppes.mpm.client.gui.util.GuiNpcButton;
import noppes.mpm.client.gui.util.ICustomScrollListener;

public class GuiCreationEntities extends GuiCreationScreenInterface implements ICustomScrollListener{

	public HashMap<String,Class<? extends EntityLivingBase>> data = new HashMap<String, Class<? extends EntityLivingBase>>();
	private List<String> list;
	private GuiCustomScroll scroll;
	private boolean resetToSelected = true;
	
	public GuiCreationEntities(){
        Map<String, Class<? extends Entity>> mapping = EntityList.stringToClassMapping;
        for(String name : mapping.keySet()){
        	Class<? extends Entity> c = mapping.get(name);
        	try {
        		if(EntityLiving.class.isAssignableFrom(c) && c.getConstructor(new Class[] {World.class}) != null && !Modifier.isAbstract(c.getModifiers())){
        			if(RenderManager.instance.getEntityClassRenderObject(c) instanceof RendererLivingEntity)
        				data.put(name.toString(),c.asSubclass(EntityLivingBase.class));
        		}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
			}
        } 
		list = new ArrayList<String>(data.keySet());
		list.add("Player");
		Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
		active = 1;
		xOffset = 60;
	}

    @Override
    public void initGui() {
    	super.initGui();
    	addButton(new GuiNpcButton(10, guiLeft, guiTop + 46, 120, 20, "Reset To Player"));
    	if(scroll == null){
    		scroll = new GuiCustomScroll(this, 0);
    		scroll.setUnsortedList(list);
    	}
    	scroll.guiLeft = guiLeft;
    	scroll.guiTop = guiTop + 68;
    	scroll.setSize(100, ySize - 96);

    	String selected = "Player";
    	if(entity != null){
	    	for(Entry<String, Class<? extends EntityLivingBase>> en : data.entrySet()){
	    		if(en.getValue().toString().equals(entity.getClass().toString())){
	    			selected = en.getKey();
	    		}
	    	}
    	}
    	scroll.setSelected(selected);
    	
    	if(resetToSelected){
    		scroll.scrollTo(scroll.getSelected());
    		resetToSelected = false;
    	}
    	addScroll(scroll);
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	super.actionPerformed(btn);
    	if(btn.id == 10){
    		playerdata.setEntityClass(null);
    		resetToSelected = true;
    		initGui();
    	}
    }

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		playerdata.setEntityClass(data.get(scroll.getSelected()));
		initGui();
	}

}
