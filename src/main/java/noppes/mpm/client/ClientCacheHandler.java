package noppes.mpm.client;

import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelData;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.util.CacheHashMap;

import java.io.File;

public class ClientCacheHandler {
    private static final CacheHashMap<String, CacheHashMap.CachedObject<ImageData>> imageDataCache = new CacheHashMap<>((long) ConfigClient.CacheLife * 60 * 1000);
    private static final CacheHashMap<String, CacheHashMap.CachedObject<ModelData>> playerData = new CacheHashMap<>((long) ConfigClient.CacheLife * 60 * 1000);

    public static ImageData getPlayerSkin(String directory, boolean x64, ResourceLocation resource, File file) {
        synchronized (imageDataCache) {
            if (!imageDataCache.containsKey(resource.getResourcePath())) {
                imageDataCache.put(resource.getResourcePath(), new CacheHashMap.CachedObject<>(new ImageData(directory, x64, resource, file)));
            }
            return imageDataCache.get(resource.getResourcePath()).getObject();
        }
    }

    public static ModelData getPlayerData(String uuid) {
        synchronized (playerData) {
            if (!playerData.containsKey(uuid)) {
                return null;
            }
            return playerData.get(uuid).getObject();
        }
    }

    public static void putPlayerData(String uuid, ModelData modelData) {
        synchronized (playerData) {
            playerData.put(uuid, new CacheHashMap.CachedObject<>(modelData));
        }
    }

    public static void clearCache() {
        ClientCacheHandler.imageDataCache.clear();
        ClientCacheHandler.playerData.clear();
    }
}
