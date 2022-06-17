package noppes.mpm.client.model.part;

import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.model.Model2DRenderer;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelPartInterface;

public class ModelFin extends ModelPartInterface {

	private Model2DRenderer model;

	public ModelFin(ModelMPM base) {
		super(base);
		model = new Model2DRenderer(base, 48, 8, 16, 24, 64, 32);
		model.setRotationPoint(-0.5F, 12, 10);
		model.setScale(0.74f);
		model.rotateAngleY = (float)Math.PI / 2;
		this.addChild(model);
	}

	@Override
	public void initData(ModelData data) {
		ModelPartData config = data.getPartData("fin");
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
