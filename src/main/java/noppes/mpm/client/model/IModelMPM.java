package noppes.mpm.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import noppes.mpm.ModelData;

public interface IModelMPM {

    public boolean getCurrentlyPlayerTexture();
    public void setCurrentlyPlayerTexture(boolean value);
    public boolean getIsArmor();
    public boolean getIsAlexArmor();
    public boolean getX64();

    public ModelBiped getBiped();

    public boolean isSleeping(Entity entity);

    public void setPlayerData(ModelData data, EntityLivingBase entity);

}
