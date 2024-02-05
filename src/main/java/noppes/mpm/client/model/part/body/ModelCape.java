package noppes.mpm.client.model.part.body;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import noppes.mpm.ModelData;
import noppes.mpm.client.model.IModelMPM;
import noppes.mpm.client.model.ModelPartInterface;

public class ModelCape extends ModelPartInterface
{
    public ModelRenderer cape;
    private final IModelMPM baseType;
    
    public ModelCape(IModelMPM base) {
        super(base);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.baseType = base;
        (this.cape = new ModelRenderer(baseType.getBiped(), 0, 0)).addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1);
        this.cape.setRotationPoint(0.0f, 0.0f, 2.0f);
        this.cape.setTextureSize(64, 32);
        this.setRotation(this.cape, 0.0f, 0.0f, 0.0f);
        this.addChild(cape);
    }
    
    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
        baseType.getBiped().render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.cape.render(f5);
    }

    public void setRotationAngles(float par1, float par2, float par3,
                                  float par4, float par5, float par6, Entity entity) {
        baseType.getBiped().setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
    }

    @Override
    public void initData(ModelData data) {
        this.isHidden = data.cloak == 0;
    }
}
