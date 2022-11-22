package noppes.mpm;

import net.minecraft.nbt.NBTTagCompound;

public class ModelPartConfig {
	public float scaleX = 1, scaleY = 1, scaleZ = 1;
	public float transX = 0, transY = 0, transZ = 0;
	public boolean notShared = true;

	public NBTTagCompound writeToNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setFloat("ScaleX", scaleX);
		compound.setFloat("ScaleY", scaleY);
		compound.setFloat("ScaleZ", scaleZ);
		
		compound.setFloat("TransX", transX);
		compound.setFloat("TransY", transY);
		compound.setFloat("TransZ", transZ);
		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound){
		scaleX = checkValue(compound.getFloat("ScaleX"), 0.5f, 1.5f);
		scaleY = checkValue(compound.getFloat("ScaleY"), 0.5f, 1.5f);
		scaleZ = checkValue(compound.getFloat("ScaleZ"), 0.5f, 1.5f);

		transX = checkValue(compound.getFloat("TransX"), 0, 1);
		transY = checkValue(compound.getFloat("TransY"), 0, 1);
		transZ = checkValue(compound.getFloat("TransZ"), 0, 1);
	}
	
	public String toString(){
		return "ScaleX: " + scaleX + " - ScaleY: " + scaleY + " - ScaleZ: " + scaleZ;
	}

	public void setScale(float x, float y, float z) {
		scaleX = checkValue(x, 0.5f, 1.5f);
		scaleY = checkValue(y, 0.5f, 1.5f);
		scaleZ = checkValue(z, 0.5f, 1.5f);
	}
	public void setScale(float x, float y) {
		scaleZ = scaleX = x;
		scaleY = y;
	}
	
	public float checkValue(float given, float min, float max){
		if(given < min)
			return min;
		if(given > max)
			return max;
		return given;
	}

	public void setTranslate(float transX, float transY, float transZ) {
		this.transX = transX;
		this.transY = transY;
		this.transZ = transZ;
	}

	public void copyValues(ModelPartConfig config) {
		scaleX = config.scaleX;
		scaleY = config.scaleY;
		scaleZ = config.scaleZ;
		transX = config.transX;
		transY = config.transY;
		transZ = config.transZ;
	}

}
