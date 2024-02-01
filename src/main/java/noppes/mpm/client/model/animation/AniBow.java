package noppes.mpm.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import noppes.mpm.ModelData;

public class AniBow {
	public static void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, ModelBiped model, ModelData data){
		float ticks = (entity.ticksExisted - data.animationStart) / 10f;
		if(ticks > 1)
			ticks = 1;
		float ticks2 = (entity.ticksExisted + 1 - data.animationStart) / 10f;
		if(ticks2 > 1)
			ticks2 = 1;
		ticks += (ticks2 - ticks) * Minecraft.getMinecraft().timer.renderPartialTicks;
		model.bipedBody.rotateAngleX = ticks;
		model.bipedHead.rotateAngleX = ticks;
		model.bipedLeftArm.rotateAngleX = ticks;
		model.bipedRightArm.rotateAngleX = ticks;

		model.bipedBody.rotationPointZ = -ticks * 10;
		model.bipedBody.rotationPointY = ticks * 6;

		model.bipedHead.rotationPointZ = -ticks * 10;
		model.bipedHead.rotationPointY = ticks * 6;

		model.bipedLeftArm.rotationPointZ = -ticks * 10;
		model.bipedLeftArm.rotationPointY += ticks * 6;

		model.bipedRightArm.rotationPointZ = -ticks * 10;
		model.bipedRightArm.rotationPointY += ticks * 6;
	}
}
