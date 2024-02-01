package noppes.mpm.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class AniDancing {
	public static void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, ModelBiped model){
		float dancing = entity.ticksExisted / 4f;
		float dancing2 = (entity.ticksExisted + 1) / 4f;
		dancing += (dancing2 - dancing) * Minecraft.getMinecraft().timer.renderPartialTicks;
		float x = (float)Math.sin(dancing);
		float y = (float)Math.abs(Math.cos(dancing));
		
		model.bipedHeadwear.rotationPointX = model.bipedHead.rotationPointX = x * 0.75F;
		model.bipedHeadwear.rotationPointY = model.bipedHead.rotationPointY = y * 1.25F - 0.02F;
		model.bipedHeadwear.rotationPointZ = model.bipedHead.rotationPointZ = -y * 0.75F;

		model.bipedLeftArm.rotationPointX += x * 0.25F;
		model.bipedLeftArm.rotationPointY += y * 1.25F;

		model.bipedRightArm.rotationPointX += x * 0.25F;
		model.bipedRightArm.rotationPointY += y * 1.25F;
		
		model.bipedBody.rotationPointX = x * 0.25F;
	}

}
