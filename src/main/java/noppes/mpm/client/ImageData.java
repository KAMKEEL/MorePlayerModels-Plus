package noppes.mpm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageData {
    private final ResourceLocation location;
    private final boolean isUrl;

    private ImageDownloadAlt imageDownloadAlt = null;

    private int totalWidth, totalHeight;
    private boolean gotWidthHeight;
    private boolean invalid;

    public ImageData(String directory, boolean x64, ResourceLocation resource) {
        this.location = resource;
        this.isUrl = true;
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        this.imageDownloadAlt = new ImageDownloadAlt(null, directory, SkinManager.field_152793_a, new ImageBufferDownloadAlt(x64));
        texturemanager.loadTexture(this.location, this.imageDownloadAlt);
    }

    public ImageData(String directory) {
        this.location = new ResourceLocation(directory);
        if (directory.startsWith("https://")) {
            this.isUrl = true;
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            this.imageDownloadAlt = new ImageDownloadAlt(null, directory, new ResourceLocation("customnpcs:textures/gui/invisible.png"), new ImageBufferDownloadAlt(true, false));
            texturemanager.loadTexture(this.location, this.imageDownloadAlt);
        } else {
            this.isUrl = false;
        }
    }

    public void bindTexture() {
        ResourceLocation location = this.getLocation();
        if (location != null && !this.invalid) {
            try {
                Minecraft.getMinecraft().getTextureManager().bindTexture(location);
            } catch (Exception exception) {
                this.invalid = true;
            }
        }
    }

    public ResourceLocation getLocation() {
        return this.isUrl && this.imageDownloadAlt.getBufferedImage() == null ? null : this.location;
    }

    public ITextureObject getITexture() {
        return this.imageDownloadAlt;
    }

    public boolean imageLoaded() {
        if (!this.gotWidthHeight) {
            try {
                if (!this.isUrl) {
                    this.getWidthHeight();
                } else {
                    this.getURLWidthHeight();
                }
            } catch (Exception ignored) {}
        }
        return !this.invalid && this.location != null && this.gotWidthHeight;
    }

    private void getURLWidthHeight(){
        if(this.imageDownloadAlt.getBufferedImage() != null && !this.invalid) {
            this.gotWidthHeight = true;
            this.totalWidth = this.imageDownloadAlt.getBufferedImage().getWidth();
            this.totalHeight = this.imageDownloadAlt.getBufferedImage().getHeight();
            correctWidthHeight();
        }
    }

    public boolean invalid() {
        return this.invalid;
    }


    private void getWidthHeight() throws IOException {
        if (this.invalid) {
            return;
        }

        InputStream inputstream = null;

        try {
            IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource(this.location);
            inputstream = iresource.getInputStream();
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            this.gotWidthHeight = true;
            this.totalWidth = bufferedimage.getWidth();
            this.totalHeight = bufferedimage.getHeight();
            correctWidthHeight();
        } catch (Exception e) {
            this.invalid = true;
        } finally {
            if (inputstream != null) {
                inputstream.close();
            }
        }
    }

    private void correctWidthHeight(){
        this.totalWidth = Math.max(this.totalWidth, 1);
        this.totalHeight = Math.max(this.totalHeight, 1);
    }

    public int getTotalWidth() {
        return this.gotWidthHeight ? this.totalWidth : -1;
    }

    public int getTotalHeight() {
        return this.gotWidthHeight ? this.totalHeight : -1;
    }
}
