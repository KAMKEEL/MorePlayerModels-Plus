package noppes.mpm.client.model.part.head;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.ClientEventHandler;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelPartInterface;
import noppes.mpm.constants.EnumParts;
import org.lwjgl.opengl.GL11;

public class ModelHalo extends ModelPartInterface {
    private AbstractClientPlayer entity;
    private boolean thinHalo;
    public float haloWidth;
    public float haloOffsetX;
    public float haloOffsetY;
    public float haloOffsetZ;
    public float haloRotationX;
    public float haloRotationY;
    public float haloRotationZ;
    public float spinSpeed;
    public float floatSpeed;
    public float floatDistance;
    public byte haloMaterial;
    public byte type;
    public NBTTagCompound customData;

    private final ModelRenderer haloBase;
    private final ModelRenderer haloThin;
    public final ModelRenderer[] haloSegments;
    public final ModelRenderer[] haloSegmentsThin;

    public ModelHalo(ModelMPM base) {
        super(base);
        this.textureWidth = 81;
        this.textureHeight = 34;

        haloBase = new ModelRenderer(base, 0, 34);
        haloBase.setTextureSize((int) textureWidth, (int) textureHeight);
        haloBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        haloBase.addBox(-4.0F, -8.0F, -4.0F, 0, 0, 0, 0.0F);

        haloThin = new ModelRenderer(base, 0, 34);
        haloThin.setTextureSize((int) textureWidth, (int) textureHeight);
        haloThin.setRotationPoint(0.0F, 0.0F, 0.0F);
        haloThin.addBox(-4.0F, -8.0F, -4.0F, 0, 0, 0, 0.0F);

        haloSegments = new ModelRenderer[12];

        for(int i = 0; i < haloSegments.length; i++) {
            haloSegments[i] = new ModelRenderer(base, 0, 32);
            haloSegments[i].setTextureSize((int) textureWidth, (int) textureHeight);
            if(i == 0) {
                haloSegments[i].setRotationPoint(0.0F, -9.0F, -3.85F);
            } else {
                haloSegments[i].setRotationPoint(2F, 0F, 0F);
            }
            haloSegments[i].addBox(0F, -1F, 0F, 2, 1, 1, 0F);
            if(i == 0) {
                setRotateAngle(haloSegments[i], 0F, (float) (-Math.PI / haloSegments.length), 0F);
            } else {
                setRotateAngle(haloSegments[i], 0F, (float) (-2D * Math.PI / haloSegments.length), 0F);
                haloSegments[i - 1].addChild(haloSegments[i]);
            }
        }
        haloBase.addChild(haloSegments[0]);

        haloSegmentsThin = new ModelRenderer[48];

        for(int i = 0; i < haloSegmentsThin.length; i++) {
            haloSegmentsThin[i] = new ModelRenderer(base, 0, 32);
            haloSegmentsThin[i].setTextureSize((int) textureWidth, (int) textureHeight);
            haloSegmentsThin[i].setRotationPoint(0F, -9.0F, 0F);
            haloSegmentsThin[i].addBox((float) (40F/Math.PI), -1F, -1, 1, 1, 2, 0F);
            setRotateAngle(haloSegmentsThin[i], 0F, i * (float) (-2D * Math.PI / haloSegmentsThin.length), 0F);
            haloThin.addChild(haloSegmentsThin[i]);
        }

        addChild(haloBase);
        addChild(haloThin);
    }

    @Override
    public void setData(ModelData data, AbstractClientPlayer entity) {
        super.setData(data, entity);
        this.entity = entity;
    }

    public void translateHalo() {
        float f1 = 0;
        if(floatSpeed > 0.01 && floatDistance > 0.01) {
            float f = (float) entity.ticksExisted + ClientEventHandler.partialTicks;
            f1 = MathHelper.sin(f * 0.4F * floatSpeed * floatSpeed) / 2.0F + 0.5F;
            f1 = f1 * f1 + f1;
            f1 *= floatDistance * floatDistance * 4;
        }
        GL11.glTranslatef(0.0F, -0.2F + f1 * 0.05F, 0.0F);

        float elevation = haloOffsetY - 0.5F;
        GL11.glTranslatef((haloOffsetX - 0.5F) * 2, -elevation, (haloOffsetZ - 0.5F) * 2);
    }

    public void rotateHalo() {
        GL11.glRotatef(haloRotationX, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(haloRotationY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(haloRotationZ, 0.0F, 0.0F, 1.0F);
        if(spinSpeed >= 0.01) {
            GL11.glRotatef((float) entity.ticksExisted * spinSpeed * spinSpeed * 4, 0.0F, 1.0F, 0.0F);
        }
    }

    @Override
    public void render(float f5) {
        if (this.isHidden || !this.showModel)
            return;
        GL11.glPushMatrix();

        rotateHalo();
        translateHalo();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if(thinHalo) {
            GL11.glTranslatef(0F, -0.45F, 0F);
            float width = haloWidth * 0.8F - 0.4F;
            GL11.glScalef(0.7F + width, 0.3F, 0.7F + width);
        }

        float prevX = OpenGlHelper.lastBrightnessX;
        float prevY = OpenGlHelper.lastBrightnessY;
        if(haloMaterial <= 1) {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        if(haloMaterial == 1) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        }

        super.render(f5);

        if(haloMaterial == 1) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
        }
        if(haloMaterial <= 1) {
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void initData(ModelData data) {
        ModelPartData config = data.getPartData(EnumParts.HALO);
        if(config == null) {
            isHidden = true;
            return;
        }
        isHidden = false;
        this.color = config.color;

        type = config.type;
        thinHalo = type == 1;
        spinSpeed = getSpinSpeed(config);
        floatSpeed = getFloatingSpeed(config);
        floatDistance = getFloatingDistance(config);
        haloBase.isHidden = thinHalo;
        haloThin.isHidden = !thinHalo;
        haloWidth = getWidth(config);
        haloOffsetX = getOffsetX(config);
        haloOffsetY = getOffsetY(config);
        haloOffsetZ = getOffsetZ(config);
        haloRotationX = getRotationX(config) * 180 - 90;
        haloRotationY = getRotationY(config) * 360 - 180;
        haloRotationZ = getRotationZ(config) * 180 - 90;
        haloMaterial = getMaterial(config);
        customData = (NBTTagCompound) config.customData.copy();
    }

    public static float getWidth(ModelPartData data) {
        return data.customData.hasKey("width") ? data.customData.getFloat("width") : 0.5F;
    }

    public static void setWidth(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("width", value);
        } else {
            data.customData.removeTag("width");
        }
    }

    public static float getRotationX(ModelPartData data) {
        return data.customData.hasKey("rotX") ? data.customData.getFloat("rotX") : 0.5F;
    }

    public static void setRotationX(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("rotX", value);
        } else {
            data.customData.removeTag("rotX");
        }
    }

    public static float getRotationY(ModelPartData data) {
        return data.customData.hasKey("rotY") ? data.customData.getFloat("rotY") : 0.5F;
    }

    public static void setRotationY(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("rotY", value);
        } else {
            data.customData.removeTag("rotY");
        }
    }

    public static float getRotationZ(ModelPartData data) {
        return data.customData.hasKey("rotZ") ? data.customData.getFloat("rotZ") : 0.5F;
    }

    public static void setRotationZ(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("rotZ", value);
        } else {
            data.customData.removeTag("rotZ");
        }
    }

    public static float getOffsetX(ModelPartData data) {
        return data.customData.hasKey("offsetX") ? data.customData.getFloat("offsetX") : 0.5F;
    }

    public static void setOffsetX(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("offsetX", value);
        } else {
            data.customData.removeTag("offsetX");
        }
    }

    public static float getOffsetY(ModelPartData data) {
        return data.customData.hasKey("offsetY") ? data.customData.getFloat("offsetY") : 0.5F;
    }

    public static void setOffsetY(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("offsetY", value);
        } else {
            data.customData.removeTag("offsetY");
        }
    }

    public static float getOffsetZ(ModelPartData data) {
        return data.customData.hasKey("offsetZ") ? data.customData.getFloat("offsetZ") : 0.5F;
    }

    public static void setOffsetZ(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("offsetZ", value);
        } else {
            data.customData.removeTag("offsetZ");
        }
    }

    public static float getSpinSpeed(ModelPartData data) {
        return data.customData.hasKey("spinSpeed") ? data.customData.getFloat("spinSpeed") : 0.5F;
    }

    public static void setSpinSpeed(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("spinSpeed", value);
        } else {
            data.customData.removeTag("spinSpeed");
        }
    }

    public static float getFloatingSpeed(ModelPartData data) {
        return data.customData.hasKey("floatSpeed") ? data.customData.getFloat("floatSpeed") : 0.5F;
    }

    public static void setFloatingSpeed(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("floatSpeed", value);
        } else {
            data.customData.removeTag("floatSpeed");
        }
    }

    public static float getFloatingDistance(ModelPartData data) {
        return data.customData.hasKey("floatDist") ? data.customData.getFloat("floatDist") : 0.5F;
    }

    public static void setFloatingDistance(ModelPartData data, float value) {
        if(Math.abs(value - 0.5F) > 0.001) {
            data.customData.setFloat("floatDist", value);
        } else {
            data.customData.removeTag("floatDist");
        }
    }

    public static byte getMaterial(ModelPartData data) {
        return data.customData.hasKey("material") ? data.customData.getByte("material") : 0;
    }

    public static void setMaterial(ModelPartData data, byte material) {
        if(material != 0) {
            data.customData.setByte("material", material);
        } else {
            data.customData.removeTag("material");
        }
    }
}
