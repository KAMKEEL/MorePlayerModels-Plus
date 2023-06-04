package noppes.mpm.client;

import net.minecraft.util.ResourceLocation;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.util.CacheHashMap;

import java.io.File;

public class ClientCacheHandler {
    private static final CacheHashMap<String, CacheHashMap.CachedObject<ImageData>> imageDataCache = new CacheHashMap<>((long) ConfigClient.CacheLife * 60 * 1000);

    public static ImageData getPlayerSkin(String directory, boolean x64, ResourceLocation resource, File file) {
        synchronized (imageDataCache) {
            if (!imageDataCache.containsKey(resource.getResourcePath())) {
                imageDataCache.put(resource.getResourcePath(), new CacheHashMap.CachedObject<>(new ImageData(directory, x64, resource, file)));
            }
            return imageDataCache.get(resource.getResourcePath()).getObject();
        }
    }
}
