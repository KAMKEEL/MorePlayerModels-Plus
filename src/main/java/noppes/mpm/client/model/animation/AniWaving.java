package noppes.mpm.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class AniWaving {
	public static void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, ModelBiped base){
		float f = MathHelper.sqrt_double(entity.ticksExisted * 0.27f);
		float f2 = MathHelper.sqrt_double((entity.ticksExisted + 1) * 0.27f);
		f += (f2 - f) * Minecraft.getMinecraft().timer.renderPartialTicks;
		
		base.bipedRightArm.rotateAngleX = -0.1f;
		base.bipedRightArm.rotateAngleY = 0;
		base.bipedRightArm.rotateAngleZ = (float) (Math.PI - 1f  - f * 0.5f );
	}
		
}
