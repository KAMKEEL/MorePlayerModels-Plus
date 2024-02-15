package noppes.mpm.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class AniPoint {

	public static void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, ModelBiped base){
        base.bipedRightArm.rotateAngleX = -1.570796F;
		base.bipedRightArm.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		base.bipedRightArm.rotateAngleZ = 0;
	}
}
