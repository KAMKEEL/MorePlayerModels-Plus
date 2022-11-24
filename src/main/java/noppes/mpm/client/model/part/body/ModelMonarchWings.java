package noppes.mpm.client.model.part.body;

import net.minecraft.client.model.ModelRenderer;
import noppes.mpm.client.model.ModelMPM;

public class ModelMonarchWings extends ModelRenderer {
	private final ModelRenderer monarch;
	public final ModelRenderer right_monarch;
	private final ModelRenderer upper_right2;
	private final ModelRenderer upper_right_r1;
	private final ModelRenderer lower_right2;
	public final ModelRenderer left_monarch;
	private final ModelRenderer upper_left2;
	private final ModelRenderer upper_left_r1;
	private final ModelRenderer lower_left2;


	public ModelMonarchWings(ModelMPM base) {
		super(base);
		textureWidth = 64;
		textureHeight = 64;

		monarch = new ModelRenderer(base);
		monarch.setTextureSize(64, 64);
		monarch.setRotationPoint(0.0F, 5.0F, 3.0F);

		right_monarch = new ModelRenderer(base);
		right_monarch.setTextureSize(64, 64);
		right_monarch.setRotationPoint(-1.0F, 0.0F, 0.0F);
		monarch.addChild(right_monarch);

		upper_right2 = new ModelRenderer(base);
		upper_right2.setTextureSize(64, 64);
		upper_right2.setRotationPoint(0.0F, -3.0F, 0.0F);
		right_monarch.addChild(upper_right2);

		upper_right_r1 = new ModelRenderer(base, 8, 0);
		upper_right_r1.setTextureSize(64, 64);
		upper_right_r1.addBox( -14.9128F, -10.0F, -0.9962F, 16, 8, 0, 0.0F);
		upper_right_r1.setRotationPoint(-1.0F, 6.0F, 0.0F);
		upper_right2.addChild(upper_right_r1);
		setRotationAngle(upper_right_r1, 0.0F, 0.0873F, 0.0F);

		lower_right2 = new ModelRenderer(base, 48, 22);
		lower_right2.setTextureSize(64, 64);
		lower_right2.addBox( -8.0F, -3.0F, -1.0F, 8, 10, 0, 0.0F);
		lower_right2.setRotationPoint(0.0F, 0.0F, 0.0F);
		right_monarch.addChild(lower_right2);

		left_monarch = new ModelRenderer(base);
		left_monarch.mirror = true;
		left_monarch.setTextureSize(64, 64);
		left_monarch.setRotationPoint(1.0F, 0.0F, 0.0F);
		monarch.addChild(left_monarch);

		upper_left2 = new ModelRenderer(base);
		upper_left2.mirror = true;
		upper_left2.setTextureSize(64, 64);
		upper_left2.setRotationPoint(0.0F, -3.0F, 0.0F);
		left_monarch.addChild(upper_left2);

		upper_left_r1 = new ModelRenderer(base, 8, 0);
		upper_left_r1.mirror = true;
		upper_left_r1.setTextureSize(64, 64);
		upper_left_r1.addBox( -1.0872F, -10.0F, -0.9962F, 16, 8, 0, 0.0F);
		upper_left_r1.setRotationPoint(1.0F, 6.0F, 0.0F);
		upper_left2.addChild(upper_left_r1);
		setRotationAngle(upper_left_r1, 0.0F, -0.0873F, 0.0F);

		lower_left2 = new ModelRenderer(base, 48, 22);
		lower_left2.mirror = true;
		lower_left2.setTextureSize(64, 64);
		lower_left2.addBox(0.0F, -3.0F, -1.0F, 8, 10, 0, 0.0F);
		lower_left2.setRotationPoint(0.0F, 0.0F, 0.0F);
		left_monarch.addChild(lower_left2);

		this.addChild(monarch);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
