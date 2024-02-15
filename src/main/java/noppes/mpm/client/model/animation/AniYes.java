package noppes.mpm.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import noppes.mpm.ModelData;

public class AniYes {
	public static void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, ModelBiped model, ModelData data){
		float ticks = (entity.ticksExisted - data.animationStart) / 8f;
		float ticks2 = (entity.ticksExisted + 1 - data.animationStart) / 8f;
		ticks += (ticks2 - ticks) * Minecraft.getMinecraft().timer.renderPartialTicks;

		ticks = ticks % 2;
		float ani = ticks - 0.5f;
		if(ticks > 1)
			ani = 1.5f - ticks;
		model.bipedHead.rotateAngleX = ani;
	}
}
