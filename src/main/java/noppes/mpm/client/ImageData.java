package noppes.mpm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageData {
    private final ResourceLocation location;
    private final boolean isUrl;

    private ThreadDownloadImageData imageDownloadAlt = null;

    private int totalWidth, totalHeight;
    private boolean gotWidthHeight;
    private boolean invalid;

    public ImageData(String directory, boolean x64, ResourceLocation resource, File file) {
        this.location = resource;
        this.isUrl = true;
        this.imageDownloadAlt = new ThreadDownloadImageData(file, directory, SkinManager.field_152793_a, new ImageBufferDownloadAlt(x64));
    }

    public ImageData(String directory, boolean x64, ResourceLocation resource, File file, ResourceLocation defLoc) {
        this.location = resource;
        this.isUrl = true;
        this.imageDownloadAlt = new ThreadDownloadImageData(file, directory, defLoc, new ImageBufferDownloadAlt(x64));
    }

    public void refreshLoad(){
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.loadTexture(this.location, this.imageDownloadAlt);
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
