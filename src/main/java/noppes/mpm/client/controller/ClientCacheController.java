package noppes.mpm.client.controller;

import net.minecraft.util.ResourceLocation;
import noppes.mpm.client.ImageData;
import noppes.mpm.client.PresetController;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.controllers.PermissionController;
import noppes.mpm.util.CacheHashMap;

public class ClientCacheController {
    private static final CacheHashMap<String, CacheHashMap.CachedObject<ImageData>> imageDataCache = new CacheHashMap<>((long) ConfigClient.CacheLife * 60 * 1000);
    public static boolean loaded = false;

    public static ImageData getPlayerSkin(String directory, boolean x64, ResourceLocation resource) {
        synchronized (imageDataCache) {
            if (!imageDataCache.containsKey(resource.getResourcePath())) {
                imageDataCache.put(resource.getResourcePath(), new CacheHashMap.CachedObject<>(new ImageData(directory, x64, resource)));
            }
            return imageDataCache.get(resource.getResourcePath()).getObject();
        }
    }

    public static ImageData getCapeTexture(String directory) {
        synchronized (imageDataCache) {
            if (!imageDataCache.containsKey(directory)) {
                imageDataCache.put(directory, new CacheHashMap.CachedObject<>(new ImageData(directory)));
            }
            return imageDataCache.get(directory).getObject();
        }
    }

    public static void createCache() {
        ClientDataController.Instance();
        loaded = true;
    }

    public static void clearAllCache() {
        ClientDataController.ClearInstance();
        ClientCacheController.imageDataCache.clear();
        ClientPermController.clientPerms.clear();
        PresetController.Instance.presets.clear();
        PresetController.Instance.loaded = false;
        loaded = false;
    }

    public static void clearDataCache() {
        ClientDataController.ClearInstance();
        ClientCacheController.imageDataCache.clear();
    }

    public static void clearSkinData() {
        ClientCacheController.imageDataCache.clear();
    }
}
