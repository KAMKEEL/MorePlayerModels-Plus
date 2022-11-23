package noppes.mpm.client.model.part.body;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMonarchWings extends ModelRenderer {
	private final ModelRenderer monarch;
	private final ModelRenderer right2;
	private final ModelRenderer upper_right2;
	private final ModelRenderer upper_right_r1;
	private final ModelRenderer lower_right2;
	private final ModelRenderer left2;
	private final ModelRenderer upper_left2;
	private final ModelRenderer upper_left_r1;
	private final ModelRenderer lower_left2;


	public ModelMonarchWings(ModelBiped base) {
		super(base);
		textureWidth = 64;
		textureHeight = 64;

		monarch = new ModelRenderer(base);
		monarch.setTextureSize(64, 64);
		monarch.setRotationPoint(0.0F, 5.0F, 3.0F);

		right2 = new ModelRenderer(base);
		right2.setTextureSize(64, 64);
		right2.setRotationPoint(-1.0F, 0.0F, 0.0F);
		monarch.addChild(right2);

		upper_right2 = new ModelRenderer(base);
		upper_right2.setTextureSize(64, 64);
		upper_right2.setRotationPoint(0.0F, -3.0F, 0.0F);
		right2.addChild(upper_right2);

		upper_right_r1 = new ModelRenderer(base, 8, 0);
		upper_right_r1.setTextureSize(64, 64);
		upper_right_r1.addBox( -15.0F, -8.0F, 0.0F, 16, 8, 0, 0.0F);
		upper_right_r1.setRotationPoint(-1.0F, 6.0F, 0.0F);
		upper_right2.addChild(upper_right_r1);
		setRotationAngle(upper_right_r1, 0.0F, 0.0873F, 0.0F);

		lower_right2 = new ModelRenderer(base, 48, 22);
		lower_right2.setTextureSize(64, 64);
		lower_right2.addBox(-8.0F, -1.0F, 0.0F, 8, 10, 0, 0.0F);
		lower_right2.setRotationPoint(0.0F, 0.0F, 0.0F);
		right2.addChild(lower_right2);

		left2 = new ModelRenderer(base);
		left2.mirror = true;
		left2.setTextureSize(64, 64);
		left2.setRotationPoint(1.0F, 0.0F, 0.0F);
		monarch.addChild(left2);

		upper_left2 = new ModelRenderer(base);
		upper_left2.mirror = true;
		upper_left2.setTextureSize(64, 64);
		upper_left2.setRotationPoint(0.0F, -3.0F, 0.0F);
		left2.addChild(upper_left2);

		upper_left_r1 = new ModelRenderer(base, 8, 0);
		upper_left_r1.mirror = true;
		upper_left_r1.setTextureSize(64, 64);
		upper_left_r1.addBox( -1.0F, -8.0F, 0.0F, 16, 8, 0, 0.0F);
		upper_left_r1.setRotationPoint(1.0F, 6.0F, 0.0F);
		upper_left2.addChild(upper_left_r1);
		setRotationAngle(upper_left_r1, 0.0F, -0.0873F, 0.0F);

		lower_left2 = new ModelRenderer(base, 48, 22);
		lower_left2.mirror = true;
		lower_left2.setTextureSize(64, 64);
		lower_left2.addBox(0.0F, -1.0F, 0.0F, 8, 10, 0, 0.0F);
		lower_left2.setRotationPoint(0.0F, 0.0F, 0.0F);
		left2.addChild(lower_left2);

		this.addChild(monarch);
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
