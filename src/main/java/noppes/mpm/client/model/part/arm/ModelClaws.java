package noppes.mpm.client.model.part.arm;

import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.model.Model2DRenderer;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelPartInterface;
import noppes.mpm.constants.EnumParts;

public class ModelClaws extends ModelPartInterface {

	private final Model2DRenderer claw;
	private final boolean rightArm;

	public ModelClaws(ModelMPM base, boolean rightArm) {
		super(base);
		this.rightArm = rightArm;
		claw = new Model2DRenderer(base, 0, 16, 4, 4, 64, 32);
		if(rightArm)
			claw.setRotationPoint(-2F, 14f, -2);
		else
			claw.setRotationPoint(3F, 14f, -2);
		claw.rotateAngleY = (float) (Math.PI / -2);
		claw.setScale(0.25f);
		this.addChild(claw);
	}

	@Override
	public void initData(ModelData data) {
		ModelPartData config = data.getPartData(EnumParts.CLAWS);
		if(config == null || rightArm && config.pattern == 1 || !rightArm && config.pattern == 2)
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
