package noppes.mpm.client.model.part.head;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.model.IModelMPM;
import noppes.mpm.client.model.Model2DRenderer;
import noppes.mpm.client.model.ModelPartInterface;
import noppes.mpm.constants.EnumParts;

public class ModelBeard extends ModelPartInterface {

	private Model2DRenderer model;
	
	public ModelBeard(IModelMPM base) {
		super(base);
		model = new Model2DRenderer(base.getBiped(), 56, 20, 8, 12, 64, 32);
		model.setRotationPoint(-3.99F, 11.9f, -4);
		model.setScale(0.74f);
		this.addChild(model);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity entity) {
		if(this.base.getBiped().bipedHead.rotateAngleX > 0)
			rotateAngleX = -base.getBiped().bipedHead.rotateAngleX;
		else
			rotateAngleX = 0;
	}

	@Override
	public void initData(ModelData data) {
		ModelPartData config = data.getPartData(EnumParts.BEARD);
		if(config == null)
		{
			isHidden = true;
			return;
		}
		color = config.color;
		isHidden = false;
		
		if(!config.playerTexture){
			location = (ResourceLocation) config.getResource();
		}
		else
			location = null;
	}

}
